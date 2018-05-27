package com.hosigus.askposts.item.jsonBean;

import com.hosigus.tools.items.JSONBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 某只机智 on 2018/5/25.
 * "status": 200,
 * "info": "success",
 * "data": null
 */
public class BaseBean extends JSONBean {
    private int status;
    private String info;
    private String data;
    @Override
    public void parser(JSONObject object) {
        try {
            status = object.getInt("status");
            info = object.getString("info");
            data = isStatusOK() && object.has("data") ? object.getString("data") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getData() {
        return data;
    }

    public boolean isStatusOK() {
        return status == 200;
    }

    public boolean isGoodJson() {
        return isStatusOK() && data != null && !data.isEmpty();
    }
}
