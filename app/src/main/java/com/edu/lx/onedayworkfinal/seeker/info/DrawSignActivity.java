package com.edu.lx.onedayworkfinal.seeker.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
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
import com.edu.lx.onedayworkfinal.vo.WorkVO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class DrawSignActivity extends AppCompatActivity {

    private DrawView mDrawView = null;
    public static float mStrokeWidth = 5;

    public static int mStrokeColor = Color.BLACK;

    Bitmap bitmap;
    LinearLayout llCanvas;
    WorkVO workVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_sign);
        llCanvas = findViewById(R.id.llCanvas);
        //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
        if(savedInstanceState!=null) {
            // 화면전환 전에 넣어주었던 pointList를 꺼내서 세팅
            mDrawView.pointList = (ArrayList<DrawSignActivity.DrawView.Point>) savedInstanceState.getSerializable("list");
        }
        //그리기 뷰 초기화..
        mDrawView = new DrawView(this);
        //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
        llCanvas.addView(mDrawView);

        Intent intent = getIntent();
        String workVOStr = intent.getStringExtra("workVO");
        workVO = Base.gson.fromJson(workVOStr,WorkVO.class);

        Button commitSignButton = findViewById(R.id.commitSignButton);
        commitSignButton.setOnClickListener(v -> commitSign());
    }

    // 방향이 바뀔때 호출되는 메소드(자원 저장용 메소드)

    // Activity가 죽기전에 호출되는 메서드

    @Override

    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putSerializable("list", mDrawView.pointList);

    }

    private void commitSign() {
        String path = saveBitmap();
        requestUpdateCandidateSign(path);
    }

    private String saveBitmap() {
        mDrawView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(mDrawView.getDrawingCache());
        mDrawView.setDrawingCacheEnabled(false);

        String id = Base.sessionManager.getUserDetails().get("id");
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "/one_day_work/";
        Log.d("seekerId",id);
        String fileName = id + "_sign.png";
        String stringPath = exStorage + folderName;

        File filePath;
        String imagePath = stringPath + fileName;
        try {
            filePath = new File(stringPath);
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.close();

        }catch (FileNotFoundException exception) {
            Log.d("FileNotFoundException",exception.getMessage());
        }catch (IOException exception) {
            Log.d("IOException",exception.getMessage());
        }

        return imagePath;
    }

    private void requestUpdateCandidateSign(String path) {
        String seekerId = Base.sessionManager.getUserDetails().get("id");
        workVO.setJobName(seekerId);
        Log.d("workVO",workVO.toString());
        String url = getResources().getString(R.string.url) + "updateCandidateSign.do";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(
                Request.Method.POST, url,
                this::processUpdateResult,
                error -> {

                }
        );
        request.addFile("seekerSign",path);
        request.addMultipartParam("work","application/text",workVO.toString());
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processUpdateResult(String response) {
        int updateSignResult = Integer.parseInt(response);
        switch (updateSignResult) {
            case 0:
                Toast.makeText(getApplicationContext(),"서명 정보 업데이트에 실패하였습니다",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Intent intent = new Intent();
                intent.putExtra("signResult","resultOK");
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
    }

    private static class DrawView extends View implements View.OnTouchListener {

        float x;
        float y;
        public ArrayList<Point> pointList = new ArrayList<>();

        public DrawView(Context context) {
            super(context);
            setOnTouchListener(this);
            setFocusableInTouchMode(true);  // 이벤트가 계속해서 발생하기 위해
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(getResources().getColor(R.color.white,getContext().getTheme()));
            Paint paint = new Paint();

            if(pointList.size() < 2) return;
            for (int i=1; i<pointList.size(); i++) {
                if (pointList.get(i).draw) {
                    paint.setColor(getResources().getColor(R.color.black,getContext().getTheme()));
                    paint.setStrokeWidth(pointList.get(i).mStrokeWidth);
                    canvas.drawLine(pointList.get(i - 1).x,
                            pointList.get(i - 1).y, pointList.get(i).x,
                            pointList.get(i).y, paint);
                }
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = event.getX();
            y = event.getY();
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.d("phoro", "손가락으로 터치했음");
                    pointList.add(new Point(x,y,false,mStrokeWidth,mStrokeColor));
                    invalidate();         // 그림 다시 그리기
                    return true;                // 이벤트가 여기에서 끝난다.
                case MotionEvent.ACTION_MOVE:
                    //Log.d("phoro", "손가락으로 움직이는 중");
                    pointList.add(new Point(x,y,true,mStrokeWidth,mStrokeColor));
                    invalidate();         // 그림 다시 그리기
                    return true;
                case MotionEvent.ACTION_UP:
                    //Log.d("phoro", "손가락 땠음");
                default:
            }
            return false;

        }//end class DrawView



        static class Point implements Serializable {

            float x,y;

            boolean draw;

            float mStrokeWidth;

            int mStrokeColor;

            public Point(float x,float y,boolean draw,float mStrokeWidth,int mStrokeColor) {

                this.x = x;

                this.y = y;

                this.draw = draw;

                this.mStrokeColor = mStrokeColor;

                this.mStrokeWidth = mStrokeWidth;

            }



        }//end class Point

    }

}
