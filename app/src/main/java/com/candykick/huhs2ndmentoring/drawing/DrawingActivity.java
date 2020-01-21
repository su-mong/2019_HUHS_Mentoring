package com.candykick.huhs2ndmentoring.drawing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityDrawingBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DrawingActivity extends BaseActivity<ActivityDrawingBinding> {

    DrawingView drawingView;
    ArrayList<Point> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        drawingView = new DrawingView(DrawingActivity.this);
        binding.drawingContainer.addView(drawingView);
    }

    public void changePen() {
        drawingView.setToPen();
    }
    public void changeEraser() {
        drawingView.setToEraser();
    }

    public void changeRed() { drawingView.color = getResources().getColor(R.color.colorRed); }
    public void changeOrange() { drawingView.color = getResources().getColor(R.color.colorOrange); }
    public void changeYellow() { drawingView.color = getResources().getColor(R.color.colorYellow); }
    public void changeVividGreen() { drawingView.color = getResources().getColor(R.color.colorVividGreen); }
    public void changeGreen() { drawingView.color = getResources().getColor(R.color.colorGreen); }
    public void changeBlue() { drawingView.color = getResources().getColor(R.color.colorBlue); }
    public void changeCobaltBlue() { drawingView.color = getResources().getColor(R.color.colorCobaltBlue); }
    public void changeViolet() { drawingView.color = getResources().getColor(R.color.colorViolet); }
    public void changePink() { drawingView.color = getResources().getColor(R.color.colorPink); }
    public void changeOak() { drawingView.color = getResources().getColor(R.color.colorOak); }
    public void changeOchre() { drawingView.color = getResources().getColor(R.color.colorOchre); }
    public void changeBlack() { drawingView.color = getResources().getColor(R.color.colorBlack); }

    public void newFile() {
        points.clear();
        drawingView.invalidate();
        binding.drawingContainer.removeAllViews();
        binding.drawingContainer.addView(drawingView);
    }

    public void saveImage() {
        /*Bitmap captureView = Bitmap.createBitmap(binding.drawingContainer.getMeasuredWidth(), binding.drawingContainer.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas screenShotCanvas = new Canvas(captureView);
        binding.drawingContainer.draw(screenShotCanvas);*/

        binding.drawingContainer.setDrawingCacheEnabled(true);
        binding.drawingContainer.buildDrawingCache();
        Bitmap screenshot = binding.drawingContainer.getDrawingCache();

        // 현재 날짜로 파일을 저장하기
        // 이미지 파일 이름: HUHS_{시간}
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "HUHS_"+timeStamp+".png";
        try {
            File file = new File(Environment.getExternalStorageDirectory(), imageFileName);
            if(file.createNewFile())
                Log.d("save", "파일 생성 성공");
            OutputStream outStream = new FileOutputStream(file);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            // 갤러리에 변경을 알려줌
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 안드로이드 버전이 Kitkat 이상 일때
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                DrawingActivity.this.sendBroadcast(mediaScanIntent);
            } else {
                DrawingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }

            Toast.makeText(DrawingActivity.this.getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_drawing; }


    class Point{
        float x;
        float y;
        boolean check;
        int color;

        public Point(float x, float y, boolean check,int color)
        {
            this.x = x;
            this.y = y;
            this.check = check;
            this.color = color;
        }
    }

    public class DrawingView extends View {
        Paint mPenPaint;
        Paint mTransPaint;

        PorterDuffXfermode clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        Bitmap mBitmap1;
        Canvas mCanvas1;

        Bitmap mBitmap2;
        Canvas mCanvas2;

        Paint mBitmapPaint;

        float x;
        float y;

        int color;

        Path mPath = new Path();

        public DrawingView(Context context) {
            super(context);

            this.mPenPaint = new Paint(Paint.DEV_KERN_TEXT_FLAG);
            this.mPenPaint.setAntiAlias(true);
            this.mPenPaint.setColor(Color.BLUE);
            this.mPenPaint.setStrokeWidth(5f);
            this.mPenPaint.setStyle(Paint.Style.STROKE);

            this.mTransPaint = new Paint(Paint.DEV_KERN_TEXT_FLAG);
            this.mTransPaint.setAntiAlias(true);
            this.mTransPaint.setColor(Color.TRANSPARENT);
            this.mTransPaint.setStrokeWidth(5f);
            this.mTransPaint.setStyle(Paint.Style.STROKE);

            mPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap1 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

            mCanvas1 = new Canvas(mBitmap1);
            mCanvas2 = new Canvas(mBitmap2);
        }

        public void setToPen(){
            mPenPaint.setXfermode(null);
        }

        public void setToEraser(){
            mPenPaint.setXfermode(clear);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap2,0,0, mBitmapPaint);
            canvas.drawBitmap(mBitmap1,0,0, mBitmapPaint);
            canvas.drawPath(mPath,mTransPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mPenPaint.setColor(color);

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                x = event.getX(0);
                y = event.getY(0);
                mPath.moveTo(x, y);
                return true;
            } else if(event.getAction() == MotionEvent.ACTION_MOVE){
                x = event.getX(0);
                y = event.getY(0);
                mPath.lineTo(x, y);
                mCanvas1.drawPath(mPath, mPenPaint);
                invalidate();
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                mCanvas1.drawPath(mPath, mPenPaint);
                mPath.reset();
                invalidate();
            }

            return super.onTouchEvent(event);
        }
    }

    /*class DrawingView extends View
    {
        Paint p = new Paint();

        public DrawingView(Context context) { super(context); }

        public void changePen() {
            p.setXfermode(null);
        }
        public void changeEraser() {
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            p.setStrokeWidth(5);
            for(int i=1 ; i<points.size() ; i++)
            {
                p.setColor(points.get(i).color);
                if(!points.get(i).check)
                    continue;
                canvas.drawLine(points.get(i-1).x,points.get(i-1).y,points.get(i).x,points.get(i).y,p);
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    points.add(new Point(x,y,false , color));
                case MotionEvent.ACTION_MOVE :
                    points.add(new Point(x,y,true , color));
                    break;
                case MotionEvent.ACTION_UP :
                    break;
            }
            invalidate();
            return true;
        }
    }*/
}

/*
OutputStream outputStream = null;

        try {

            // 이미지가 저장될 폴더 이름: startup
            File storageDir = new File(Environment.getExternalStorageDirectory());
            if(!storageDir.exists())
                storageDir.mkdirs();
            // 빈 파일 생성
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);

            outputStream = new FileOutputStream(image);
            drawingView.buildDrawingCache();
            Bitmap bitmap = drawingView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            makeToast("저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            makeToast("오류가 발생했습니다: "+e.getMessage());
        } finally {
            /*try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                makeToast("오류가 발생했습니다: "+e.getMessage());
            }
        }
 */
