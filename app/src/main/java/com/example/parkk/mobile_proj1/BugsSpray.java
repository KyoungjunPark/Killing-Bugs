package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by KJPARK on 2015-10-08.
 */
public class BugsSpray {
    private static final String TAG ="BugsSpray";
    private Bitmap sprayImage;
    private Context mContext;

    private int screen_width;
    private int screen_height;

    private int width;
    private int height;

    private int pivot_x;
    private int pivot_y;

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
        sprayImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bug_spray);
        width = sprayImage.getWidth();
        height = sprayImage.getHeight();

        //matrix = new Matrix();
    }
    public void draw(Canvas canvas)
    {
        Log.d(TAG, "x:" + x + "/" + "y:" + y + "angle:" + currentSelfAngle);
        canvas.rotate(currentSelfAngle, x, y + height / 2);
        canvas.drawBitmap(sprayImage, x, y + height / 2 + 70, new Paint());
    }
    public void setScreenSize(int width, int height)
    {
        screen_width = width;
        screen_height = height;
    }
    public void setPivot(int pivot_x, int pivot_y)
    {
        this.pivot_x = pivot_x;
        this.pivot_y = pivot_y;
    }
    public void setInitialPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void moveRight()
    {
        currentSelfAngle += 2;
    }
    public void moveLeft()
    {
        currentSelfAngle -= 2;
    }



}
