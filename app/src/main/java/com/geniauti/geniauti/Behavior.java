package com.geniauti.geniauti;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Behavior implements Serializable {

    public Behavior() {

    }

    public Date start_time;
    public Date end_time;
    public String place;
    public String categorization;
    public String current_behavior;
    public String before_behavior;
    public String after_behavior;
    public HashMap<String,Boolean> type;
    public int intensity;
    public HashMap<String, Boolean> reason;
    public Date created_at;
    public Date updated_at;
    public String uid;
    public String name;
    public String cid;

}
