package com.example.game2048;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity
{
    private GameManager gameManager;
    private static final String APP_NAME = "2048";
    private static final String GAME_STATE_KEY = "gameState";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        gameManager = (GameManager) findViewById(R.id.gameManager);

        SharedPreferences prefs = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        String savedState = prefs.getString(GAME_STATE_KEY, "");

        if (!savedState.isEmpty())
        {
            gameManager.getTileManager().deserializeGameState(savedState);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String currentState = gameManager.getTileManager().serializeGameState();
        editor.putString(GAME_STATE_KEY, currentState);
        editor.apply();
    }
}