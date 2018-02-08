package com.app.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements NumberKeyboardListener {

    private TextView textContent;
    private int amount;
    private NumberFormat nf = NumberFormat.getInstance();
    private int MAX_ALLOWED_AMOUNT = 99999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Keyboard integer");
        textContent = findViewById(R.id.textContent);
        NumberKeyboard numberKeyboard = findViewById(R.id.keyboard);
        numberKeyboard.setListener(this);
    }

    @Override
    public void onNumberClicked(int number) {
        int newAmount = (int) (amount * 10.0 + number);
        if (newAmount <= MAX_ALLOWED_AMOUNT) {
            amount = newAmount;
            showAmount();
        }
    }

    @Override
    public void onLeftAuxButtonClicked(int number) {
        int newAmount = (int) (amount * 100.0 + number);
        if (newAmount <= MAX_ALLOWED_AMOUNT) {
            amount = newAmount;
            showAmount();
        }
    }

    @Override
    public void onRightAuxButtonClicked() {
        amount = (int) (amount / 10.0);
        showAmount();
    }

    private void showAmount() {
        textContent.setText(nf.format(amount));
    }
}
