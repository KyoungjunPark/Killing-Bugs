package com.example.parkk.mobile_proj1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by KJPARK on 2015-10-10.
 */
public class BugCircle {
    private static final String TAG ="BugCircle";

    private float x;
    private float y;
    private float radius = 30;

    private Paint paint;

    private Path path;
    private int iCurStep;
    private Matrix matrix;
    private float pathSegmentLen;
    private int pathSegmentNum;
    private PathMeasure pathMeasure;

    private Paint pathPaint;

    private float[] pos;
    private float[] tan;
    public BugCircle(float x, float y){
        this.x = x;
        this.y = y;

        initialize();
    }
    private void initialize(){
        paint = new Paint();

        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);

        path = new Path();
        matrix = new Matrix();

        pos = new float[2];
        tan = new float[2];

        iCurStep = 0;

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(1);
        pathPaint.setStyle(Paint.Style.STROKE);

        pathSetting();
    }
    public void draw(Canvas canvas){
        paint.setColor(Color.BLUE);

        //canvas.drawPath(path, pathPaint);

        //path setting
        if(iCurStep <= pathSegmentNum){
            pathMeasure.getPosTan(pathSegmentLen * iCurStep, pos, tan);
            canvas.drawCircle(pos[0], pos[1], radius, paint);
            x = pos[0];
            y = pos[1];

            iCurStep++;
        }
    }
    private void pathSetting()
    {
        int angle = (int) (Math.random() * 361);
        float destX, destY, largePeakX, largePeakY, smallPeakX, smallPeakY;
        destX = getNewSpray_X(100,0, angle);
        destY = getNewSpray_Y(100,0, angle);

        largePeakX = getNewSpray_X(destX / 2, destY / 2, -60);
        largePeakY = getNewSpray_Y(destX/2, destY / 2, -60);

        smallPeakX = getNewSpray_X(destX, destY, 90);
        smallPeakY = getNewSpray_Y(destX, destY, 90);

        pathSegmentNum = 1500;
        //path.addCircle(x, y, 100, Path.Direction.CCW);
        path.moveTo(x, y);

        for(int i = 0 ; i < 30; i++){
            path.rCubicTo(0,0, largePeakX, largePeakY, destX, destY);
            path.rCubicTo(0,0, smallPeakX, smallPeakY, -destX/3, destY/3);
        }
        
        pathMeasure = new PathMeasure(path, false);
        pathSegmentLen = pathMeasure.getLength() / pathSegmentNum;//20 animation steps
    }
    private float getNewSpray_X(float x, float y, int angleSize)
    {
        double tmpX = x;
        double tmpY = y;

        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newX = tmpX*Math.cos(radianAngle) - tmpY*Math.sin(radianAngle);
        //double newX = circle_radius * Math.sqrt((tmpX*tmpX)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newX;
    }
    private float getNewSpray_Y(float x, float y, int angleSize)
    {
        double tmpX = x;
        double tmpY = y;
        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newY = tmpX*Math.sin(radianAngle) + tmpY*Math.cos(radianAngle);
        //double newY = circle_radius * Math.sqrt((tmpY*tmpY)/((tmpX*tmpX)+(tmpY*tmpY)));
        return (float)newY;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
