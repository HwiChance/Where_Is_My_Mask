package com.hwichance.android.WhereIsMyMask.retrofit;

import com.hwichance.android.WhereIsMyMask.data.LocationSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LocationSearchRetrofitAPI {
    @GET("v2/local/search/keyword.json")
    Call<LocationSearchResult> getLocationList(@Header ("Authorization") String AppKey, @Query("query") String place, @Query("page") int page);
}
