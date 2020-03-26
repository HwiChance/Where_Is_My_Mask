package com.hwichance.android.WhereIsMyMask.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocationData implements Parcelable {

    @SerializedName("place_name")
    private String place_name;

    @SerializedName("address_name")
    private String address_name;

    @SerializedName("road_address_name")
    private String road_address_name;

    @SerializedName("x")
    private double coordX;

    @SerializedName("y")
    private double coordY;

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String _place_name) {
        this.place_name = _place_name;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String _address_name) {
        this.address_name = _address_name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String _road_address_name) {
        this.road_address_name = _road_address_name;
    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double _coordX) {
        this.coordX = _coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double _coordY) {
        this.coordY = _coordY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place_name);
        dest.writeString(address_name);
        dest.writeString(road_address_name);
        dest.writeDouble(coordX);
        dest.writeDouble(coordY);
    }

    protected LocationData(Parcel in) {
        place_name = in.readString();
        address_name = in.readString();
        road_address_name = in.readString();
        coordX = in.readDouble();
        coordY = in.readDouble();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };
}
