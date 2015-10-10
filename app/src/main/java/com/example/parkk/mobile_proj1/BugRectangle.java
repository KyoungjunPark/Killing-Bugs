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
public class BugRectangle {
    private static final String TAG ="BugRectangle";
    private float x;
    private float y;
    private float width;
    private float height;

    private Paint paint;

    private Path path;
    private int iCurStep;
    private float pathSegmentLen;
    private int pathSegmentNum;
    private PathMeasure pathMeasure;

    private Paint pathPaint;

    private float[] pos;
    private float[] tan;

    public BugRectangle(float x, float y){
        this.x = x;
        this.y = y;

        initialize();
    }
    private void initialize(){
        width = 30;
        height = 30;

        paint = new Paint();

        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);

        path = new Path();

        pos = new float[2];
        tan = new float[2];

        iCurStep = 0;

        pathPaint = new Paint();
        pathPaint.setColor(Color.RED);
        pathPaint.setStrokeWidth(1);
        pathPaint.setStyle(Paint.Style.STROKE);

        pathSetting();
    }
    public void draw(Canvas canvas){
        paint.setColor(Color.RED);

        //canvas.drawPath(path, pathPaint);

        //path setting
        if(iCurStep <= pathSegmentNum){
            pathMeasure.getPosTan(pathSegmentLen * iCurStep, pos, tan);
            canvas.drawRect(pos[0]-width/2, pos[1]-height/2, pos[0]+width/2, pos[1] +height/2, paint);
            x = pos[0];
            y = pos[1];

            iCurStep++;
        }
    }
    private void pathSetting()
    {
        int angle = (int) (Math.random() * 361);
        float radius, destX, destY, largePeakX, largePeakY;
        radius = 100;
        pathSegmentNum = 1500;

        destX = getNewSpray_X(radius, 0, angle);
        destY = getNewSpray_Y(radius, 0, angle);

        largePeakX = getNewSpray_X(destX, destY, 45);
        largePeakY = getNewSpray_Y(destX, destY, 45);

        path.moveTo(x, y);
        for(int i = 0; i < 30 ; i++){
            path.rCubicTo(0,0,largePeakX,largePeakY,destX,destY);
            path.rCubicTo(0,0,-largePeakX,-largePeakY,-destX,-destY);
            path.rCubicTo(0,0,-largePeakX,-largePeakY,-destX,-destY);
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
        return (float)newX;
    }
    private float getNewSpray_Y(float x, float y, int angleSize)
    {
        double tmpX = x;
        double tmpY = y;
        double radianAngle = (double)angleSize*(Math.PI)/180;
        double newY = tmpX*Math.sin(radianAngle) + tmpY*Math.cos(radianAngle);
        return (float)newY;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
