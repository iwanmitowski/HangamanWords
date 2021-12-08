package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity{

    TextView hint;
    TextView tvCurrentWord;
    EditText userGuess;
    Button guess;
    TextView tvLives;

    String[] words1 = {
        "horse", "bread", "apple", "food", "doost",
        "cross", "chair", "zebra", "dance", "drink",
        "glass", "grass", "group", "heart", "knife",
        "knight", "money", "model", "nurse", "novel"
    };

    String[] words2 = {
            "abroad", "accept", "favour", "floor", "flower",
            "regard ", "content", "creator", "player", "receive",
            "football", "basketball", "volleyball", "unusual", "beautiful",
            "group", "better", "words", "castle", "cannon"
    };

    Random rnd;

    int difficulty = 0;
    int lives = 0;

    String currentWord;
    String placeHolder;
    String currentGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rnd = new Random();

        hint = findViewById(R.id.tvHint);
        tvCurrentWord = findViewById(R.id.tvCurrentWord);
        userGuess = findViewById(R.id.etUserGuess);
        guess = findViewById(R.id.btnGuess);

        tvLives = findViewById(R.id.tvLives);

        generateWord();
        placeHolder = new String(new char[currentWord.length()]).replace('\0', '*');
        tvCurrentWord.setText(placeHolder);
        hint.setText(currentWord);

        guess.setOnClickListener(guessListener);

    }

    private void playGame() {

        while(lives > 0){
            currentGuess = userGuess.getText().toString();

            if (currentGuess.length() < 1 ||
                    !currentWord.contains(currentGuess)){
                userGuess.setText("");
                lives--;
                tvLives.setText(lives + "");
            }
            else if(currentGuess.length() > 1){
                userGuess.setText("");
                Toast.makeText(getApplicationContext(), "Please, enter your only 1 letter!", Toast.LENGTH_SHORT).show();
                continue;
            }

            tvLives.setText(lives + "");

            makeAGuess();
        }

        Toast.makeText(getApplicationContext(), "YOU ARE HANGED", Toast.LENGTH_SHORT).show();
    }

    private void generateWord() {
        
        int upperBoundary = words1.length;
        
        if (difficulty % 2 != 0){
            upperBoundary = words2.length;
            currentWord = words2[rnd.nextInt(upperBoundary)];
        }
        else{
            currentWord = words1[rnd.nextInt(upperBoundary)];
        }

        lives = currentWord.length();
        tvLives.setText(lives + "");
    }

    private void makeAGuess(){
        String currentGuess = userGuess.getText().toString();

        if (currentGuess.length() < 1 ||
                !currentWord.contains(currentGuess)){
            userGuess.setText("");
            lives--;
            tvLives.setText(lives + "");
        }
        else if(currentGuess.length() > 1){
            userGuess.setText("");
            Toast.makeText(getApplicationContext(), "Please, enter your only 1 letter!", Toast.LENGTH_SHORT).show();
        }

        showGuessedLetters();
    }

    private void showGuessedLetters() {

        StringBuilder newWord = new StringBuilder();

        char currentLetter = currentGuess.charAt(0);

        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == currentLetter){
                newWord.append(currentLetter);
            }
            else{
                newWord.append("*");
            }
        }

        tvCurrentWord.setText(newWord.toString());

        tvLives.setText(lives);

        if (currentWord.contains("*")){
            //Write to db
            difficulty++;
            generateWord();
        }
    }

    View.OnClickListener guessListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playGame();
        }
    };

}