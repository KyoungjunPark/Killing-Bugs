package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

/**
 * Created by parkk on 2015-10-08.
 */
public class GraphicsView extends View{
    private static final String TAG = "GraphicsView";
    private Paint circlePaint;
    private int screen_width;
    private int screen_height;
    private int circle_x;
    private int circle_y;
    private int radius;


    private BugsSpray bugsSpray;

    public GraphicsView(Context context) {
        super(context);
        initialize();
    }
    public GraphicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    private void initialize()
    {
        /* Circle border initialization */
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.MAGENTA);
        circlePaint.setAntiAlias(true);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                getLocationOnScreen(location);
                screen_width = getWidth();
                screen_height = getHeight();

                circle_x = screen_width/2;
                circle_y = screen_height/2;

                radius = Math.min(screen_width, screen_height)/2 - 100;
                Log.d(TAG,"first :"+circle_x+"/"+circle_y);
                Log.d(TAG, "two :" + (location[0]) + "/" + (location[1]));
                Log.d(TAG, "three :" + screen_width + "/" + screen_height);

                bugsSpray.setPivot(location[0], location[1]);
                bugsSpray.setInitialPosition(location[0], location[1]+radius);

                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        /* BugsSpray initialization */
        bugsSpray = new BugsSpray(getContext());
        bugsSpray.setScreenSize(screen_width, screen_height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //circle border
        canvas.drawCircle(circle_x, circle_y, radius, circlePaint);

        bugsSpray.draw(canvas);


        //invalidate();
    }
    public void sprayLeft() {
        bugsSpray.moveLeft();
        invalidate();
    }
    public void sprayRight(){
        bugsSpray.moveRight();
        invalidate();
    }


}
