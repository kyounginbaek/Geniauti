package com.geniauti.geniauti;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Statistics implements Serializable {

    public Statistics(String date_value, HashMap<String, Object> behavior_freq, HashMap<String, Object> summary, HashMap<String, Object> type, HashMap<String, Object> reason_type, HashMap<String, Object> place) {
        this.date_value = date_value;
        this.behavior_freq = behavior_freq;
        this.summary = summary;
        this.type = type;
        this.reason_type = reason_type;
        this.place = place;
    }

    public String date_value;
    public HashMap<String, Object> behavior_freq;
    public HashMap<String, Object> summary;
    public HashMap<String, Object> type;
    public HashMap<String, Object> reason_type;
    public HashMap<String, Object> place;

}
