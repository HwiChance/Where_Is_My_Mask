package com.hwichance.android.WhereIsMyMask.utils;

import com.hwichance.android.WhereIsMyMask.R;
import com.hwichance.android.WhereIsMyMask.data.MaskData;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class CustomMarkerUtils {

    public static MapPOIItem customMarker (MaskData maskData) {

        MapPOIItem customMarker = new MapPOIItem();

        customMarker.setUserObject(maskData);
        customMarker.setItemName(maskData.getStoreName());
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);

        switch (maskData.getRemainNumber()) {
            case "plenty":
                customMarker.setCustomImageResourceId(R.drawable.maskfull_60px);
                break;
            case "some":
                customMarker.setCustomImageResourceId(R.drawable.masksome_60px);
                break;
            case "few":
                customMarker.setCustomImageResourceId(R.drawable.maskfew_60px);
                break;
            default:
                customMarker.setCustomImageResourceId(R.drawable.maskempty_60px);
                break;
        }
        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(maskData.getLatitude(), maskData.getLongitude()));
        customMarker.setCustomImageAnchor(0.5f, 1.0f);

        return customMarker;

    }

    public static void createMarkers (MapView mapView, ArrayList<MaskData> maskDataList) {
        for(MaskData data : maskDataList) {
            mapView.addPOIItem(customMarker(data));
        }
    }
}
