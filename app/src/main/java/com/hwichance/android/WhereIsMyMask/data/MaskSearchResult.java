package com.hwichance.android.WhereIsMyMask.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MaskSearchResult {
    @SerializedName("stores")
    @Expose
    private ArrayList<MaskData> stores;

    public ArrayList<MaskData> getStores() {
        return stores;
    }

    public void setStores(ArrayList<MaskData> _stores) {
        this.stores = _stores;
    }
}
