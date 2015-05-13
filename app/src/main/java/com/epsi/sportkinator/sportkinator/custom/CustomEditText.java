package com.epsi.sportkinator.sportkinator.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by rbertal on 13/05/2015.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
public class CustomEditText extends EditText{

    public CustomEditText(Context context) {
        super(context);
        setFont();
    }
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "world_cup.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
