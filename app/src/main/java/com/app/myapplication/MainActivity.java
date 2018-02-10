package com.app.myapplication;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements NumberKeyboardListener*/ {
    private View contentLayout;
    private List<TextView> numericKeys;
    private TextView textContent;
    private TextView textSplZero;
    private TextView btnAdd;
    private TextView btnXTimes;

    private ImageView btnBackSpc;
    private ImageView btnReset;
    private ImageView btn;
    private int amount;
    private NumberFormat nf = NumberFormat.getInstance();
    private int MAX_ALLOWED_AMOUNT = 99999999;
    private boolean isMultiply = false;
    private NumberKeyboardListener listener;
    private int itemCode = 0;
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Keyboard integer");
        initView();
        setupListeners();
        Log.e("ScreenWidth", "" + getScreenWidth());
        Log.e("Content Width", "" + contentLayout.getWidth());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void initView() {
        contentLayout = findViewById(R.id.contentLayout);
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
    }

    /**
     * Setup on click listeners.
     */
    private void setupListeners() {
        // Set number callbacks
        for (int i = 0; i < numericKeys.size(); i++) {
            final TextView key = numericKeys.get(i);
            final int number = i;
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNumberClicked(number);
                }
            });
        }
        // Set auxiliary key callbacks
        textSplZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpecialZeroClicked();
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

    private void onMultiply() {
        if (!isMultiply) {
            isMultiply = true;
        }
        textContent.setText(textContent.getText() + "X");
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
            int newAmount = (int) (quantity * 10.0 + number);
            if (newAmount <= MAX_ALLOWED_AMOUNT) {
                quantity = newAmount;
                showQuantity();
            }
        } else {
            int newAmount = (int) (itemCode * 10.0 + number);
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

    public void onSpecialZeroClicked() {
        if (isMultiply) {
            int newAmount = (int) (quantity * 10.0 + 00);
            if (newAmount <= MAX_ALLOWED_AMOUNT) {
                quantity = newAmount;
                showQuantity();
            }
        } else {
            int newAmount = (int) (itemCode * 10.0 + 00);
            if (newAmount <= MAX_ALLOWED_AMOUNT) {
                itemCode = newAmount;
                showItem();
            }
        }
    }

    public void onRightAuxButtonClicked() {
        if (isMultiply) {
            if (quantity == 0) {
                isMultiply = false;
                itemCode = (int) (itemCode / 10.0);
                showItem();
            } else {
                quantity = (int) (quantity / 10.0);
                showQuantity();
            }
        } else {
            itemCode = (int) (itemCode / 10.0);
            showItem();
        }
    }
}
