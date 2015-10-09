package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

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
    private int circle_radius;
    private float x;
    private float y;

    private int currentAngle = 0;
    private int angleSize = 6;
    private boolean isRotated = false;

    private Path spray_path;

    private ArrayList<BugsSprayBullet> bullets;


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

        bullets = new ArrayList<>();

        spray_path = new Path();
        //new BulletShootTask().execute();
    }
    public void draw(Canvas canvas)
    {
        float newX;
        float newY;
        Bitmap tmp;

        if(isRotated) {
            Log.d(TAG,"before : "+x+"/"+y);
            newX = getNew_X();
            newY = getNew_Y();
            x = newX;
            y = newY;
            Log.d(TAG, "new : " + newX + "/" + newY);

            Matrix matrix = new Matrix();
            matrix.postRotate(angleSize);
            tmp = sprayBitmap;

            sprayBitmap = Bitmap.createBitmap(tmp, 0, 0, sprayBitmap.getWidth(), sprayBitmap.getHeight(), matrix, true);
            tmp.recycle();
            width = sprayBitmap.getWidth();
            height = sprayBitmap.getHeight();

            isRotated = false;
        }
        //draw the spray
        canvas.drawBitmap(sprayBitmap, x-width/2, y-height/2, new Paint());

        //draw the bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(canvas);
        }
    }
    public void setCircleCenter(int circle_x, int circle_y, int circle_radius)
    {
        this.circle_x = circle_x;
        this.circle_y = circle_y;
        this.circle_radius = circle_radius;

        //spray_path.addCircle(circle_x, circle_y, circle_radius, );
    }
    public void setInitialPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void moveRight()
    {
        angleSize = -6;
        currentAngle += angleSize;
        isRotated = true;
    }
    public void moveLeft()
    {
        angleSize = 6;
        currentAngle += angleSize;
        isRotated = true;
    }
    private float getNew_X()
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;
        Log.d(TAG,"tmpX : "+tmpX+"/"+tmpY);
        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newX = tmpX*Math.cos(radianAngle) - tmpY*Math.sin(radianAngle);
        //double newX = circle_radius * Math.sqrt((tmpX*tmpX)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newX+ circle_x;
    }
    private float getNew_Y()
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;
        Log.d(TAG,"tmpY : "+tmpX+"/"+tmpY);
        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newY = tmpX*Math.sin(radianAngle) + tmpY*Math.cos(radianAngle);
        //double newY = circle_radius * Math.sqrt((tmpY*tmpY)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newY + circle_y;
    }
    public void shootBullet()
    {

    }
    public class BulletShootTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            while(true) {
                //draw the bullet
                BugsSprayBullet bullet = new BugsSprayBullet(mContext);
                bullet.setPosition(x + 40, y);
                bullets.add(bullet);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
