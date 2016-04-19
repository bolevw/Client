package com.example.administrator.client.wedgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.administrator.client.R;

/**
 * Created by Administrator on 2016/4/19.
 */
public class FixLinearLayout extends LinearLayout {

    private float radio;
    private boolean baseOnWith;


    public FixLinearLayout(Context context) {
        super(context);
    }

    public FixLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FixLinearLayout);

        radio = ta.getFloat(R.styleable.FixLinearLayout_ratio, 1);
        baseOnWith = ta.getBoolean(R.styleable.FixLinearLayout_baseOnWith, false);
        ta.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (baseOnWith) {
            int size = (int) (MeasureSpec.getSize(heightMeasureSpec) * radio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        } else {
            int size = (int) (MeasureSpec.getSize(widthMeasureSpec) * radio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public FixLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
