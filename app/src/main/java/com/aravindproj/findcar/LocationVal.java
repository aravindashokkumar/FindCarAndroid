package com.aravindproj.findcar;

import java.util.HashMap;

/**
 * Created by aravindashokkumar on 27/7/15.
 */
public class LocationVal extends HashMap<String,String> {

    public LocationVal(String loc, double lat, double longi)
    {
        put("loc", loc);
        put("lat", Double.toString(lat));
        put("longi", Double.toString(longi));
    }
}
