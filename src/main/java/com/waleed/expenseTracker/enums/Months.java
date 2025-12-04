package com.waleed.expenseTracker.enums;

public enum Months {
    JANUARY(1, "January"),
    FEBRUARY(2, "February"),
    MARCH(3, "March"),
    APRIL(4, "April"),
    MAY(5, "May"),
    JUNE(6, "June"),
    JULY(7, "July"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "October"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "December");

    private final int number;
    private final String displayName;

    Months(int number, String displayName) {
        this.number = number;
        this.displayName = displayName;
    }

    public int getNumber() {
        return number;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Optional helper: get month by number
    public static Months fromNumber(int number) {
        for (Months month : values()) {
            if (month.number == number) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month number: " + number);
    }
}
