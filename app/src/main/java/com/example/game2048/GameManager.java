package com.example.game2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.game2048.sprites.EndGame;
import com.example.game2048.sprites.Grid;
import com.example.game2048.sprites.Score;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback, GameManagerCallback
{
    private static final String APP_NAME = "2048";
    private MainThread thread;
    private Grid grid;
    private int screenWidth, screenHeight, standardSize;
    private TileManager tileManager;
    private boolean endGame = false;
    private EndGame endGameSprite;
    private Score score;

    private SwipeListener swipe;
    private Bitmap restartButton;
    private int restartButtonX, restartButtonY, restartButtonSize;

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
        tileManager = new TileManager(getResources(), standardSize, screenWidth, screenHeight, this);
        endGameSprite = new EndGame(getResources(), screenWidth, screenHeight);
        score = new Score(getResources(), screenWidth, screenHeight, standardSize,
                getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));

        restartButtonSize = (int) getResources().getDimension(R.dimen.restart_button_size);
        Bitmap bmpRestart = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
        restartButton = Bitmap.createScaledBitmap(bmpRestart, restartButtonSize, restartButtonSize, false);

        restartButtonX = screenWidth / 2 + 2 * standardSize - restartButtonSize;
        restartButtonY = screenHeight / 2 - 3 * standardSize - restartButtonSize / 2;
    }

    public void initGame()
    {
        endGame = false;

        if (endGameSprite != null)
        {
            endGameSprite.setVisible(false);
            endGameSprite.recycleBitmap();
        }

        tileManager.initGame();

        score = new Score(getResources(), screenWidth, screenHeight, standardSize,
                getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));
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

    public TileManager getTileManager()
    {
        return tileManager;
    }

    public void update()
    {
        if(!endGame)
        {
            tileManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawRGB(90, 90, 90);
        grid.draw(canvas);
        tileManager.draw(canvas);
        score.draw(canvas);
        canvas.drawBitmap(restartButton, restartButtonX, restartButtonY, null);

        if(endGame)
        {
            endGameSprite.draw(canvas);
        }

    }

    @Override
    public void onSwipe(Direction direction)
    {
        tileManager.onSwipe(direction);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(endGame)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                initGame();
                return true;
            }
        }
        else
        {
            float eventX = event.getX();
            float eventY = event.getY();

            if(event.getAction() == MotionEvent.ACTION_DOWN
                    && eventX > restartButtonX
                    && eventX < restartButtonX + restartButtonSize
                    && eventY > restartButtonY
                    && eventY < restartButtonY + restartButtonSize)
            {
                initGame();
                return true;
            }
            else
            {
                swipe.onTouchEvent(event);
            }
        }
        return true;
    }

    @Override
    public void gameOver()
    {
        endGame = true;

        if (endGameSprite != null)
        {
            endGameSprite.setVisible(true);
        }
    }

    @Override
    public void updateScore(int delta)
    {
        score.updateScore(delta);
    }

    @Override
    public void reached2048()
    {
        score.reached2048();
    }
}
