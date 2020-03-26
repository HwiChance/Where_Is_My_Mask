package com.hwichance.android.WhereIsMyMask.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationSearchResult {
    @SerializedName("meta")
    @Expose
    private LocationSearchMeta locationSearchMeta;

    @SerializedName("documents")
    @Expose
    private ArrayList<LocationData> documents;


    public LocationSearchMeta getLocationSearchMeta() {
        return locationSearchMeta;
    }

    public void setLocationSearchMeta(LocationSearchMeta _locationSearchMeta) {
        this.locationSearchMeta = _locationSearchMeta;
    }

    public ArrayList<LocationData> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<LocationData> _documents) {
        this.documents = _documents;
    }

}
