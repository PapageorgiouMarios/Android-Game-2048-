package com.example.game2048;

public interface SwipeCallback
{
    void onSwipe(Direction direction);

    enum Direction
    {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
