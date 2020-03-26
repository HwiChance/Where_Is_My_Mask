package com.hwichance.android.WhereIsMyMask.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hwichance.android.WhereIsMyMask.R;
import com.hwichance.android.WhereIsMyMask.data.LocationData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<LocationData> locationData = new ArrayList<>();

    public RecyclerViewAdapter() {

    }

    public interface OnItemClickListener {
        void OnItemClick(ViewHolder holder, View v, int position);
    }

    private OnItemClickListener itemClickListener = null;

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.place_name_textView) TextView place_name_textView;
        @BindView(R.id.address_name_textView) TextView address_name_textView;
        @BindView(R.id.road_address_name_textView) TextView road_address_name_textView;
        @BindView(R.id.address_tag_textView) TextView address_tag_textView;
        @BindView(R.id.road_address_tag_textView) TextView road_address_tag_textView;

        OnItemClickListener vItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(vItemClickListener != null) {
                    vItemClickListener.OnItemClick(ViewHolder.this, v, position);
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.vItemClickListener = listener;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.locationlist_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);

        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String address_tag = holder.itemView.getContext().getString(R.string.address_name);
        String road_address_tag = holder.itemView.getContext().getString(R.string.road_address_name);

        holder.place_name_textView.setText(locationData.get(position).getPlace_name());
        holder.address_name_textView.setText(locationData.get(position).getAddress_name());
        holder.road_address_name_textView.setText(locationData.get(position).getRoad_address_name());
        holder.address_tag_textView.setText(address_tag);
        holder.road_address_tag_textView.setText(road_address_tag);

        holder.setOnItemClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return locationData.size();
    }

    public LocationData getItem(int position) {
        return locationData.get(position);
    }

    public void addItem(ArrayList<LocationData> data){
        for(LocationData x : data) {
            locationData.add(x);
        }
    }

    public void initializeData() {
        locationData.clear();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
