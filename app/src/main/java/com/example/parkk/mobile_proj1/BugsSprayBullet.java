package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by parkk on 2015-10-09.
 */
public class BugsSprayBullet {
    private static final String TAG ="BugsSprayBullet";

    private Bitmap bulletBitmap;
    private float x;
    private float y;

    private float speedX = 0;
    private float speedY = -10;

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

        canvas.drawBitmap(bulletBitmap, x, y-height/2, new Paint());
    }
    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
