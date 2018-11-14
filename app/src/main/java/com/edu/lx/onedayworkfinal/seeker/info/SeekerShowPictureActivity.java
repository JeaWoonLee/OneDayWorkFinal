package com.edu.lx.onedayworkfinal.seeker.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;

public class SeekerShowPictureActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_show_picture);

        Intent intent = getIntent();
        String bitmapStr = intent.getStringExtra("bitmap");
        String imgUrl = intent.getStringExtra("imgUrl");
        imageView = findViewById(R.id.image);
        if (bitmapStr != null){
            byte[] encodeByte = Base64.decode(bitmapStr,Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
            imageView.setImageBitmap(bitmap);
        } else if (imgUrl != null) {
            requestServerImage(imgUrl);
        }

    }

    private void requestServerImage(String imgUrl) {
        String url = getResources().getString(R.string.url) + imgUrl;
        ImageRequest request = new ImageRequest(
                url,
                this::showImage,
                100,
                100,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                error -> { });
        Base.requestQueue.add(request);
    }

    private void showImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
