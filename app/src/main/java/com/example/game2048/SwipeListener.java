package com.example.game2048;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SwipeListener implements GestureDetector.OnGestureListener
{
    private GestureDetector detector;
    private SwipeCallback callback;
    public SwipeListener(Context context, SwipeCallback callback)
    {
        this.callback = callback;
        detector = new GestureDetector(context, this);
    }

    public void onTouchEvent(MotionEvent e)
    {
        detector.onTouchEvent(e);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e)
    {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e)
    {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY)
    {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e)
    {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY)
    {
        if(Math.abs(velocityX) > Math.abs(velocityY))
        {
            if(velocityX > 0)
            {
                callback.onSwipe(SwipeCallback.Direction.RIGHT);
            }
            else
            {
                callback.onSwipe(SwipeCallback.Direction.LEFT);
            }
        }
        else
        {
            if(velocityY > 0)
            {
                callback.onSwipe(SwipeCallback.Direction.DOWN);
            }
            else
            {
                callback.onSwipe(SwipeCallback.Direction.UP);
            }
        }

        return false;
    }
}
