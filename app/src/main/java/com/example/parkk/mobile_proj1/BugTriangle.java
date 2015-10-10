package com.example.parkk.mobile_proj1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Typeface;

/**
 * Created by KJPARK on 2015-10-10.
 */
public class BugTriangle {
    private static final String TAG ="BugTriangle";
    private float x;
    private float y;

    private final int triangleSize = 30;
    private final int degree_of_triangle = 60;
    private final float radian_of_triangle = (float) Math
            .toRadians(degree_of_triangle);
    private Path trianglePath;

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
    public BugTriangle(float x, float y){
        this.x = x;
        this.y = y;

        initialize();
    }
    private void initialize(){
        paint = new Paint();

        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);

        path = new Path();
        trianglePath = new Path();

        pos = new float[2];
        tan = new float[2];

        iCurStep = 0;

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStrokeWidth(1);
        pathPaint.setStyle(Paint.Style.STROKE);

        pathSetting();
    }
    public void draw(Canvas canvas){
        paint.setColor(Color.BLACK);

        //canvas.drawPath(path, pathPaint);

        //path setting
        if(iCurStep <= pathSegmentNum) {
            pathMeasure.getPosTan(pathSegmentLen * iCurStep, pos, tan);
            x = pos[0];
            y = pos[1];
            drawTriangle();
            canvas.drawPath(trianglePath, paint);

            iCurStep++;
        }
    }
    private void pathSetting()
    {
        int angle = (int) (Math.random() * 361);
        int angle2 = (int) (Math.random() * 361);
        int angle3 = (int) (Math.random() * 361);

        float destX, destY, peak1X, peak1Y, peak2X, peak2Y, peak3X, peak3Y;
        destX = getNewSpray_X(100,0, angle);
        destY = getNewSpray_Y(100, 0, angle);

        peak1X = getNewSpray_X(destX / 2, destY / 2, -60);
        peak1Y = getNewSpray_Y(destX / 2, destY / 2, -60);

        peak2X = getNewSpray_X(destX, destY, angle2);
        peak2Y = getNewSpray_Y(destX, destY, angle2);

        peak3X = getNewSpray_X(destX, destY, angle3);
        peak3Y = getNewSpray_Y(destX , destY, angle3);

        pathSegmentNum = 1500;
        //path.addCircle(x, y, 100, Path.Direction.CCW);
        path.moveTo(x, y);

        for(int i = 0 ; i < 30; i++){
            int value =(int) (Math.random() *3);
            if(value == 0) {
                path.rCubicTo(0, 0, peak1X, peak1Y, destX, destY);
            }else if(value == 1) {
                path.rCubicTo(0, 0, peak2X, peak2Y, -destX / 3, destY / 3);
            }else {
                path.rLineTo(peak3X, peak3Y);
            }
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
    private void drawTriangle()
    {
        trianglePath.reset();
        trianglePath.moveTo(x, y - triangleSize / 2);
        trianglePath.rLineTo(-triangleSize * (float) Math.cos(radian_of_triangle),
                +triangleSize * (float) Math.sin(radian_of_triangle));
        trianglePath.rLineTo(+triangleSize, 0);
        trianglePath.rLineTo(-triangleSize / 2,
                -triangleSize * (float) Math.sin(radian_of_triangle));
        trianglePath.rLineTo(-triangleSize * (float) Math.cos(radian_of_triangle),
                +triangleSize * (float) Math.sin(radian_of_triangle));
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
