package com.example.portfolio_simple_spring_mvc.application.util;

import java.time.LocalDate;
import java.time.Period;

public final class AgeCalculator {

    private AgeCalculator() {}

    public static int getAge(LocalDate birhOfDate) {
        return Period.between(birhOfDate, LocalDate.now()).getYears();
    }
}
