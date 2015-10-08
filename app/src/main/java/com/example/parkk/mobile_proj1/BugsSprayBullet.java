package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by parkk on 2015-10-09.
 */
public class BugsSprayBullet {
    private static final String TAG ="BugsSprayBullet";

    private Bitmap bulletBitmap;
    private int x;
    private int y;

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

        //matrix = new Matrix();
    }
}
