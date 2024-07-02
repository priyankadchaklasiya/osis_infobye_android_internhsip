package com.example.ad_task3_calcutor_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private StringBuilder input = new StringBuilder();
    private Double operand1 = null;
    private String operator = null;
    private boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result);

        setNumberButtonListeners();
        setOperatorButtonListeners();
        setEqualsButtonListener();
        setClearButtonListener();
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonDot
        };

        View.OnClickListener numberButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (isOperatorPressed) {
                    input.setLength(0);
                    isOperatorPressed = false;
                }
                input.append(button.getText().toString());
                resultTextView.setText(input.toString());
            }
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(numberButtonClickListener);
        }
    }

    private void setOperatorButtonListeners() {
        int[] operatorButtonIds = {
                R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide
        };

        View.OnClickListener operatorButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                operator = button.getText().toString();
                operand1 = Double.parseDouble(resultTextView.getText().toString());
                isOperatorPressed = true;
            }
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(operatorButtonClickListener);
        }
    }

    private void setEqualsButtonListener() {
        findViewById(R.id.buttonEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double operand2 = Double.parseDouble(resultTextView.getText().toString());
                Double result = 0.0;

                if (operator != null && operand1 != null) {
                    switch (operator) {
                        case "+":
                            result = operand1 + operand2;
                            break;
                        case "-":
                            result = operand1 - operand2;
                            break;
                        case "*":
                            result = operand1 * operand2;
                            break;
                        case "/":
                            if (operand2 != 0) {
                                result = operand1 / operand2;
                            } else {
                                resultTextView.setText("Error");
                                return;
                            }
                            break;
                    }
                    resultTextView.setText(new DecimalFormat("#.##").format(result));
                    operand1 = result;
                    isOperatorPressed = true;
                }
            }
        });
    }

    private void setClearButtonListener() {
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setLength(0);
                operand1 = null;
                operator = null;
                resultTextView.setText("0");
                isOperatorPressed = false;
            }
        });
    }
}
