package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
            "inconsistent", "improvement", "extraordinary", "dictionary", "flower",
            "regardless ", "content", "creatoional", "contemporaneous", "receive",
            "football", "basketball", "volleyball", "unusual", "beautiful",
            "concurrent", "amazing", "words", "synchronous", "meaningful"
    };

    Random rnd;

    int difficulty = 0;
    int lives = 0;

    String currentWord;
    String placeHolder;
    String currentGuess;
    ArrayList<Character> alreadyGuessedLetters;
    boolean isGuessed = false;
    StringBuilder incorrectGuessedLetters;
    User currentUser;

    //tv with current winstreak
    //after hung show gif and retry button
    //write in db if current streak > the streak in db after hung

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentUser = getIntent().getParcelableExtra("user");

        alreadyGuessedLetters = new ArrayList<>();
        rnd = new Random();

        hint = findViewById(R.id.tvHint);
        tvCurrentWord = findViewById(R.id.tvCurrentWord);
        userGuess = findViewById(R.id.etUserGuess);
        guess = findViewById(R.id.btnGuess);
        tvLives = findViewById(R.id.tvLives);
        incorrectGuessedLetters = new StringBuilder();

        generateWord();

        guess.setOnClickListener(guessListener);

    }

    private void playGame() {


        while(lives > 0 && isGuessed == false){
            currentGuess = userGuess.getText().toString();

            if (currentGuess.length() == 0){
                return;
            }

            tvLives.setText(lives + "");

            if (!currentWord.contains(currentGuess.charAt(0) + "")){
                userGuess.setText("");


                char currentLetter = currentGuess.charAt(0);
                if (!alreadyGuessedLetters.contains(currentLetter))
                {
                    alreadyGuessedLetters.add(currentLetter);
                    incorrectGuessedLetters.append(alreadyGuessedLetters.get(alreadyGuessedLetters.size() - 1) + "");
                    lives--;
                }

                hint.setText(incorrectGuessedLetters.toString());

                tvLives.setText(lives + "");
                continue;
            }

            makeAGuess();
        }

        if (isGuessed){
            isGuessed = false;
        }
        if(lives <= 0){
            Toast.makeText(getApplicationContext(), "YOU ARE HANGED", Toast.LENGTH_SHORT).show();
        }
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

        alreadyGuessedLetters.clear();
        incorrectGuessedLetters = new StringBuilder();
        lives = currentWord.length();
        tvLives.setText(lives + "");
        placeHolder = new String(new char[currentWord.length()]).replace('\0', '*');
        tvCurrentWord.setText(placeHolder);
        hint.setText(currentWord);
    }

    private void makeAGuess(){
        currentGuess = userGuess.getText().toString();

        if(currentGuess.length() != 1){
            userGuess.setText("");
            Toast.makeText(getApplicationContext(), "Please, enter your only 1 letter!", Toast.LENGTH_SHORT).show();
            return;
        }

        showGuessedLetters();
        userGuess.setText("");
    }

    private void showGuessedLetters() {

        StringBuilder newWord = new StringBuilder();

        char currentLetter = currentGuess.charAt(0);
        alreadyGuessedLetters.add(currentLetter);

        for (int i = 0; i < currentWord.length(); i++) {
            char currentChar = currentWord.charAt(i);

            if (alreadyGuessedLetters.contains(currentChar)){
                newWord.append(currentChar);
            }
            else{
                newWord.append("*");
            }
        }

        tvCurrentWord.setText(newWord.toString());

        tvLives.setText(lives + "");

        if (!newWord.toString().contains("*")){
            //Write to db
            difficulty++;
            generateWord();
            isGuessed = true;
        }
    }

    View.OnClickListener guessListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                playGame();

        }
    };

}