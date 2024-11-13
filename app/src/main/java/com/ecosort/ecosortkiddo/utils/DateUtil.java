package com.ecosort.ecosortkiddo.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

public class DateUtil {

    public static double toJulianDayNumber(LocalDateTime localDateTime) {
        long epochSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);
        double julianDay = (epochSeconds / 86400.0) + 2440587.5;
        return julianDay;
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        double julianDayNumber = toJulianDayNumber(now);
        System.out.println("Julian Day Number: " + julianDayNumber);
    }
}
