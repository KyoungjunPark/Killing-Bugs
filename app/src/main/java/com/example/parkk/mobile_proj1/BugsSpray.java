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
import java.util.Iterator;

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

    private float bullet_x;
    private float bullet_y;

    public boolean isBulletClicked = false;
    public boolean isParticlesClicked = false;

    private BulletShootTask bulletShoot;

    private int currentAngle = 0;
    private int rotateAngle = 0;
    private int angleSize = 6;
    private boolean isRotated = false;

    private int bulletPower =1;

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

            newX = getNewSpray_X(bullet_x, bullet_y);
            newY = getNewSpray_Y(bullet_x, bullet_y);
            bullet_x = newX;
            bullet_y = newY;

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

        if(isParticlesClicked) {
            if (rotatedParticlesBitmap == null)
                canvas.drawBitmap(particlesBitmap, particle_x - particles_width / 2, particle_y - particles_height / 2, new Paint());
            else
                canvas.drawBitmap(rotatedParticlesBitmap, particle_x - particles_width / 2, particle_y - particles_height / 2, new Paint());
        }

        //refresh bullets
        for(Iterator<BugsSprayBullet> iterator = bullets.iterator() ; iterator.hasNext();){
            try {
                BugsSprayBullet bullet = iterator.next();
                if (isOut(bullet)) {
                    bullet.remove();
                    iterator.remove();
                }
            } catch (java.util.ConcurrentModificationException e){
                Log.e(TAG, "ConcurrentModificationException is called", e);
            }
        }
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

        this.particle_x = x +65;
        this.particle_y = y - particles_height/2;

        this.bullet_x = particle_x;
        this.bullet_y = particle_y + 160;
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
        return (float)newX+ circle_x;
    }
    private float getNewSpray_Y(float x, float y)
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;
        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newY = tmpX*Math.sin(radianAngle) + tmpY*Math.cos(radianAngle);
        //double newY = circle_radius * Math.sqrt((tmpY*tmpY)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newY + circle_y;
    }
    private boolean isOut(BugsSprayBullet bullet)
    {
        float x = bullet.getX();
        float y = bullet.getY();

        return ((x-circle_x)*(x-circle_x)+(y-circle_y)*(y-circle_y)
                > circle_radius*circle_radius);
    }
    public ArrayList<BugsSprayBullet> getBullets()
    {
        return bullets;
    }
    public class BulletShootTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(!isCancelled()) {
                //draw the bullet
                for(int i = 0 ; i < bulletPower ; i++) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(currentAngle+(i*2));

                    BugsSprayBullet bullet = new BugsSprayBullet(mContext);
                    bullet.setPosition(bullet_x, bullet_y);
                    bullet.setRotation(matrix);
                    bullet.setSpeeds(currentAngle + (i*2));
                    bullets.add(bullet);
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }
    public float getSpray_x()
    {
        return spray_x;
    }
    public float getSpray_y()
    {
        return spray_y;
    }
    public void startShootBullet()
    {
        bulletShoot = new BulletShootTask();
        bulletShoot.execute();
    }
    public void pauseShootBullet()
    {
        bulletShoot.cancel(true);
    }
    public void startParticles()
    {
        isParticlesClicked = true;
    }
    public void pauseParticles()
    {
        isParticlesClicked = false;
    }
    public void upBulletPower()
    {
        if(bulletPower < 4) bulletPower++;
        if(bulletPower == 1){
            particlesBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.spray_particles);
            particles_width = particlesBitmap.getWidth();
            particles_height = particlesBitmap.getHeight();
        } else if(bulletPower == 2){
            particlesBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.spray_particles_power2);
            particles_width = particlesBitmap.getWidth();
            particles_height = particlesBitmap.getHeight();
        } else if(bulletPower == 3){
            particlesBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.spray_particles_power3);
            particles_width = particlesBitmap.getWidth();
            particles_height = particlesBitmap.getHeight();
        }
    }
    public boolean isParticlesKill(float x, float y)
    {
        return (isParticlesClicked && (((particle_x - particles_width/2) < x) && ((particle_x + particles_width/2) > x))
                && (((particle_y - particles_height/2) < y) && ((particle_y + particles_height/2) > y)));

    }
}
