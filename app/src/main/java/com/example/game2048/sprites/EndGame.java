package com.example.game2048.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.game2048.R;

public class EndGame implements Sprite
{
    private int screenWidth, screenHeight;
    private Bitmap bmp;
    private boolean visible = false;

    public EndGame(Resources resources, int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        int endGameWidth = (int) resources.getDimension(R.dimen.endgame_width);
        int endGameHeight = (int) resources.getDimension(R.dimen.endgame_height);

        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.gameover);
        bmp = Bitmap.createScaledBitmap(b, endGameWidth, endGameHeight, false);
        b.recycle();
    }

    public void setVisible(boolean isVisible)
    {
        this.visible = isVisible;
    }

    public void recycleBitmap()
    {
        if (bmp != null && !bmp.isRecycled())
        {
            bmp.recycle();
            bmp = null;
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (visible && bmp != null) {
            canvas.drawBitmap(bmp, screenWidth / 2 - bmp.getWidth() / 2,
                    screenHeight / 2 - bmp.getHeight() / 2, null);
        }
    }

    @Override
    public void update()
    {

    }
}
