package com.example.game2048;

public interface GameManagerCallback
{
    void gameOver();
    void updateScore(int delta);
    void reached2048();
}
