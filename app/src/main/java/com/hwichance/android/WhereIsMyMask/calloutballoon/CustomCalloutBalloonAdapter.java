package com.hwichance.android.WhereIsMyMask.calloutballoon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwichance.android.WhereIsMyMask.R;
import com.hwichance.android.WhereIsMyMask.data.MaskData;
import com.hwichance.android.WhereIsMyMask.utils.StringParse;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

    private final View calloutBallon;

    @BindView(R.id.mask_imageView) ImageView mask_imageView;
    @BindView(R.id.store_name_textView) TextView store_name_textView;
    @BindView(R.id.mask_remain_textView) TextView mask_remain_textView;
    @BindView(R.id.warehousing_textView) TextView warehousing_textView;
    @BindView(R.id.update_textView) TextView update_textView;

    public CustomCalloutBalloonAdapter(View v) {
        Context context = v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        calloutBallon = inflater.inflate(R.layout.custom_callout_balloon, null);
    }
    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        ButterKnife.bind(this, calloutBallon);
        MaskData maskData = (MaskData) mapPOIItem.getUserObject();

        store_name_textView.setText(maskData.getStoreName());
        switch (maskData.getRemainNumber()) {
            case "plenty":
                mask_imageView.setImageResource(R.drawable.maskfull_60px);
                mask_remain_textView.setText("충분");
                mask_remain_textView.setTextColor(calloutBallon.getContext().getResources().getColor(R.color.colorMaskGreen));

                break;
            case "some":
                mask_imageView.setImageResource(R.drawable.masksome_60px);
                mask_remain_textView.setText("보통");
                mask_remain_textView.setTextColor(calloutBallon.getContext().getResources().getColor(R.color.colorMaskYellow));
                break;
            case "few":
                mask_imageView.setImageResource(R.drawable.maskfew_60px);
                mask_remain_textView.setText("부족");
                mask_remain_textView.setTextColor(calloutBallon.getContext().getResources().getColor(R.color.colorMaskRed));

                break;
            default:
                mask_imageView.setImageResource(R.drawable.maskempty_60px);
                mask_remain_textView.setText("없음");
                mask_remain_textView.setTextColor(calloutBallon.getContext().getResources().getColor(R.color.colorMaskGray));
                break;
        }
        warehousing_textView.setText(StringParse.ParsingDate(maskData.getWarehousingTime()));
        update_textView.setText(StringParse.ParsingDate(maskData.getDataCreatedTime()));

        return calloutBallon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}
