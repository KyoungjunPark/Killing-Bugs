package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by parkk on 2015-10-09.
 */
public class BugsSprayBullet {
    private static final String TAG ="BugsSprayBullet";

    private Bitmap bulletBitmap;
    private Bitmap rotatedBulletBitmap;
    private float x;
    private float y;

    private double speedX = 0;
    private double speedY = -10;

    private int width;
    private int height;

    private Context mContext;

    public BugsSprayBullet(Context context)
    {
        mContext = context;
        initialize();
    }
    private void initialize()
    {
        bulletBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.spray_pill);
        width = bulletBitmap.getWidth();
        height = bulletBitmap.getHeight();
    }
    public void draw(Canvas canvas)
    {
        x += speedX;
        y += speedY;

        canvas.drawBitmap(rotatedBulletBitmap, x-width/2, y-height/2, new Paint());
    }
    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    public void setRotation(Matrix matrix)
    {
        rotatedBulletBitmap = Bitmap.createBitmap(bulletBitmap, 0, 0, bulletBitmap.getWidth(), bulletBitmap.getHeight(), matrix, true);

        width = rotatedBulletBitmap.getWidth();
        height = rotatedBulletBitmap.getHeight();
    }
    public void setSpeeds(double angle)
    {
        double newX, newY;
        double radianAngle = angle*(Math.PI)/180;
        newX = speedX*Math.cos(radianAngle) - speedY*Math.sin(radianAngle);
        newY = speedX*Math.sin(radianAngle) + speedY*Math.cos(radianAngle);

        this.speedX = newX;
        this.speedY = newY;
    }
    public float getX(){return x;}
    public float getY(){return y;}
    public void remove()
    {
        bulletBitmap.recycle();
        rotatedBulletBitmap.recycle();
    }
}
