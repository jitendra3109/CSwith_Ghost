package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private Button bChallege, bRestart;
    private TextView input, gamestatus,userScore,computerScore;
    private int scoreUSer=0,scoreComputer=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
            //dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        bRestart = (Button) findViewById(R.id.restart);
        bChallege = (Button) findViewById(R.id.challege);
        input = (TextView) findViewById(R.id.ghostText);
        gamestatus = (TextView) findViewById(R.id.gameStatus);
        userScore = (TextView) findViewById(R.id.textView);
        computerScore = (TextView) findViewById(R.id.textView2);


        onStart(null);
   // challenge button
       bChallege.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               challegehandler();

           }
       });

      bRestart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onStart(null);

          }
      });
    }


    private void challegehandler(){

        String word=input.getText().toString();
        String nextWord;
        if(word.length()>=4 && dictionary.isWord(word)){
            endGame(true);
        }else{
            nextWord=dictionary.getAnyWordStartingWith(word);
            if (nextWord!=null){
                endGame(false);
                (Toast.makeText(this,"Word Exists"+nextWord,Toast.LENGTH_LONG)).show();
            }else{
                endGame(true);
            }
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onSaveInstanceState(Bundle outstate){
        outstate.putString("WORD",input.getText().toString());
        outstate.putString("LEBEL",gamestatus.getText().toString());
        outstate.putInt("SCORE_USER",scoreUSer);
        outstate.putInt("SCORE_COMPUTER",scoreComputer);
        super.onSaveInstanceState(outstate);
      }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState!=null){
            input.setText(savedInstanceState.getString("WORD"));
            gamestatus.setText(savedInstanceState.getString("LEBEL"));
            userTurn=true;
            scoreComputer=savedInstanceState.getInt("SCORE_COMPUTER");
            scoreUSer=savedInstanceState.getInt("SCORE_USER");

            updateScoreBoard();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");

        if (userTurn) {
            gamestatus.setText(USER_TURN);
        } else {
            gamestatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {

        // Do computer turn stuff then make it the user's turn again
       String text=input.getText().toString();
        String nextWord;
        if (text.length()>=4 && dictionary.isWord(text)){
            endGame(false);
            return;
        }else{
            nextWord=dictionary.getAnyWordStartingWith(text);
            if (nextWord==null){
                endGame(false);
                return;
            }else {
                addTextToGame(nextWord.charAt(text.length()));
            }
        }

        userTurn = true;
        gamestatus.setText(USER_TURN);
    }
    private void endGame(boolean win){

        if (win){
            input.setText("User Win");
            scoreUSer+=1;
        }
        else{
            input.setText("Computer Win");
            scoreComputer+=1;
        }
       updateScoreBoard();
        gamestatus.setText("");
        userTurn=true;
    }
    private void addTextToGame(char c){
        input.setText(input.getText().toString()+c);
    }


    private void updateScoreBoard(){
        userScore.setText("Your: "+scoreUSer+"");
        computerScore.setText("Computer: "+scoreComputer+"");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char c = (char) event.getUnicodeChar();
      if ((c>='a'&& c<='z') || (c>='A' && c<='Z')){
          addTextToGame(c);
          input.setText(input.getText().toString()+c);
          gamestatus.setText(COMPUTER_TURN);
          userTurn=false;
          computerTurn();
      }
        else {
          (Toast.makeText(this,"Please enter  a valid character",Toast.LENGTH_SHORT)).show();
      }
        return super.onKeyUp(keyCode,event);

    }

}
