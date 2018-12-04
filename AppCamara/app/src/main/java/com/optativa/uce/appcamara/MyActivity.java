package com.optativa.uce.appcamara;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class MyActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 100;
    private Permiso permission;
    Button captureButton;
    ImageView imageView;
    File destination;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        permission = new Permiso(this);
        captureButton = (Button)findViewById(R.id.capture);
        captureButton.setOnClickListener(listener);
        imageView = (ImageView)findViewById(R.id.image);
        destination = new File(Environment.getExternalStorageDirectory(), "image.jpg");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap userImage = BitmapFactory.decodeStream(in, null, options);
                imageView.setImageBitmap(userImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    permission.checkAll();
                    if (permission.storage()) {
                    try {
                        Uri destino = FileProvider.getUriForFile(MyActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider", destination);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, destino);
                        startActivityForResult(intent, REQUEST_IMAGE);
                    } catch (ActivityNotFoundException e) {
                    }
                }
            }
    };
}