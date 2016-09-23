package com.hanuorsocialcops.socialcops.Utils;

/**
 * Created by Shantanu Johri on 9/23/2016.
 */

public class InformationHandler {
    private final String USERNAME;
    public InformationHandler(String keyid) {
        this.USERNAME = keyid;
    }
    public String getKEYID(){
        return USERNAME;
    }

}
