package com.talhadincer.creditModule.core;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@NoArgsConstructor
public class DateUtil {

    /**
     * format to ISO Local Date: '2011-12-03'
     */
    public static String toStringNow() {
        return LocalDate.now().format(ISO_LOCAL_DATE);
    }

    public static String toString(LocalDate localDate) {

        if (localDate == null) {
            return "0";
        }

        return localDate.format(ISO_LOCAL_DATE);
    }

    public static LocalDate getFirstDayOfTheMonth(LocalDate localDate) {
        return localDate.withDayOfMonth(1);
    }

    public static LocalDate getFirstDayOfTheFutureMonth(long monthFromCurrent) {

        LocalDate futureDate = LocalDate.now().plusMonths(monthFromCurrent);
        return getFirstDayOfTheMonth(futureDate);
    }

    /**
     * @param givenDate
     * @return positive value if givenDate is before, negative if it's after.
     */
    public static long dayDifferenceToCurrentDate(LocalDate givenDate) {
        return ChronoUnit.DAYS.between(givenDate, LocalDate.now());
    }
}
