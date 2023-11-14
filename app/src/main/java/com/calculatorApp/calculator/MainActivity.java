package com.calculatorApp.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.calculator.R;

public class MainActivity extends AppCompatActivity {

    private CalculatorModel calculator;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] actionsIds = new int[]{
                R.id.equals,
                R.id.btn_clear,
                R.id.btn_clear
        };

        calculator = new CalculatorModel();

        text = findViewById(R.id.text);

        View.OnClickListener symbolsButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.onSymbolPressed(view.getId());
                text.setText(calculator.getText());
            }
        };

        View.OnClickListener actionButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.onActionPressed(view.getId());
                text.setText(calculator.getText());
            }
        };


        for (int id: calculator.getSymbolMap().keySet()) {
            findViewById(id).setOnClickListener(symbolsButtonClickListener);
        }

        for (int actionsId : actionsIds) {
            findViewById(actionsId).setOnClickListener(actionButtonClickListener);
        }

    }
}