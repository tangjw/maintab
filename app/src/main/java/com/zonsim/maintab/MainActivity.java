package com.zonsim.maintab;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.zonsim.maintab.dummy.DummyContent;
import com.zonsim.maintab.dummy.ItemFragment;
import com.zonsim.maintab.widget.BottomNavLayout;


public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    
    private static final String TAG = "MainActivity";
    
    private BottomNavLayout mBottomNavLayout;
    private FragmentManager mFragmentManager;
    private ItemFragment mItemFragment1;
    
    private int mCurrentTab;
    private ItemFragment mItemFragment2;
    private ItemFragment mItemFragment3;
    private static final int DEFAULT_TAB = R.id.bottom_nav_item_1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFragmentManager = getSupportFragmentManager();
        
        mBottomNavLayout = (BottomNavLayout) findViewById(R.id.bottom_nav_layout);
        
        if (savedInstanceState == null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            mItemFragment1 = ItemFragment.newInstance(1);
            mItemFragment2 = ItemFragment.newInstance(2);
            mItemFragment3 = ItemFragment.newInstance(3);
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            mItemFragment1.setArguments(getIntent().getExtras());
            //get->begi-add->com
            ft.add(R.id.fl_main_container, mItemFragment1, "main1");
            ft.add(R.id.fl_main_container, mItemFragment2, "main2");
            ft.add(R.id.fl_main_container, mItemFragment3, "main3");
            ft.commit();
            mCurrentTab = DEFAULT_TAB;
            
        } else {
            mItemFragment1 = (ItemFragment) getSupportFragmentManager().findFragmentByTag("main1");
            mItemFragment2 = (ItemFragment) getSupportFragmentManager().findFragmentByTag("main2");
            mItemFragment3 = (ItemFragment) getSupportFragmentManager().findFragmentByTag("main3");
            mCurrentTab = savedInstanceState.getInt("currentMainTab");
        }
        
        mBottomNavLayout.check(mCurrentTab);
        switchMainTab(mCurrentTab);
        
        mBottomNavLayout.setOnCheckedChangeListener(new BottomNavLayout.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(BottomNavLayout group, int checkedId) {
                mCurrentTab = checkedId;
                switchMainTab(mCurrentTab);
            }
        });
        
        
    }
    
    private void switchMainTab(int checkedId) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        switch (checkedId) {
            case R.id.bottom_nav_item_1:
                ft.show(mItemFragment1);
                ft.hide(mItemFragment2);
                ft.hide(mItemFragment3);
                break;
            case R.id.bottom_nav_item_2:
                ft.show(mItemFragment2);
                ft.hide(mItemFragment1);
                ft.hide(mItemFragment3);
                break;
            case R.id.bottom_nav_item_3:
                ft.show(mItemFragment3);
                ft.hide(mItemFragment2);
                ft.hide(mItemFragment1);
                break;
        }
        
        ft.commit();
    }
    
    public void click(View view) {
        
    }
    
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentMainTab", mCurrentTab);
        super.onSaveInstanceState(outState);
    }
    
}
