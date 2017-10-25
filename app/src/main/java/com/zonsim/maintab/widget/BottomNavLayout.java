package com.zonsim.maintab.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * ^-^
 * Created by tang-jw on 10/24.
 */

public class BottomNavLayout extends LinearLayout {
    
    private static final String TAG = "BottomNavLayout";
    
    private int mCheckedId = -1;
    
    private boolean mProtectFromCheckedChange = false;
    
    private PassThroughHierarchyChangeListener mPassThroughListener;
    
    private CheckedStateTracker mChildOnCheckedChangeListener;
    
    public BottomNavLayout(Context context) {
        this(context, null);
    }
    
    public BottomNavLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }
    
    public BottomNavLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(10f);
        }
    }
    
    
    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }
    
    
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }
    
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BottomNavItemView) {
            final BottomNavItemView itemView = (BottomNavItemView) child;
            if (itemView.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(itemView.getId());
            }
        }
        
        super.addView(child, index, params);
    }
    
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }
        
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
        
        if (id != -1) {
            setCheckedStateForView(id, true);
        }
        
        setCheckedId(id);
    }
    
    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }
    
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof BottomNavItemView) {
            ((BottomNavItemView) checkedView).setChecked(checked);
        }
    }
    
    @Override
    public CharSequence getAccessibilityClassName() {
        return BottomNavLayout.class.getName();
    }
    
    private class CheckedStateTracker implements BottomNavItemView.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(BottomNavItemView buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            
            int id = buttonView.getId();
            setCheckedId(id);
        }
    }
    
    
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;
        
        @SuppressLint("NewApi")
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == BottomNavLayout.this && child instanceof BottomNavItemView) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((BottomNavItemView) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            }
            
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }
        
        
        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == BottomNavLayout.this && child instanceof BottomNavItemView) {
                ((BottomNavItemView) child).setOnCheckedChangeListener(null);
            }
            
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
    
    private OnCheckedChangeListener mOnCheckedChangeListener;
    
    public interface OnCheckedChangeListener {
        void onCheckedChanged(BottomNavLayout group, @IdRes int checkedId);
    }
    
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }
    
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
}
