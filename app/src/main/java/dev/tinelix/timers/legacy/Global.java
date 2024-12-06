package dev.tinelix.timers.legacy;

public class Global {
    // Workaround for Plurals methods, because they only work with int (32-bit) numbers.
    public static int getEndNumberFromLong(long number) {
        int end_number = 0;
        if(Long.toString(number).endsWith("1")) {
            end_number = 1;
        } else if(Long.toString(number).endsWith("2")) {
            end_number = 2;
        } else if(Long.toString(number).endsWith("3")) {
            end_number = 3;
        } else if(Long.toString(number).endsWith("4")) {
            end_number = 4;
        } else if(Long.toString(number).endsWith("5")) {
            end_number = 5;
        } else if(Long.toString(number).endsWith("6")) {
            end_number = 6;
        } else if(Long.toString(number).endsWith("7")) {
            end_number = 7;
        } else if(Long.toString(number).endsWith("8")) {
            end_number = 8;
        } else if(Long.toString(number).endsWith("9")) {
            end_number = 9;
        }
        return end_number;
    }
}
