package com.geniauti.geniauti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cases implements Serializable {

    public Cases(String case_title, String case_backgroundInfo, String case_behavior, List<HashMap<String,String>> case_cause, List<HashMap<String,String>> case_solution, String case_effect, HashMap<String,String> case_tags, String case_id) {
        this.case_title = case_title;
        this.case_backgroundInfo = case_backgroundInfo;
        this.case_behavior = case_behavior;
        this.case_cause = case_cause;
        this.case_solution = case_solution;
        this.case_effect = case_effect;
        this.case_tags = case_tags;
        this.case_id = case_id;


    }

    public HashMap<String, Object> firebase_input_data() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("case_title", case_title);
        data.put("case_backgroundInfo", case_backgroundInfo);
        data.put("case_behavior", case_behavior);
        data.put("case_cause", case_cause);
        data.put("case_solution", case_solution);
        data.put("case_effect", case_effect);
        data.put("case_tags", case_tags);

        return data;
    }

    public String case_title;
    public String case_backgroundInfo;
    public String case_behavior;
    public List<HashMap<String,String>> case_cause;
    public List<HashMap<String,String>> case_solution;
    public String case_effect;
    public HashMap<String,String> case_tags;
    public String case_id;
}
