package com.jrt.customcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mShowPaths;
    private ImageView mIvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mShowPaths = (TextView)findViewById(R.id.tv_show_paths);
        mIvShow = (ImageView)findViewById(R.id.iv_show);
    }

    public void startCamera1(View view){
        Intent intent = new Intent(this,CustomCameraActivity.class);
        startActivity(intent);
    }
    public void startCamera2(View view){
        Intent intent = new Intent(this,AutoTakePicturesActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
             if (resultCode==RESULT_OK){
                  if (data!=null){
                      ArrayList<CharSequence> picturePaths = data.getCharSequenceArrayListExtra(Contast.PICTURE_PATHS);
                      String paths="连拍图片的路径如下：\n";
                      for (int i = 0; i <picturePaths.size() ; i++) {
                          paths+=picturePaths.get(i)+"\n";
                      }
                      String  path = picturePaths.get(0).toString();
                      Bitmap bitmap = BitmapFactory.decodeFile(path);
                      Matrix matrix = new Matrix();
                      matrix.setRotate(90);//默认是横屏的饿，旋转90度
                      Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
                      mIvShow.setImageBitmap(bitmap1);
                      mShowPaths.setText(paths);
                  }
             }
        }
    }
}
