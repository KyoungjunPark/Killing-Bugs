package com.example.parkk.mobile_proj1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Typeface;

/**
 * Created by KJPARK on 2015-10-10.
 */
public class Items {
    private static final String TAG ="Items";
    private float x;
    private float y;

    private float chicken_width;
    private float chicken_height;

    private float pizza_width;
    private float pizza_height;

    private Context mContext;

    private Bitmap chickenBitmap;
    private Bitmap pizzaBitmap;

    private Paint paint;

    private Path path;
    private int iCurStep;
    private float pathSegmentLen;
    private int pathSegmentNum;
    private PathMeasure pathMeasure;

    private Paint pathPaint;

    private float[] pos;
    private float[] tan;

    private boolean isChicken = false;

    public Items(Context context, float x, float y){
        mContext = context;
        this.x = x;
        this.y = y;

        initialize();
    }
    private void initialize(){

        chickenBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.chicken_item);

        chicken_width = chickenBitmap.getWidth();
        chicken_height = chickenBitmap.getHeight();

        pizzaBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pizza_item);

        pizza_width = pizzaBitmap.getWidth();
        pizza_height = pizzaBitmap.getHeight();


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

        if((int) (Math.random()*2) == 0){
            isChicken = true;
        } else{
            isChicken = false;
        }

        pathSetting();
    }
    public void draw(Canvas canvas){
        paint.setColor(Color.RED);

        if(isChicken) {
            if(iCurStep <= pathSegmentNum) {
                pathMeasure.getPosTan(pathSegmentLen * iCurStep, pos, tan);
                x = pos[0];
                y = pos[1];
                canvas.drawBitmap(chickenBitmap, x - chicken_width / 2, y - chicken_height / 2, new Paint());

                iCurStep++;
            }
        } else{
            if(iCurStep <= pathSegmentNum) {
                pathMeasure.getPosTan(pathSegmentLen * iCurStep, pos, tan);
                x = pos[0];
                y = pos[1];

                canvas.drawBitmap(pizzaBitmap, x-pizza_width/2, y - pizza_height/2, new Paint());

                iCurStep++;
            }
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

        pathSegmentNum = 1300;
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
    public float getWidth(){
        if(isChicken) return chicken_width;
        return pizza_width;
    }
    public float getHeight(){
        if(isChicken) return chicken_height;
        return pizza_height;
    }
    public void resetItem(float x, float y)
    {
        this.x = x;
        this.y = y;

        initialize();
        if(isChicken) isChicken = false;
        else isChicken = true;
    }
}
