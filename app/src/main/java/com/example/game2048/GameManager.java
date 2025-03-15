package com.example.game2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.game2048.sprites.Grid;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback
{
    private MainThread thread;
    private Grid grid;
    private int screenWidth, screenHeight, standardSize;
    private TileManager tileManager;

    private SwipeListener swipe;

    public GameManager(Context context, AttributeSet set)
    {
        super(context, set);
        setLongClickable(true);
        getHolder().addCallback(this);

        swipe = new SwipeListener(getContext(), this);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        standardSize = (int) (screenWidth * 0.88) / 4;

        grid = new Grid(getResources(), screenWidth, screenHeight, standardSize);
        tileManager = new TileManager(getResources(), standardSize, screenWidth, screenHeight);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        thread = new MainThread(surfaceHolder, this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height)
    {
        thread.setSurfaceHolder(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
    {
        boolean retry = true;

        while(retry)
        {
            try
            {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void update()
    {
        tileManager.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawRGB(90, 90, 90);
        grid.draw(canvas);
        tileManager.draw(canvas);

    }

    @Override
    public void onSwipe(Direction direction)
    {
        tileManager.onSwipe(direction);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        swipe.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
