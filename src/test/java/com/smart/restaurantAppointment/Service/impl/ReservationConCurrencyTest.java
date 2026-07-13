package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.*;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.repository.RestaurantTableRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReservationConCurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    private User testCustomer;
    private Restaurant testRestaurant;
    private final LocalDateTime bookingTime = LocalDateTime.now().plusDays(7).withHour(19).withMinute(0);

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
        Merchant merchant  = new Merchant();
        String unique = UUID.randomUUID().toString();
        merchant.setEmail("merchant-" + unique + "@test.com");
        merchant.setSlug("merchant-" + unique);
        merchant = merchantRepository.save(merchant);

        User customer  = new User();
        customer.setRole(UserRole.CUSTOMER);
        customer.setMerchant(merchant);

        testCustomer   = userRepository.save(customer);

        Restaurant restaurant = new Restaurant();
        restaurant.setMerchant(merchant);
        restaurant.setOpeningTime(LocalTime.of(10, 0));   // opens 10:00
        restaurant.setClosingTime(LocalTime.of(22, 0));
        restaurant.setSlotIntervalMinutes(30);
        restaurant.setDefaultBookingMinutes(60);

        testRestaurant = restaurantRepository.save(restaurant);
        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(testRestaurant);
        table.setCapacity(10);
        table.setName("Table 1");
        tableRepository.save(table);
    }

    @Test
    void onlyOneOfTenSimultaneousBookingsShouldSucceed() throws Exception {
        int threadCount = 10;

        // Starting gun — all threads freeze on it until we open the gate
        CountDownLatch startGate = new CountDownLatch(1);

        // "All done?" tracker — main thread waits on it
        CountDownLatch finishGate = new CountDownLatch(threadCount);

//      Init 10 workers
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        // Thread-safe counters
        AtomicInteger successes = new AtomicInteger(0);
        AtomicInteger failures  = new AtomicInteger(0);

        // One booking request, used by ALL 10 threads — they all fight for it
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setRestaurantId(testRestaurant.getId());
        request.setReservationDateTime(bookingTime);
        request.setSize(1);

        // Hand 10 identical jobs to the pool
        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                try {
                    // SecurityContext is thread-local — each thread sets its own
                    setSecurityContextFor(testCustomer);

                    // Freeze here until startGate opens
                    startGate.await();

                    // Race! All 10 try to book at the same millisecond
                    reservationService.createReservation(request,testCustomer);
                    successes.incrementAndGet();
                } catch (Exception e) {
                    // Redis lock blocked us, or any other error → count as failure
                    failures.incrementAndGet();
                    System.out.println("FAILED: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                } finally {
                    // ALWAYS report done, even on failure
                    finishGate.countDown();
                }
            });
        }

        // Fire the gun — all 10 unfreeze at once
        startGate.countDown();

        // Wait for all workers to finish; fail loudly on timeout instead of
        // asserting against half-finished counters
        boolean allFinished = finishGate.await(60, TimeUnit.SECONDS);
        pool.shutdown();
        assertTrue(allFinished, "Not all booking threads finished within 60s");

        System.out.println("Successes: " + successes.get());
        System.out.println("Failures: "  + failures.get());

        // The verdict: exactly one winner, nine blocked
        assertEquals(1, successes.get(), "Only one booking should succeed");
        assertEquals(9, failures.get(),  "Nine bookings should be blocked");
    }

    // Helper — manually puts a User into Spring Security's thread-local context
    private void setSecurityContextFor(User user) {
            MyUserDetails details = new MyUserDetails(user);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            details, null, details.getAuthorities())
            );
        }


    }
