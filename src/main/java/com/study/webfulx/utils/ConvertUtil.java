package com.study.webfulx.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ConvertUtil {

    public static Output convert(SampleDto input) {
        Output out = new Output();
        out.setName(input.getLastName() + " " + input.getFirstName());
        out.setAge(calAge(input.getBirthday()));
        return out;
    }

    private static long calAge(String birthday) {
        LocalDate birth = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return ChronoUnit.YEARS.between(birth, LocalDate.now());
    }
}
