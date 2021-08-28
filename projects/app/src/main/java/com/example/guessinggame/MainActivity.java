package com.example.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private Button btnPlayAgain;
    private int theNumber;
    private int range = 100;
    private TextView lblRange;
    private int maxTries = 7;
    private int tries = 0;
    private TextView lblAttempts;

    public void checkGuess() {
        tries++;
        lblAttempts.setText("Attempts:  " + tries + " / " + maxTries);
        String guessText = txtGuess.getText().toString();
        String message = "";
        int guess;
        try {
            guess = Integer.parseInt(guessText);

            if (tries == maxTries && guess != theNumber) {
                message = "You lose! Correct number is " + theNumber + ". Do you want to play again? ";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesLost = preferences.getInt("gamesLost", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesLost", gamesLost);
                editor.apply();
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnGuess.setVisibility(View.INVISIBLE);
            }
             else {
                    if (guess < theNumber)
                        message = guess + " is too low. Try again.";
                    else if (guess > theNumber)
                        message = guess + " is too high. Try again.";
                    else {
                        message = guess + " is correct. You win after " + tries + " tries! Do you want to play again?";
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                        int gamesWon = preferences.getInt("gamesWon", 0) + 1;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("gamesWon", gamesWon);
                        editor.apply();
                        btnPlayAgain.setVisibility(View.VISIBLE);
                        btnGuess.setVisibility(View.INVISIBLE);
                    }

            }
        }
        catch (Exception e) {
            message = "Enter a whole number between 1 and " + range + " .";
        }
            finally {
                lblOutput.setText(message);
                txtGuess.requestFocus();
                txtGuess.selectAll();
            }
        }


    public void newGame() {
        tries = 0;
        lblAttempts.setText("Attempts:  " + tries + " / " + maxTries);
        theNumber = (int) (Math.random() * range + 1);
        lblRange.setText("Enter a number between 1 and " + range + " .");
        lblOutput.setText("Enter a number above and click Guess!");
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnGuess.setVisibility(View.VISIBLE);
        txtGuess.requestFocus();
        txtGuess.selectAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.textView2);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        maxTries = preferences.getInt("maxTries",7);
        lblAttempts = (TextView) findViewById((R.id.lblAttempts));
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setVisibility(View.INVISIBLE);
        newGame();
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkGuess();
                return true;
            }
        });
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
                txtGuess.requestFocus();
                txtGuess.selectAll();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.action_settings:
                final CharSequence[] items = {"1 to 10", "1 to 100", "1 to 1000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the Range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                range = 10;
                                maxTries = 4;
                                storeRangeAndMaxtries(10,4);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                maxTries = 7;
                                storeRangeAndMaxtries(100, 7);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                maxTries = 10;
                                storeRangeAndMaxtries(1000, 10);
                                newGame();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                int gamesLost = preferences.getInt("gamesLost", 0);
                int percent;
                try {
                    percent = gamesWon * 100 / (gamesWon + gamesLost);
                }
                catch (Exception e) {
                    percent = 0;
                }
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats");
                statDialog.setMessage("You have won " + gamesWon + " out of " + (gamesWon + gamesLost) + " games! Your winrate: " + percent + "%!");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                statDialog.show();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c)2021 Krugman Artem.");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void storeRangeAndMaxtries(int newRange, int maxTries) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.putInt("maxTries", maxTries);
        editor.apply();
    }
}