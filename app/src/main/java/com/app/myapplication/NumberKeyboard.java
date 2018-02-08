package com.app.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nasrudeen on 8/2/18.
 */

public class NumberKeyboard extends GridLayout {
    private static final int DEFAULT_KEY_WIDTH_DP = 70;
    private static final int DEFAULT_KEY_HEIGHT_DP = 70;
    private static final int DEFAULT_KEY_TEXT_SIZE_SP = 32;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Dimension
    private int keyWidth;
    @Dimension
    private int keyHeight;
    @DrawableRes
    private int numberKeyBackground;
    @Dimension
    private int numberKeyTextSize;
    @ColorRes
    private int numberKeyTextColor;
    @DrawableRes
    private int rightAuxBtnIcon;
    @DrawableRes
    private int rightAuxBtnBackground;
    private List<TextView> numericKeys;

    private TextView splZeroKey;
    private ImageView rightAuxBtn;

    private NumberKeyboardListener listener;

    public NumberKeyboard(Context context) {
        super(context);
    }

    public NumberKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs);
        inflateView();
    }

    public NumberKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(attrs);
        inflateView();
    }


    /**
     * Sets keyboard listener.
     */
    public void setListener(NumberKeyboardListener listener) {
        this.listener = listener;
    }

    /**
     * Sets key width in px.
     */
    public void setKeyWidth(int px) {
        for (TextView key : numericKeys) {
            key.getLayoutParams().width = px;
        }
        splZeroKey.getLayoutParams().width = px;
        rightAuxBtn.getLayoutParams().width = px;
        requestLayout();
    }

    /**
     * Sets key height in px.
     */
    public void setKeyHeight(int px) {
        for (TextView key : numericKeys) {
            key.getLayoutParams().height = px;
        }
        splZeroKey.getLayoutParams().height = px;
        rightAuxBtn.getLayoutParams().height = px;
        requestLayout();
    }

    /**
     * Sets number keys text typeface.
     */
    public void setNumberKeyTypeface(Typeface typeface) {
        for (TextView key : numericKeys) {
            key.setTypeface(typeface);
        }
    }

    /**
     * Initializes XML attributes.
     */
    private void initializeAttributes(AttributeSet attrs) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.NumberKeyboard, 0, 0);
        try {
            // Get keyboard type
            int type = array.getInt(R.styleable.NumberKeyboard_keyboardType, -1);
            if (type == -1) {
                throw new IllegalArgumentException("keyboardType attribute is required.");
            }
            // Get key sizes
            keyWidth = array.getLayoutDimension(R.styleable.NumberKeyboard_keyWidth,
                    dpToPx(DEFAULT_KEY_WIDTH_DP));
            keyHeight = array.getLayoutDimension(R.styleable.NumberKeyboard_keyHeight,
                    dpToPx(DEFAULT_KEY_HEIGHT_DP));
            // Get number key background
            numberKeyBackground = array.getResourceId(R.styleable.NumberKeyboard_numberKeyBackground,
                    R.drawable.key_bg);
            // Get number key text size
            numberKeyTextSize = array.getDimensionPixelSize(R.styleable.NumberKeyboard_numberKeyTextSize,
                    spToPx(DEFAULT_KEY_TEXT_SIZE_SP));
            // Get number key text color
            numberKeyTextColor = array.getResourceId(R.styleable.NumberKeyboard_numberKeyTextColor,
                    R.drawable.key_text_color);
            // Get auxiliary icons
            switch (type) {
                case 0: // integer
                    rightAuxBtnIcon = R.drawable.ic_backspace;
                    rightAuxBtnBackground = R.drawable.key_bg_transparent;
                    break;
                default:
                    rightAuxBtnIcon = R.drawable.key_bg_transparent;
                    rightAuxBtnBackground = R.drawable.key_bg;
            }
        } finally {
            array.recycle();
        }
    }

    /**
     * Inflates layout.
     */

    private void inflateView() {
        View view = inflate(getContext(), R.layout.keyboard, this);
        // Get numeric keys
        numericKeys = new ArrayList<>(10);
        numericKeys.add((TextView) view.findViewById(R.id.key0));
        numericKeys.add((TextView) view.findViewById(R.id.key1));
        numericKeys.add((TextView) view.findViewById(R.id.key2));
        numericKeys.add((TextView) view.findViewById(R.id.key3));
        numericKeys.add((TextView) view.findViewById(R.id.key4));
        numericKeys.add((TextView) view.findViewById(R.id.key5));
        numericKeys.add((TextView) view.findViewById(R.id.key6));
        numericKeys.add((TextView) view.findViewById(R.id.key7));
        numericKeys.add((TextView) view.findViewById(R.id.key8));
        numericKeys.add((TextView) view.findViewById(R.id.key9));
        splZeroKey = view.findViewById(R.id.splZeroKey);
        // Get auxiliary key
        rightAuxBtn = view.findViewById(R.id.rightAuxBtn);
        // Set styles
        setStyles();
        // Set listeners
        setupListeners();
    }

    /**
     * Set styles.
     */
    private void setStyles() {
        setKeyWidth(keyWidth);
        setKeyHeight(keyHeight);

        for (TextView key : numericKeys) {

            // Sets number keys background.
            key.setBackground(ContextCompat.getDrawable(getContext(), numberKeyBackground));

            // Sets number keys text size.
            key.setTextSize(TypedValue.COMPLEX_UNIT_PX, numberKeyTextSize);

            // Sets number keys text color.
            key.setTextColor(ContextCompat.getColorStateList(getContext(), numberKeyTextColor));

        }

        // Sets right auxiliary button background.
        rightAuxBtn.setBackground(ContextCompat.getDrawable(getContext(), rightAuxBtnBackground));

        // Sets right auxiliary button icon.
        rightAuxBtn.setImageResource(rightAuxBtnIcon);

        splZeroKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, numberKeyTextSize);
        splZeroKey.setBackground(ContextCompat.getDrawable(getContext(), numberKeyBackground));


    }

    /**
     * Setup on click listeners.
     */
    private void setupListeners() {
        // Set number callbacks
        for (int i = 0; i < numericKeys.size(); i++) {
            final TextView key = numericKeys.get(i);
            final int number = i;
            key.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onNumberClicked(number);
                    }
                }
            });
        }
        // Set auxiliary key callbacks
        splZeroKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftAuxButtonClicked(00);
                }
            }
        });
        rightAuxBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRightAuxButtonClicked();
                }
            }
        });
    }

    /**
     * Utility method to convert dp to pixels.
     */
    public int dpToPx(float valueInDp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, getResources().getDisplayMetrics());
    }

    /**
     * Utility method to convert sp to pixels.
     */
    public int spToPx(float valueInSp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, valueInSp, getResources().getDisplayMetrics());
    }

}
