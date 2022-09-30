package com.bitbubble.api.app.util;

import java.util.Random;

public class CommonUtil {
    public String RandomNumber(){
        Random rad =  new Random();
        int number = rad.nextInt(999999);
        return String.format("%06d", number);
    }
}
