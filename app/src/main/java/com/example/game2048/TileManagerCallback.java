package com.example.game2048;

import android.graphics.Bitmap;

public interface TileManagerCallback
{
    Bitmap getBitmap(int count);
}
