package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by KJPARK on 2015-10-08.
 */
public class BugsSpray {
    private static final String TAG ="BugsSpray";
    private Bitmap sprayBitmap;
    private Bitmap rotatedSprayBitmap;

    private Bitmap particlesBitmap;
    private Bitmap rotatedParticlesBitmap;

    private Context mContext;

    private int spray_width;
    private int spray_height;

    private int particles_width;
    private int particles_height;
    private int circle_x;
    private int circle_y;
    private int circle_radius;
    private float spray_x;
    private float spray_y;

    private float particle_x;
    private float particle_y;


    private int currentAngle = 0;
    private int rotateAngle = 0;
    private int angleSize = 6;
    private boolean isRotated = false;

    //private Path spray_path;

    private ArrayList<BugsSprayBullet> bullets;


    public BugsSpray(Context context)
    {
        mContext = context;
        initialize();
    }
    private void initialize()
    {
        sprayBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bug_spray);

        spray_width = sprayBitmap.getWidth();
        spray_height = sprayBitmap.getHeight();

        particlesBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.spray_particles);
        particles_width = particlesBitmap.getWidth();
        particles_height = particlesBitmap.getHeight();

        bullets = new ArrayList<>();
        //spray_path = new Path();

        new BulletShootTask().execute();
    }
    public void draw(Canvas canvas)
    {
        float newX;
        float newY;

        if(isRotated) {
            newX = getNewSpray_X(spray_x, spray_y);
            newY = getNewSpray_Y(spray_x, spray_y);
            spray_x = newX;
            spray_y = newY;

            newX = getNewSpray_X(particle_x, particle_y);
            newY = getNewSpray_Y(particle_x, particle_y);
            particle_x = newX;
            particle_y = newY;

            //rotate spray
            Matrix matrix = new Matrix();
            matrix.postRotate(currentAngle);

            rotatedSprayBitmap = Bitmap.createBitmap(sprayBitmap, 0, 0, sprayBitmap.getWidth(), sprayBitmap.getHeight(), matrix, true);

            spray_width = rotatedSprayBitmap.getWidth();
            spray_height = rotatedSprayBitmap.getHeight();

            //rotate particles
            rotatedParticlesBitmap = Bitmap.createBitmap(particlesBitmap, 0, 0, particlesBitmap.getWidth(), particlesBitmap.getHeight(), matrix, true);

            particles_width = rotatedParticlesBitmap.getWidth();
            particles_height = rotatedParticlesBitmap.getHeight();

            isRotated = false;
        }
        //draw the spray
        if(rotatedSprayBitmap == null) canvas.drawBitmap(sprayBitmap, spray_x - spray_width / 2, spray_y - spray_height / 2, new Paint());
        else canvas.drawBitmap(rotatedSprayBitmap, spray_x - spray_width / 2, spray_y - spray_height / 2, new Paint());

        if(rotatedParticlesBitmap == null) canvas.drawBitmap(particlesBitmap, particle_x - particles_width/2, particle_y-particles_height/2, new Paint());
        else canvas.drawBitmap(rotatedParticlesBitmap, particle_x - particles_width/2, particle_y -particles_height/2, new Paint());

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
        this.spray_x = x;
        this.spray_y = y;

        this.particle_x = x +50;
        this.particle_y = y - particles_height/2;
    }
    public void moveRight()
    {
        angleSize = -6;
        currentAngle += angleSize;
        isRotated = true;
        rotateAngle -= 6;
    }
    public void moveLeft()
    {
        angleSize = 6;
        currentAngle += angleSize;
        isRotated = true;
        rotateAngle += 6;
    }
    private float getNewSpray_X(float x, float y)
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;

        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newX = tmpX*Math.cos(radianAngle) - tmpY*Math.sin(radianAngle);
        //double newX = circle_radius * Math.sqrt((tmpX*tmpX)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newX+ circle_x;
    }
    private float getNewSpray_Y(float x, float y)
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

    public class BulletShootTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(true) {
                //draw the bullet
                BugsSprayBullet bullet = new BugsSprayBullet(mContext);
                bullet.setPosition(spray_x + 40, spray_y);

                Matrix matrix = new Matrix();
                matrix.postRotate(currentAngle);
                bullet.setRotation(matrix);
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
