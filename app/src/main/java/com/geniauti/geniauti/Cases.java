package com.geniauti.geniauti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Cases implements Serializable {

    public Cases(String title, String backgroundInfo, String behavior, List<HashMap<String,Object>> cause, List<HashMap<String,Object>> solution, String effect, HashMap<String,Object> tags_reason, HashMap<String,Object> tags_type,  String id) {
        this.title = title;
        this.backgroundInfo = backgroundInfo;
        this.behavior = behavior;
        this.cause = cause;
        this.solution = solution;
        this.effect = effect;
        this.tags_reason = tags_reason;
        this.tags_type = tags_type;
        this.id = id;


    }

    public HashMap<String, Object> firebase_input_data() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("title", title);
        data.put("backgroundInfo", backgroundInfo);
        data.put("behavior", behavior);
        data.put("cause", cause);
        data.put("solution", solution);
        data.put("effect", effect);
        data.put("tags_reason", tags_reason);
        data.put("tags_type", tags_type);

        return data;
    }

    public String title;
    public String backgroundInfo;
    public String behavior;
    public List<HashMap<String,Object>> cause;
    public List<HashMap<String,Object>> solution;
    public String effect;
    public HashMap<String,Object> tags_reason;
    public HashMap<String,Object> tags_type;
    public String id;
}
