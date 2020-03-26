package com.hwichance.android.WhereIsMyMask.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwichance.android.WhereIsMyMask.R;
import com.hwichance.android.WhereIsMyMask.utils.DayOfWeekCalculator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseInfoFragment extends Fragment {

    @BindView(R.id.dayTextView) TextView dayTextView;
    @BindView(R.id.yearTextView) TextView yearTextView;

    public PurchaseInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_info, container, false);
        ButterKnife.bind(this, view);
        setTextView();
        return view;
    }

    private void setTextView() {
        String dayOfWeek = DayOfWeekCalculator.getDayOfWeek();
        dayTextView.setText(dayOfWeek);
        yearTextView.setText(DayOfWeekCalculator.getPurchaseInfo(dayOfWeek));
    }
}
