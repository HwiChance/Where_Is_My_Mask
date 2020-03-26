package com.hwichance.android.WhereIsMyMask.data;

import com.google.gson.annotations.SerializedName;

public class MaskData {

    @SerializedName("name")
    private String storeName;

    @SerializedName("addr")
    private String storeAddress;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    @SerializedName("stock_at")
    private String warehousingTime;

    @SerializedName("remain_stat")
    private String remainNumber;

    @SerializedName("created_at")
    private String dataCreatedTime;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String _storeName) {
        this.storeName = _storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String _storeAddress) {
        this.storeAddress = _storeAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double _latitude) {
        this.latitude = _latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double _longitude) {
        this.longitude = _longitude;
    }

    public String getWarehousingTime() {
        if(warehousingTime != null){
            return warehousingTime;
        }
        else {
            return "----/--/-- --:--:--";
        }
    }

    public void setWarehousingTime(String _warehousingTime) {
        this.warehousingTime = _warehousingTime;
    }

    public String getRemainNumber() {
        if(remainNumber != null){
            return remainNumber;
        }
        else {
            return "default";
        }
    }

    public void setRemainNumber(String _remainNumber) {
        this.remainNumber = _remainNumber;
    }

    public String getDataCreatedTime() {
        if(dataCreatedTime != null) {
            return dataCreatedTime;
        }
        else {
            return "----/--/-- --:--:--";
        }
    }

    public void setDataCreatedTime(String _dataCreatedTime) {
        this.dataCreatedTime = _dataCreatedTime;
    }
}
