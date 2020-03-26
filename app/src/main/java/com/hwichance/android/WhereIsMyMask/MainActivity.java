package com.hwichance.android.WhereIsMyMask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hwichance.android.WhereIsMyMask.fragments.FindStoreFragment;
import com.hwichance.android.WhereIsMyMask.fragments.PurchaseInfoFragment;
import com.hwichance.android.WhereIsMyMask.viewpager.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private PurchaseInfoFragment purchaseInfoFragment;
    private FindStoreFragment findStoreFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private MenuItem preMenuItem;

    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(preMenuItem != null) {
                    preMenuItem.setChecked(false);
                }
                else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
                preMenuItem = mBottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dayTab:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.drugstoreTab:
                    mViewPager.setCurrentItem(1);
                    break;
            }
            return false;
        });

        setViewPager();
    }

    private void setViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        purchaseInfoFragment = new PurchaseInfoFragment();
        findStoreFragment = new FindStoreFragment();

        viewPagerAdapter.addFragments(purchaseInfoFragment);
        viewPagerAdapter.addFragments(findStoreFragment);

        mViewPager.setAdapter(viewPagerAdapter);
    }


}
