package com.example.flutter_peluhttp;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
    public int statusCode;
    public String response;

    public Response(int statusCode, String response){
        this.statusCode = statusCode;
        this.response = response;
    }

    public String toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("statusCode", this.statusCode);
            jsonObject.put("response", this.response);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }
}
