package com.example.ad_task4_quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button submitButton;
    private TextView scoreTextView;

    private List<question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.question);
        optionsRadioGroup = findViewById(R.id.options);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitButton = findViewById(R.id.submit_button);
        scoreTextView = findViewById(R.id.score);

        questionList = new ArrayList<>();
        questionList.add(new question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        questionList.add(new question("Who is known as the father of computers?", new String[]{"Charles Babbage", "Alan Turing", "Thomas Edison", "Albert Einstein"}, 0));
        questionList.add(new question("What is the largest planet in our solar system?", new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 2));
        questionList.add(new question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        questionList.add(new question("Which element has the chemical symbol 'O'?", new String[]{"Oxygen", "Gold", "Osmium", "Zinc"}, 0));
        questionList.add(new question("Who wrote 'To Kill a Mockingbird'?", new String[]{"Harper Lee", "J.K. Rowling", "Mark Twain", "Ernest Hemingway"}, 0));
        questionList.add(new question("What is the largest ocean on Earth?", new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"}, 3));
        questionList.add(new question("What year did the first man land on the moon?", new String[]{"1969", "1975", "1981", "1961"}, 0));
        questionList.add(new question("Which planet is known as the Red Planet?", new String[]{"Earth", "Venus", "Mars", "Jupiter"}, 2));

        loadQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            question currentQuestion = questionList.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestion());
            option1.setText(currentQuestion.getOptions()[0]);
            option2.setText(currentQuestion.getOptions()[1]);
            option3.setText(currentQuestion.getOptions()[2]);
            option4.setText(currentQuestion.getOptions()[3]);
            optionsRadioGroup.clearCheck();
        } else {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("TOTAL_QUESTIONS", questionList.size());
            startActivity(intent);
            finish();
        }
    }

    private void checkAnswer() {
        int selectedOption = optionsRadioGroup.indexOfChild(findViewById(optionsRadioGroup.getCheckedRadioButtonId()));
        if (selectedOption == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedOption == questionList.get(currentQuestionIndex).getCorrectAnswer()) {
            score++;
        }

        currentQuestionIndex++;
        scoreTextView.setText("Score: " + score);
        loadQuestion();
    }
}
