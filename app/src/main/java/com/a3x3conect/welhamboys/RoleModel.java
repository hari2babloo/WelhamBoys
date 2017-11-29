package com.a3x3conect.welhamboys;

import org.json.JSONArray;
import org.json.JSONException;


public class RoleModel {
    public RoleModel(JSONArray jsonArray) throws JSONException {
        place_name=jsonArray.getString(1);
        id=jsonArray.getString(0);
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getId() {
        return id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String place_name;
    private String id;




    public RoleModel(String place_name, String id)
    {
        this.place_name=place_name;
        this.id=id;
    }
}
