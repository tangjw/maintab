package com.zonsim.maintab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zonsim.maintab.R;


/**
 * ^-^
 * Created by tang-jw on 10/24.
 */

public class BottomNavItemView extends FrameLayout implements Checkable, View.OnClickListener {
    
    private boolean mChecked;
    private TextView mTextView;
    private ImageView mImageView;
    private Drawable mDrawableNormal;
    private Drawable mDrawableChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }
    
    public BottomNavItemView(Context context) {
        this(context, null);
    }
    
    public BottomNavItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    
    public BottomNavItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        final float density = getResources().getDisplayMetrics().density;
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            addCompatibilityTopDivider(context);
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            setBackgroundResource(outValue.resourceId);
        }
        
        
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(0,(int) (4f * density + 0.5f),0,0);
        
        mImageView = new ImageView(context);
        
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                (int) (20f * density + 0.5f),
                (int) (20f * density + 0.5f));
        imageViewParams.setMargins(0, 0, 0, (int) (2f * density + 0.5f));
        layout.addView(mImageView, imageViewParams);
        
        mTextView = new TextView(context);
        
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f);
        layout.addView(mTextView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        addView(layout, layoutParams);
        setOnClickListener(this);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavItemView);
        
        CharSequence text = a.getText(R.styleable.BottomNavItemView_android_text);
        mDrawableNormal = a.getDrawable(R.styleable.BottomNavItemView_normal_src);
        mDrawableChecked = a.getDrawable(R.styleable.BottomNavItemView_checked_src);
        boolean checked = a.getBoolean(R.styleable.BottomNavItemView_android_checked, false);
        setChecked(checked);
        a.recycle();
        mTextView.setText(text);
        if (mDrawableNormal != null && !checked) {
            mImageView.setImageDrawable(mDrawableNormal);
        }
        if (!checked) {
            mTextView.setTextColor(ContextCompat.getColor(context, R.color.colorTextGray));
        }
    }
    
    
    private void addCompatibilityTopDivider(Context context) {
        View divider = new View(context);
        divider.setBackgroundColor(Color.parseColor("#E1E1E1"));
        LayoutParams dividerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 1);
        dividerParams.gravity = Gravity.TOP;
        addView(divider, dividerParams);
    }
    
    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            mTextView.setTextColor(ContextCompat.getColor(getContext(), checked ? R.color.colorPrimary : R.color.colorTextGray));
            if (mDrawableChecked != null && mDrawableNormal != null) {
                mImageView.setImageDrawable(checked ? mDrawableChecked : mDrawableNormal);
            }
            
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }
    
    @Override
    public boolean isChecked() {
        return mChecked;
    }
    
    @Override
    public void toggle() {
        if (!mChecked) {
            setChecked(true);
        }
    }
    
    @Override
    public void onClick(View v) {
        toggle();
    }
    
    public interface OnCheckedChangeListener {
        void onCheckedChanged(BottomNavItemView itemView, boolean isChecked);
    }
}
