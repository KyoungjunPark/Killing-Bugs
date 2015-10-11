package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

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
    private boolean isBugsOut = false;
    private Context mContext;

    Paint endPaint;

    private Path circle_path;

    private BugsSpray bugsSpray;

    private ArrayList<BugCircle> bugCircles;
    private int bugCircleNum;
    private int bugCircleAngleSize = 0;

    private ArrayList<BugRectangle> bugRectangles;
    private int bugRectangleNum;
    private int bugRectangleAngleSize = 0;

    private ArrayList<BugTriangle> bugTriangles;
    private int bugTriangleNum;
    private int bugTriangleAngleSize = 0;

    private int stage = 1;
    private int bugKillCount = 0;
    private int bugKillScale = 10;

    private boolean isPlaying = true;

    private TextView killScoreText;


    public GraphicsView(Context context) {
        super(context);
        mContext = context;
        initialize();
    }
    public GraphicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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

        endPaint = new Paint();
        circle_path = new Path();

        bugCircles = new ArrayList<>();
        bugRectangles = new ArrayList<>();
        bugTriangles = new ArrayList<>();

        bugCircleNum = 2;
        bugRectangleNum = 2;
        bugTriangleNum = 2;

        bugCircleAngleSize = 360/bugCircleNum;
        bugRectangleAngleSize = 360/bugRectangleNum;
        bugTriangleAngleSize = 360/bugTriangleNum;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            BugCircle bugCircle;
            BugRectangle bugRectangle;
            BugTriangle bugTriangle;

            @Override
            public void onGlobalLayout() {
                float tmpX, tmpY;
                float newX, newY;
                int[] location = new int[2];
                getLocationOnScreen(location);
                screen_width = getWidth();
                screen_height = getHeight();

                circle_x = screen_width / 2;
                circle_y = screen_height / 2;

                radius = Math.min(screen_width, screen_height) / 2 - 40;

                bugsSpray.setInitialPosition(circle_x, circle_y + radius);
                bugsSpray.setCircleCenter(circle_x, circle_y, radius);

                circle_path.addCircle(circle_x, circle_y, radius, Path.Direction.CW);

                //Make BugCircles
                tmpX = circle_x;
                tmpY = circle_y - radius / 2;
                for (int i = 0; i < bugCircleNum; i++) {
                    bugCircle = new BugCircle(tmpX, tmpY);
                    bugCircles.add(bugCircle);

                    newX = getNewSpray_X(tmpX, tmpY, bugCircleAngleSize);
                    newY = getNewSpray_Y(tmpX, tmpY, bugCircleAngleSize);
                    tmpX = newX;
                    tmpY = newY;
                }
                //Make BugRectangles
                tmpX = circle_x;
                tmpY = circle_y - radius / 4;
                for (int i = 0; i < bugRectangleNum; i++) {
                    bugRectangle = new BugRectangle(tmpX, tmpY);
                    bugRectangles.add(bugRectangle);

                    newX = getNewSpray_X(tmpX, tmpY, bugRectangleAngleSize);
                    newY = getNewSpray_Y(tmpX, tmpY, bugRectangleAngleSize);
                    tmpX = newX;
                    tmpY = newY;
                }
                //Make BugTriangles
                tmpX = circle_x;
                tmpY = circle_y - radius / 8;
                for (int i = 0; i < bugTriangleNum; i++) {
                    bugTriangle = new BugTriangle(tmpX, tmpY);
                    bugTriangles.add(bugTriangle);

                    newX = getNewSpray_X(tmpX, tmpY, bugTriangleAngleSize);
                    newY = getNewSpray_Y(tmpX, tmpY, bugTriangleAngleSize);
                    tmpX = newX;
                    tmpY = newY;
                }
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        /* BugsSpray initialization */
        bugsSpray = new BugsSpray(getContext());

        Toast.makeText(mContext.getApplicationContext(), "STAGE "+stage, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //circle border

        if(isPlaying) {
            canvas.drawPath(circle_path, circlePaint);
            bugsSpray.draw(canvas);

            //when bugs are out to the circle
            bugRefresh();

            //draw bugCircles
            for (int i = 0; i < bugCircles.size(); i++) {
                bugCircles.get(i).draw(canvas);
            }
            //draw bugRectangles
            for (int i = 0; i < bugRectangles.size(); i++) {
                bugRectangles.get(i).draw(canvas);
            }
            //draw bugTriangles
            for (int i = 0; i < bugTriangles.size(); i++) {
                bugTriangles.get(i).draw(canvas);
            }
            //when program is end
            if (isSprayCollied() || isBugsOut) {
                endPaint.setColor(Color.RED);
                endPaint.setTextSize(200);
                endPaint.setFakeBoldText(true);
                isPlaying = false;
                canvas.drawText("MISSION FAILED", circle_x - 670, circle_y, endPaint);
                return;
            }
            if (isAllBugsDead()) {
                Toast.makeText(mContext.getApplicationContext(), "STAGE " + (++stage), Toast.LENGTH_LONG).show();
                programRestart();
            }
            if(bugKillCount % bugKillScale == 0 && bugKillCount != 0){
                bugKillScale*=2.5;
                bugsSpray.upBulletPower();
            }

            invalidate();
        }
    }
    public void sprayLeft() {
        if(isPlaying) {
            bugsSpray.moveLeft();
            invalidate();
        }
    }
    public void sprayRight(){
        if(isPlaying) {
            bugsSpray.moveRight();
            invalidate();
        }
    }
    private float getNewSpray_X(float x, float y, int angleSize)
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;

        double radianAngle = (double) angleSize *(Math.PI)/180;
        double newX = tmpX*Math.cos(radianAngle) - tmpY*Math.sin(radianAngle);
        //double newX = circle_radius * Math.sqrt((tmpX*tmpX)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newX+ circle_x;
    }
    private float getNewSpray_Y(float x, float y, int angleSize)
    {
        double tmpX = x - circle_x;
        double tmpY = y - circle_y;
        double radianAngle = (double) angleSize *(Math.PI)/180;
        double newY = tmpX * Math.sin(radianAngle) + tmpY * Math.cos(radianAngle);
        //double newY = circle_radius * Math.sqrt((tmpY*tmpY)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newY + circle_y;
    }
    private void bugRefresh()
    {
        try {
            for (Iterator<BugsSprayBullet> bulletIterator = bugsSpray.getBullets().iterator(); bulletIterator.hasNext(); ) {
                BugsSprayBullet bullet = bulletIterator.next();

                for (Iterator<BugCircle> iterator = bugCircles.iterator(); iterator.hasNext(); ) {
                    BugCircle bug = iterator.next();
                    if (isOut(bug.getX(), bug.getY()) || isShot(bullet, bug.getX(), bug.getY())) {
                        iterator.remove();
                    }
                }
                for (Iterator<BugRectangle> iterator = bugRectangles.iterator(); iterator.hasNext(); ) {
                    BugRectangle bug = iterator.next();
                    if (isOut(bug.getX(), bug.getY()) || isShot(bullet, bug.getX(), bug.getY())) {
                        iterator.remove();
                    }
                }
                for (Iterator<BugTriangle> iterator = bugTriangles.iterator(); iterator.hasNext(); ) {
                    BugTriangle bug = iterator.next();
                    if (isOut(bug.getX(), bug.getY()) || isShot(bullet, bug.getX(), bug.getY())) {
                        iterator.remove();
                    }
                }
            }
            for (Iterator<BugCircle> iterator = bugCircles.iterator(); iterator.hasNext(); ) {
                BugCircle bug = iterator.next();
                if (isParticlesShot(bug.getX(), bug.getY())) {
                    iterator.remove();
                }
            }
            for (Iterator<BugRectangle> iterator = bugRectangles.iterator(); iterator.hasNext(); ) {
                BugRectangle bug = iterator.next();
                if (isParticlesShot(bug.getX(), bug.getY())) {
                    iterator.remove();
                }
            }
            for (Iterator<BugTriangle> iterator = bugTriangles.iterator(); iterator.hasNext(); ) {
                BugTriangle bug = iterator.next();
                if (isParticlesShot(bug.getX(), bug.getY())) {
                    iterator.remove();
                }
            }
        } catch(java.util.ConcurrentModificationException e){
            Log.e(TAG, "Update random pattern thread", e);
        }
    }
    private boolean isOut(float x, float y){
       if((x-circle_x)*(x-circle_x)+(y-circle_y)*(y-circle_y)
                > radius*radius){
           isBugsOut = true;
           return true;
       }
        return false;
    }
    private boolean isShot(BugsSprayBullet bullet, float x, float y){
        if((bullet.getX() - x <30 && bullet.getX() -x > -30
            &&bullet.getY() - y < 30 && bullet.getY() - y > -30)){
            bugKillCount++;
            killScoreText.setText(mContext.getString(R.string.Score, bugKillCount));
            return true;
        }
        return false;
    }
    private boolean isParticlesShot(float x, float y){
        if(bugsSpray.isParticlesKill(x, y)){
            bugKillCount++;
            killScoreText.setText(mContext.getString(R.string.Score, bugKillCount));
            return true;
        }
        return false;
    }
    private boolean isSprayCollied()
    {
        for (Iterator<BugCircle> iterator = bugCircles.iterator(); iterator.hasNext(); ) {
            BugCircle bug = iterator.next();
            if((bugsSpray.getSpray_x() - bug.getX() <40 && bugsSpray.getSpray_x() -bug.getX() > -40
                    &&bugsSpray.getSpray_y() - bug.getY() < 40 && bugsSpray.getSpray_y() - bug.getY() > -40))
                return true;
        }
        for (Iterator<BugRectangle> iterator = bugRectangles.iterator(); iterator.hasNext(); ) {
            BugRectangle bug = iterator.next();
            if((bugsSpray.getSpray_x() - bug.getX() <40 && bugsSpray.getSpray_x() -bug.getX() > -40
                    &&bugsSpray.getSpray_y() - bug.getY() < 40 && bugsSpray.getSpray_y() - bug.getY() > -40))
                return true;
        }
        for (Iterator<BugTriangle> iterator = bugTriangles.iterator(); iterator.hasNext(); ) {
            BugTriangle bug = iterator.next();
            if((bugsSpray.getSpray_x() - bug.getX() <40 && bugsSpray.getSpray_x() -bug.getX() > -40
                    &&bugsSpray.getSpray_y() - bug.getY() < 40 && bugsSpray.getSpray_y() - bug.getY() > -40))
                return true;
        }
        return false;
    }
    private boolean isAllBugsDead()
    {
        return (bugCircles.size() == 0 && bugRectangles.size() == 0 && bugTriangles.size() == 0);

    }
    public void shootBullet()
    {
        bugsSpray.startShootBullet();
    }
    public void pauseShootBullet()
    {
        bugsSpray.pauseShootBullet();
    }
    public void shootParticles()
    {
        bugsSpray.startParticles();
    }
    public void pauseParticles()
    {
        bugsSpray.pauseParticles();
    }
    private void programRestart()
    {
        float tmpX, tmpY;
        float newX, newY;
        BugCircle bugCircle;
        BugRectangle bugRectangle;
        BugTriangle bugTriangle;

        bugCircleNum = 2*stage;
        bugRectangleNum = 2*stage;
        bugTriangleNum = 2*stage;

        bugCircleAngleSize = 360/bugCircleNum;
        bugRectangleAngleSize = 360/bugRectangleNum;
        bugTriangleAngleSize = 360/bugTriangleNum;

        //Make BugCircles
        tmpX = circle_x;
        tmpY = circle_y - radius / 2;
        for (int i = 0; i < bugCircleNum; i++) {
            bugCircle = new BugCircle(tmpX, tmpY);
            bugCircles.add(bugCircle);

            newX = getNewSpray_X(tmpX, tmpY, bugCircleAngleSize);
            newY = getNewSpray_Y(tmpX, tmpY, bugCircleAngleSize);
            tmpX = newX;
            tmpY = newY;
        }
        //Make BugRectangles
        tmpX = circle_x;
        tmpY = circle_y - radius / 4;
        for (int i = 0; i < bugRectangleNum; i++) {
            bugRectangle = new BugRectangle(tmpX, tmpY);
            bugRectangles.add(bugRectangle);

            newX = getNewSpray_X(tmpX, tmpY, bugRectangleAngleSize);
            newY = getNewSpray_Y(tmpX, tmpY, bugRectangleAngleSize);
            tmpX = newX;
            tmpY = newY;
        }
        //Make BugTriangles
        tmpX = circle_x;
        tmpY = circle_y - radius / 8;
        for (int i = 0; i < bugTriangleNum; i++) {
            bugTriangle = new BugTriangle(tmpX, tmpY);
            bugTriangles.add(bugTriangle);

            newX = getNewSpray_X(tmpX, tmpY, bugTriangleAngleSize);
            newY = getNewSpray_Y(tmpX, tmpY, bugTriangleAngleSize);
            tmpX = newX;
            tmpY = newY;
        }

    }
    public void setKillScoreText(TextView textView){
        killScoreText = textView;
    }
}
