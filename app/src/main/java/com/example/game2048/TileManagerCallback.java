package com.example.game2048;

import android.graphics.Bitmap;

import com.example.game2048.sprites.Tile;

public interface TileManagerCallback
{
    Bitmap getBitmap(int count);
    void finishedMoving(Tile t);
}
