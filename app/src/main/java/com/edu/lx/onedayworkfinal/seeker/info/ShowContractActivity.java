package com.edu.lx.onedayworkfinal.seeker.info;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.WorkVO;

import java.io.IOException;

public class ShowContractActivity extends AppCompatActivity {

    WorkVO todayWorkVO;

    //웹뷰
    WebView webView;
    //웹뷰 세팅
    WebSettings webSettings;
    //pdf 보여줄 웹 뷰
    ImageView imageView;
    Button signContract;
    Button confirmButton;

    long downloadId;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contract);

        Intent intent = getIntent();
        String workVO = intent.getStringExtra("workVO");
        todayWorkVO = Base.gson.fromJson(workVO,WorkVO.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        webView = findViewById(R.id.webView);
        signContract = findViewById(R.id.signContract);
        signContract.setOnClickListener(v -> signStatement());
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> confirmContract());
        imageView = findViewById(R.id.imageView);
        //웹뷰 크롬 세팅
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        //웹뷰로부터 세팅 가져오기
        webSettings = webView.getSettings();
        //자바 스크립트 사용 허용
        webSettings.setJavaScriptEnabled(true); //자바 스크립트 사용
        webSettings.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            //다운로드 매니저 리퀘스트
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setMimeType(mimetype);
            request.addRequestHeader("User-Agent",userAgent);
            request.setTitle("근로계약서");
            request.setDescription("미서명된 근로계약서");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = downloadManager.enqueue(request);

            //end 다운로드 매니저 리퀘스트
        });
        //원하는 url 입력
        webView.loadUrl(getResources().getString(R.string.url)+"unsignedContract.do?candidateNumber="+todayWorkVO.getCandidateNumber());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    try {
                        ParcelFileDescriptor file = downloadManager.openDownloadedFile(downloadId);

                            PdfRenderer renderer = new PdfRenderer(file);

                            Bitmap mBitmap = Bitmap.createBitmap(800,1000,Bitmap.Config.ARGB_8888);
                            final int pageCount = renderer.getPageCount();

                            for(int i = 0 ; i < pageCount ; i ++ ) {
                                PdfRenderer.Page page = renderer.openPage(i);

                                //say we render for showing on the screen
                                page.render(mBitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                                //do stuff with the bitmap

                                //close the page
                                page.close();

                                imageView.setImageBitmap(mBitmap);
                                downloadManager.remove(downloadId);

                            }

                            //close the renderer
                            renderer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 사인하고 사인된 근로계약서 확인하고 확인 버튼 눌렀을때
     */
    private void confirmContract() {
        Intent intent = new Intent();
        intent.putExtra("signResult","resultOK");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /**
     * signStatement
     * 서명하기 창을 띄워준다.
     */
    private void signStatement() {
        Intent intent = new Intent(this,DrawSignActivity.class);
        intent.putExtra("workVO",todayWorkVO.toString());
        startActivityForResult(intent,601);
        //requestCommute(seekerId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            switch (requestCode) {
                case 601 :
                    String signResult = intent.getStringExtra("signResult");
                    if (TextUtils.equals(signResult,"resultOK")){
                        Snackbar.make(getWindow().getDecorView().getRootView(),"성공적으로 서명 정보가 업데이트 되었습니다",Snackbar.LENGTH_LONG).show();
                        signContract.setVisibility(View.GONE);
                        confirmButton.setVisibility(View.VISIBLE);
                        webView.loadUrl(getResources().getString(R.string.url)+"report.do?candidateNumber="+todayWorkVO.getCandidateNumber());
                    }
                    break;
            }
        }
    }


}
