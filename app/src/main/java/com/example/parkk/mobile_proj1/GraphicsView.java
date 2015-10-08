package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by parkk on 2015-10-08.
 */
public class GraphicsView extends View{

    private Paint circlePaint;
    int screen_width;
    int screen_height;
    int circle_x;
    int circle_y;
    int radius;

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
        //circle border setting
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.CYAN);
        circlePaint.setAntiAlias(true);

        screen_width = getWidth();
        screen_height = getHeight();

        circle_x = screen_width/2;
        circle_y = screen_height/2;
        radius = Math.min(screen_width, screen_height)/2;

        //
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //circle border

        canvas.drawCircle(circle_x, circle_y , radius, circlePaint);



    }

}
