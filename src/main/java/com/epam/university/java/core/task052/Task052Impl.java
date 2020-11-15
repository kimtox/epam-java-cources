package com.epam.university.java.core.task052;

public class Task052Impl implements Task052 {
    @Override
    public boolean validateCard(long number) {
        if (String.valueOf(number).length() < 14) {
            throw new IllegalArgumentException();
        }
        long num = number;
        String cardNumberString = Long.toString(num);
        char[] digitsFromCardNumber = cardNumberString.toCharArray();
        int[] cardNumber = new int[digitsFromCardNumber.length - 1];
        for (char c : digitsFromCardNumber) {
            System.out.print(c);
        }
        System.out.println("");
        for (int i = 0; i < digitsFromCardNumber.length - 1; i++) {
            if (i % 2 == 0) {
                int integer = charToInt(digitsFromCardNumber[i]) * 2;
                if (integer >= 10) {
                    cardNumber[i] = getTheSum(integer);
                } else {
                    cardNumber[i] = integer;
                }
            } else {
                cardNumber[i] = charToInt(digitsFromCardNumber[i]);
            }
        }
        for (int i : cardNumber) {
            System.out.print(i);
        }
        int sumOfDigitsInCardNumber = 0;
        for (int i : cardNumber) {
            sumOfDigitsInCardNumber += i;
        }
        char checkDigit = digitsFromCardNumber[digitsFromCardNumber.length - 1];
        return (10 - (sumOfDigitsInCardNumber % 10) == charToInt(checkDigit));
    }

    private Integer charToInt(char c) {
        return Integer.parseInt(String.valueOf(c));
    }

    private Integer getTheSum(Integer integer) {
        Integer[] numbers = new Integer[2];
        int i = 0;
        while (integer > 0) {
            numbers[i] = integer % 10;
            integer = integer / 10;
            i++;
        }

        return (numbers[0] + numbers[1]);
    }
}
