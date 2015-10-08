package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by KJPARK on 2015-10-08.
 */
public class BugsSpray {
    private static final String TAG ="BugsSpray";
    private Bitmap sprayBitmap;
    private Context mContext;


    private int width;
    private int height;

    private int circle_x;
    private int circle_y;

    private int x;
    private int y;

    private int currentSelfAngle = 0;

    private Matrix matrix;

    public BugsSpray(Context context)
    {
        mContext = context;
        initialize();
    }
    private void initialize()
    {
        sprayBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bug_spray);
        width = sprayBitmap.getWidth();
        height = sprayBitmap.getHeight();

        //matrix = new Matrix();
    }
    public void draw(Canvas canvas)
    {
        Log.d(TAG, "circle_x:" + circle_x + "/" + "circle_y:"
                + circle_y + "angle:" + currentSelfAngle);
        canvas.save();
        canvas.rotate(currentSelfAngle, circle_x, circle_y);
        canvas.drawBitmap(sprayBitmap, x, y + height / 2 + 70, new Paint());
        canvas.restore();
    }
    public void setCircleCenter(int circle_x, int circle_y)
    {
        this.circle_x = circle_x;
        this.circle_y = circle_y;
    }
    public void setInitialPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void moveRight()
    {
        currentSelfAngle -= 6;
    }
    public void moveLeft()
    {
        currentSelfAngle += 6;
    }



}
