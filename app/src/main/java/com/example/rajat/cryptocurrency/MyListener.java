package com.example.rajat.cryptocurrency;

import java.util.HashMap;


public interface MyListener {

    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode);
}
