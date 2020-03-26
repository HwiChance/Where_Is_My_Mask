package com.hwichance.android.WhereIsMyMask.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hwichance.android.WhereIsMyMask.R;
import com.hwichance.android.WhereIsMyMask.SearchAddressActivity;
import com.hwichance.android.WhereIsMyMask.calloutballoon.CustomCalloutBalloonAdapter;
import com.hwichance.android.WhereIsMyMask.data.LocationData;
import com.hwichance.android.WhereIsMyMask.data.MaskData;
import com.hwichance.android.WhereIsMyMask.data.MaskSearchResult;
import com.hwichance.android.WhereIsMyMask.retrofit.MaskSearchRetrofitAPI;
import com.hwichance.android.WhereIsMyMask.utils.CustomMarkerUtils;
import com.hwichance.android.WhereIsMyMask.utils.RetrofitUtils;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class FindStoreFragment extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private static final int GPS_SETTINGS_REQUEST_CODE = 100;
    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 101;
    private MapView mapView;
    private LocationManager locationManager;
    private MapPoint currentMapPoint;

    @BindView(R.id.btn_refresh) Button btn_refresh;
    @BindView(R.id.btn_address_search) Button btn_address_search;
    @BindView(R.id.btn_refresh_current_screen) Button btn_refresh_current_screen;
    @BindView(R.id.btn_current_location) ToggleButton btn_current_location;
    @BindView(R.id.kakao_map_view) ViewGroup kakao_map_view_container;
    @BindView(R.id.notice_layout) LinearLayout notice_layout;
    @BindView(R.id.btn_hide) Button btn_hide;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private final static String BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com";
    private Retrofit retrofit;
    private MaskSearchRetrofitAPI maskSearchRetrofitAPI;
    private Call<MaskSearchResult> maskSearchResultCall;
    private ArrayList<MaskData> maskDataArrayList;

    public FindStoreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_store, container, false);

        ButterKnife.bind(this, view);

        retrofit = RetrofitUtils.createRetrofit(BASE_URL);
        maskSearchRetrofitAPI = retrofit.create(MaskSearchRetrofitAPI.class);
        maskDataArrayList = new ArrayList<>();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mapView = new MapView(getContext());
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(view));
        kakao_map_view_container.addView(mapView);

        setCustomMapView();

        btn_address_search.setOnClickListener(v -> {
            Intent SearchIntent = new Intent(getContext(), SearchAddressActivity.class);
            SearchIntent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(SearchIntent, SEARCH_ACTIVITY_REQUEST_CODE);
        });

        btn_current_location.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertDialog.Builder GPSAlertDialogBuilder = new AlertDialog.Builder(getContext());
                    GPSAlertDialogBuilder.setMessage(R.string.gps_need);
                    GPSAlertDialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                        Intent GPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        GPSSettingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(GPSSettingIntent, GPS_SETTINGS_REQUEST_CODE);
                    });
                    GPSAlertDialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        btn_current_location.setChecked(false);
                    });
                    AlertDialog GPSAlertDialog = GPSAlertDialogBuilder.create();
                    GPSAlertDialog.show();
                }
                else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                    if(currentMapPoint != null) {
                        mapView.setMapCenterPoint(currentMapPoint, true);
                        searchingStoreList(currentMapPoint.getMapPointGeoCoord().latitude, currentMapPoint.getMapPointGeoCoord().longitude);
                    }
                }
            }
            else {
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
            }
        });

        btn_refresh.setOnClickListener(v -> {
            MapPoint.GeoCoordinate currentGeoCoord = mapView.getMapCenterPoint().getMapPointGeoCoord();
            searchingStoreList(currentGeoCoord.latitude, currentGeoCoord.longitude);
        });

        btn_refresh_current_screen.setOnClickListener(v -> {
            MapPoint.GeoCoordinate currentGeoCoord = mapView.getMapCenterPoint().getMapPointGeoCoord();
            searchingStoreList(currentGeoCoord.latitude, currentGeoCoord.longitude);
            btn_refresh_current_screen.setVisibility(View.INVISIBLE);
            btn_current_location.setVisibility(View.VISIBLE);
            btn_refresh.setVisibility(View.VISIBLE);
        });

        btn_hide.setOnClickListener(v -> {
            notice_layout.setVisibility(View.INVISIBLE);
        });

        checkGPS();
        return view;
    }

    private void checkGPS() {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            btn_current_location.setChecked(false);
        }
        else{
          btn_current_location.setChecked(true);
        }
    }

    private void setCustomMapView() {
        MapPOIItem.ImageOffset currentMapPointOffset = new MapPOIItem.ImageOffset(15, 15);
        mapView.setCustomCurrentLocationMarkerImage(R.drawable.location_point, currentMapPointOffset);
        mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.location_point, currentMapPointOffset);

        mapView.setZoomLevel(1, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEARCH_ACTIVITY_REQUEST_CODE: {
                if(data != null) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving );
                    LocationData locationData = data.getParcelableExtra("selectedPlace");
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(locationData.getCoordY(), locationData.getCoordX()), true);
                    searchingStoreList(locationData.getCoordY(), locationData.getCoordX());
                    btn_refresh_current_screen.setVisibility(View.INVISIBLE);
                    btn_current_location.setVisibility(View.VISIBLE);
                    btn_refresh.setVisibility(View.VISIBLE);
                }
                break;
            }
            case GPS_SETTINGS_REQUEST_CODE: {
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    btn_current_location.setChecked(false);
                }
                else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    private void searchingStoreList(double latitude, double longitude) {
        progressBar.setVisibility(View.VISIBLE);
        maskSearchResultCall = maskSearchRetrofitAPI.getStoreList(latitude, longitude, 750);
        maskSearchResultCall.enqueue(retrofitCallback);
    }

    private Callback<MaskSearchResult> retrofitCallback = new Callback<MaskSearchResult>() {
        @Override
        public void onResponse(Call<MaskSearchResult> call, Response<MaskSearchResult> response) {
            ArrayList<MaskData> result = response.body().getStores();
            if(!maskDataArrayList.equals(result)){
                mapView.removeAllPOIItems();
                CustomMarkerUtils.createMarkers(mapView, response.body().getStores());
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<MaskSearchResult> call, Throwable t) {
            Log.e("failure", t.getMessage());
        }
    };

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        if(currentMapPoint == null || !currentMapPoint.getMapPointGeoCoord().equals(mapPointGeo)) {
            currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
            if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
                progressBar.setVisibility(View.GONE);
                searchingStoreList(mapPointGeo.latitude, mapPointGeo.longitude);
            }
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        btn_refresh_current_screen.setVisibility(View.VISIBLE);
        btn_current_location.setVisibility(View.INVISIBLE);
        btn_refresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}