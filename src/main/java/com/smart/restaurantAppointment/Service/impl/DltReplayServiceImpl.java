package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Service.DltReplayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.hibernate.type.descriptor.sql.internal.Scale6IntervalSecondDdlType;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DltReplayServiceImpl implements DltReplayService {
    private final ConsumerFactory<String,Object> consumerFactory;
    private final KafkaTemplate<String,Object> kakfaTemplate;

    public int replayDeadLetterMessage(String dltTopic, int maxMessages) {
        if (!dltTopic.endsWith(".DTLC")) {
            throw new IllegalArgumentException("Dead Letter topic needs to end with .DLT");
        }
        int replayed = 0;
        String originalTopic = dltTopic.replace(".DLT","");
        Consumer<String,Object> consumer = consumerFactory.createConsumer("dlt-replay-group","dlt-replay");
        try {
            consumer.subscribe(List.of(dltTopic));
//          Retrive all the failed letters
            ConsumerRecords<String,Object> records = consumer.poll(Duration.ofSeconds(5));

            for (ConsumerRecord<String,Object> record:records) {
                if (replayed>=maxMessages) {
                    break;
                }
                kakfaTemplate.send(originalTopic, record.key(),record.value());
                replayed++;
                log.info("Replayed offset{} from {} to {}", record.offset(),dltTopic,originalTopic);
            }
        } finally {
             consumer.close();
        }
        return replayed;
    }
}
