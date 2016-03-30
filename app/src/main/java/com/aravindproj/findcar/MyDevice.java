package com.aravindproj.findcar;

import java.util.HashMap;

/**
 * Created by aravindashokkumar on 26/7/15.
 */
public class MyDevice extends HashMap<String, String> {

        String name;
        int range;

        public MyDevice(String name, int range) {
            put("name", name);
            put("age", Integer.toString(range));

        }
}
