package com.hwichance.android.WhereIsMyMask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwichance.android.WhereIsMyMask.data.LocationData;
import com.hwichance.android.WhereIsMyMask.data.LocationSearchMeta;
import com.hwichance.android.WhereIsMyMask.data.LocationSearchResult;
import com.hwichance.android.WhereIsMyMask.recyclerview.RecyclerViewAdapter;
import com.hwichance.android.WhereIsMyMask.retrofit.LocationSearchRetrofitAPI;
import com.hwichance.android.WhereIsMyMask.utils.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchAddressActivity extends AppCompatActivity {

    private final static String BASE_URL = "https://dapi.kakao.com";
    private Retrofit retrofit;
    private LocationSearchRetrofitAPI locationSearchRetrofitAPI;
    private Call<LocationSearchResult> locationSearchResultCall;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocationSearchMeta locationSearchMeta;
    private String searchPlace;
    private int pageCount = 1;

    @BindView(R.id.address_edit) EditText address_edit;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        ButterKnife.bind(this);

        retrofit = RetrofitUtils.createRetrofit(BASE_URL);
        locationSearchRetrofitAPI = retrofit.create(LocationSearchRetrofitAPI.class);

        setRecyclerView();

        address_edit.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        address_edit.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    recyclerViewAdapter.initializeData();
                    searchPlace = address_edit.getText().toString();
                    pageCount = 1;
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    searchingAddress(searchPlace, pageCount++);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    private void setRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerViewAdapter.setOnItemClickListener((holder, v, position) -> {
            LocationData item = recyclerViewAdapter.getItem(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedPlace", item);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if(recyclerViewAdapter.getItemCount() != 0 && !locationSearchMeta.isEndPage()) {
                        searchingAddress(searchPlace, pageCount++);
                        if(pageCount == 3){
                            Log.e("isEndPage",locationSearchMeta.isEndPage()?"true":"false");
                        }
                    }
                }
            }
        });
    }

    private void searchingAddress(String place, int page) {
        locationSearchResultCall = locationSearchRetrofitAPI.getLocationList(getString(R.string.kakao_rest_api_key), place, page);
        locationSearchResultCall.enqueue(retrofitCallback);
    }

    private Callback<LocationSearchResult> retrofitCallback = new Callback<LocationSearchResult>() {
        @Override
        public void onResponse(Call<LocationSearchResult> call, Response<LocationSearchResult> response) {
            ArrayList<LocationData> resultLocationDatas = response.body().getDocuments();
            locationSearchMeta = response.body().getLocationSearchMeta();

            recyclerViewAdapter.addItem(resultLocationDatas);
            recyclerViewAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<LocationSearchResult> call, Throwable t) {
            Log.e("failure", t.getMessage());
        }
    };
}
