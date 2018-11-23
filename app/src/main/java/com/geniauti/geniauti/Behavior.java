package com.geniauti.geniauti;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Behavior implements Serializable {

    public Behavior(Date start_time, Date end_time, String place, String categorization, String current_behavior, String before_behavior, String after_behavior, HashMap<String, String> type, int intensity, HashMap<String, String> reason, String created_at, String updated_at, String uid, String name, String cid) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
        this.categorization = categorization;
        this.current_behavior = current_behavior;
        this.before_behavior = before_behavior;
        this.after_behavior = after_behavior;
        this.type = type;
        this.intensity = intensity;
        this.reason = reason;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.uid = uid;
        this.name = name;
        this.cid = cid;
    }

    public Date start_time;
    public Date end_time;
    public String place;
    public String categorization;
    public String current_behavior;
    public String before_behavior;
    public String after_behavior;
    public HashMap<String,String> type;
    public int intensity;
    public HashMap<String, String> reason;
    public String created_at;
    public String updated_at;
    public String uid;
    public String name;
    public String cid;

}
