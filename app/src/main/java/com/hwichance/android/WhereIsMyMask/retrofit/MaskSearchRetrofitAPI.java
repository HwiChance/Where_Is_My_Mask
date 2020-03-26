package com.hwichance.android.WhereIsMyMask.retrofit;

import com.hwichance.android.WhereIsMyMask.data.MaskSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MaskSearchRetrofitAPI {

    @GET("corona19-masks/v1/storesByGeo/json")
    Call<MaskSearchResult> getStoreList(@Query("lat") double latitude, @Query("lng") double longitude, @Query("m") int distance);

}
