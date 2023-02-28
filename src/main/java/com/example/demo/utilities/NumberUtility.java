package com.example.demo.utilities;

import java.util.Random;

public class NumberUtility {
    private NumberUtility (){}
    private static final Random RANDOM = new Random();

    public static int getRandomInt (final int max){
        return RANDOM.nextInt(max);
    }
}
