package com.example.bjheggset.buckets;

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
}
