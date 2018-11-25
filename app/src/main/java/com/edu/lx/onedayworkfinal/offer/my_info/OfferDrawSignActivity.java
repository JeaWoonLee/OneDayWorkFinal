package com.edu.lx.onedayworkfinal.offer.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.SimpleMultiPartRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class OfferDrawSignActivity extends AppCompatActivity {

    private DrawView mDrawView = null;
    public static float mStrokeWidth = 5;

    public static int mStrokeColor = Color.BLACK;

    public static int mBackColor = Color.WHITE;
    Bitmap bitmap;
    LinearLayout llCanvas;
    OfferVO offerVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_draw_sign);
        llCanvas = findViewById(R.id.llCanvas);

        if (savedInstanceState!=null){
            mDrawView.pointList = (ArrayList<OfferDrawSignActivity.DrawView.Point>) savedInstanceState.getSerializable("list");
        }
        mDrawView = new DrawView(this);
        llCanvas.addView(mDrawView);

        Intent intent = getIntent();
        String offerVoStr = intent.getStringExtra("offerVO");
        offerVO =Base.gson.fromJson(offerVoStr,OfferVO.class);

        Button commitSignButton = findViewById(R.id.commitSignButton);
        commitSignButton.setOnClickListener(v -> commitSign());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("list",mDrawView.pointList);
    }

    public void commitSign(){
        String path = saveBitmap();
        requestUpdateCandidateSign(path);

    }

    private String saveBitmap(){
        mDrawView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(mDrawView.getDrawingCache());
        mDrawView.setDrawingCacheEnabled(false);

        String id = Base.sessionManager.getUserDetails().get("id");
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "/one_day_work/";
        Log.d("offerId",id);
        String fileName = id + "_sign.png";
        String stringPath = exStorage + folderName;

        File filePath;
        String imagePath = stringPath + fileName;
        try{
            filePath = new File(stringPath);
            if (!filePath.isDirectory()){
                filePath.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.close();

        }catch (FileNotFoundException exception){
            Log.d("FileNotFoundException",exception.getMessage());
        }catch (IOException exception){
            Log.d("IOException",exception.getMessage());
        }
        return imagePath;
    }

    private void requestUpdateCandidateSign(String path){
        Log.d("offerVO",offerVO.toString());
        String url = getResources().getString(R.string.url)+"updateOfferSign.do";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(
                Request.Method.POST,url,
                this::processUpdateResult,
                error -> {

                });
        request.addFile("offerSign",path);
        request.addMultipartParam("offer","application/text",offerVO.toString());
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processUpdateResult(String response){
        int upadateSignResult = Integer.parseInt(response);
        switch (upadateSignResult){
            case 0:
                Toast.makeText(getApplicationContext(),"서명정보 업데이트에 실패하였습니다",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(getApplicationContext(),"성공적으로 서명 정보가 업데이트 되었습니다.",Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("signResult","ResultOK");
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    private static class DrawView extends View implements View.OnTouchListener{
        float x;
        float y;
        public ArrayList<Point> pointList = new ArrayList<>();

        public DrawView(Context context) {
            super(context);
            setOnTouchListener(this);
            setFocusableInTouchMode(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(getResources().getColor(R.color.white,getContext().getTheme()));
            Paint paint = new Paint();

            if (pointList.size() <2 ) return;
            for (int i=1 ; i<pointList.size(); i++) {
                if (pointList.get(i).draw) {
                    paint.setColor(getResources().getColor(R.color.black, getContext().getTheme()));
                    paint.setStrokeWidth(pointList.get(i).mStrokeWidth);
                    canvas.drawLine(pointList.get(i - 1).x, pointList.get(i - 1).y, pointList.get(i).x, pointList.get(i).y, paint);
                }
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = event.getX();
            y = event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pointList.add(new Point(x,y,false,mStrokeWidth,mStrokeColor));
                    invalidate();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    pointList.add(new Point(x,y,true,mStrokeWidth,mStrokeColor));
                    invalidate();
                    return true;

                case MotionEvent.ACTION_UP:
                    default:
            }
            return false;
        }

        static class Point implements Serializable{
            float x,y;
            boolean draw;
            float mStrokeWidth;
            int mStrokeColor;
            public Point(float x, float y, boolean draw, float mStrokeWidth, int mStrokeColor){
                this.x = x;
                this.y = y;
                this.draw = draw;
                this.mStrokeColor = mStrokeColor;
                this.mStrokeWidth = mStrokeWidth;
            }
        }
    }
}
