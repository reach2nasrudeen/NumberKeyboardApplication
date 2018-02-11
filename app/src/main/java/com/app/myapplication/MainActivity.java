package com.app.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity /*implements NumberKeyboardListener*/ {
    private List<TextView> numericKeys;
    private TextView textContent;
    private TextView textSplZero;
    private TextView btnAdd;
    private TextView btnXTimes;

    private ImageView btnBackSpc;
    private ImageView btnReset;
    private int MAX_ALLOWED_AMOUNT = 99999999;
    private boolean isMultiply;
    private int itemCode;
    private int quantity;
    private double singleMultiplier = 10.0;
    private double doubleMultiplier = 100.0;
    private double multiplier = 10.0;

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setTitle("Keyboard integer");
        initView();
        setupListeners();
        onReset();
    }

    public static int convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void initView() {
        numericKeys = new ArrayList<>(10);
        numericKeys.add((TextView) findViewById(R.id.key0));
        numericKeys.add((TextView) findViewById(R.id.key1));
        numericKeys.add((TextView) findViewById(R.id.key2));
        numericKeys.add((TextView) findViewById(R.id.key3));
        numericKeys.add((TextView) findViewById(R.id.key4));
        numericKeys.add((TextView) findViewById(R.id.key5));
        numericKeys.add((TextView) findViewById(R.id.key6));
        numericKeys.add((TextView) findViewById(R.id.key7));
        numericKeys.add((TextView) findViewById(R.id.key8));
        numericKeys.add((TextView) findViewById(R.id.key9));
        textSplZero = findViewById(R.id.keySpl0);
        btnBackSpc = findViewById(R.id.keyBack);
        textContent = findViewById(R.id.textContent);
        btnAdd = findViewById(R.id.btnAdd);
        btnXTimes = findViewById(R.id.keyMultiply);
        btnReset = findViewById(R.id.btnReset);

        setupKeyWidth();
    }

    private void setupKeyWidth() {
        int px = convertPixelsToDp(this, getScreenWidth() / 4);
        for (TextView key : numericKeys) {
            key.getLayoutParams().width = px;
        }
        btnXTimes.getLayoutParams().width = px;
        btnReset.getLayoutParams().width = px;
        btnAdd.getLayoutParams().width = px;
        textSplZero.getLayoutParams().width = px;
        btnBackSpc.getLayoutParams().width = px;
        btnBackSpc.requestLayout();
    }

    /**
     * Setup on click listeners.
     */
    private void setupListeners() {
        // Set number callbacks
        for (int i = 0; i < numericKeys.size(); i++) {
            final TextView key = numericKeys.get(i);
            final int number = i;
            key.getLayoutParams().width = convertPixelsToDp(this, getScreenWidth() / 4);
            key.requestLayout();
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMultiplier(singleMultiplier);
                    onNumberClicked(number);
                }
            });
        }
        // Set auxiliary key callbacks
        textSplZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMultiplier(doubleMultiplier);
                onNumberClicked(0);
            }
        });
        btnBackSpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightAuxButtonClicked();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });
        btnXTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMultiply();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReset();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void onMultiply() {
        if (!isMultiply) {
            isMultiply = true;
            textContent.setText(textContent.getText() + "X");
        }
    }

    private void onAdd() {
        onReset();
        Toast.makeText(MainActivity.this, "Item will add to cart", Toast.LENGTH_SHORT).show();
    }

    private void onReset() {
        itemCode = 0;
        quantity = 0;
        textContent.setText("0");
        isMultiply = false;
    }

    private void onNumberClicked(int number) {
        if (isMultiply) {
            int newAmount = (int) (quantity * getMultiplier() + number);
            if (newAmount <= MAX_ALLOWED_AMOUNT) {
                quantity = newAmount;
                showQuantity();
            }
        } else {
            int newAmount = (int) (itemCode * getMultiplier() + number);
            if (newAmount <= MAX_ALLOWED_AMOUNT) {
                itemCode = newAmount;
                showItem();
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void showItem() {
        textContent.setText(String.format("%d", itemCode));
    }

    @SuppressLint("DefaultLocale")
    private void showQuantity() {
        textContent.setText(String.format("%d X %d", itemCode, quantity));
    }


    public void onRightAuxButtonClicked() {
        if (isMultiply) {
            if (quantity == 0) {
                isMultiply = false;
                itemCode = (int) (itemCode / singleMultiplier);
                showItem();
            } else {
                quantity = (int) (quantity / singleMultiplier);
                showQuantity();
            }
        } else {
            itemCode = (int) (itemCode / singleMultiplier);
            showItem();
        }
    }
}
