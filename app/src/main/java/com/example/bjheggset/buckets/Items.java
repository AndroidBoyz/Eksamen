package com.example.bjheggset.buckets;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimRo on 04/04/2017.
 */

public class Items {
    public int itemID;
    public String items;

    public Items (int itemID, String items) {
        this.itemID = itemID;
        this.items = items;
    }

    public String getItems(){
        return this.items;
    }

    public int getItemID(){
        return this.itemID;
    }

    @Override
    public String toString(){
        return items;
    }

    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("itemID", itemID);
            obj.put("items", items);
        } catch (JSONException e) {

        }
        return obj;
    }

    @Override
    public boolean equals (Object object) {
        boolean same = false;

        if(object != null && object instanceof Items) {
            same = this.itemID == ((Items) object).itemID;
        }

        return same;
    }
}
