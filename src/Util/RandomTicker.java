package Util;

import java.util.Random;

public class RandomTicker{
    public Random rand = new Random();
    public RandomTicker(){
        // nothing to do here.
    }

    public int randint(int min, int max){
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}