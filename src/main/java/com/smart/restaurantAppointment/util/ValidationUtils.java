package com.smart.restaurantAppointment.util;

public final class ValidationUtils {
    private static final String MOBILE_REGEX = "^(\\+?6?01)[02-46-9]-?[0-9]{7}$|^(\\+?6?01)1-?[0-9]{8}$";

//  Since it cannot be new as a util class
    private ValidationUtils() {}

    public static boolean isValidPhoneNumber (String mobilePhoneNumber) {
        if (mobilePhoneNumber.matches(MOBILE_REGEX)) {
            return false;
        }
        return true;
    }
}
