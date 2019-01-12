package com.geniauti.geniauti;

import java.io.Serializable;
import java.util.HashMap;

public class Bookmark implements Serializable {

    public Bookmark(int position, String title, String place, String categorization, HashMap<String, Object> type, int intensity, HashMap<String, Object> reason_type, HashMap<String, Object> reason) {
        this.position = position;
        this.title = title;
        this.place = place;
        this.categorization = categorization;
        this.type = type;
        this.intensity = intensity;
        this.reason_type = reason_type;
        this.reason = reason;
    }
    public int position;
    public String title;
    public String place;
    public String categorization;
    public HashMap<String, Object> type;
    public int intensity;
    public HashMap<String, Object> reason_type;
    public HashMap<String, Object> reason;
}
