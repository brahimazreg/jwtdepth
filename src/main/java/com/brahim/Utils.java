package com.brahim;

import java.util.Date;

public class Utils {
    private Utils(){}

    public static  String generateUUID(){
        Date date = new Date();
        long time = date.getTime();
        return "bill:"+time;
    }
}
