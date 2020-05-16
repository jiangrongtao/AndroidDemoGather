package com.jrt.customcamera;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.jrt.customcamera.utils.PermisionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：自定义相机
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-15 16:16
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class CustomCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "CustomCameraActivity";
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private SurfaceHolder mSurfaceViewHolder;
    /**
     * 摄像头类型（前置/后置），默认后置
     */
    protected int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static final int TAKE_PHOTO_REQUEST_CODE = 2;
    private CustomCameraActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_custom_camera);
        mContext = this;
        PermisionUtils.verifyStoragePermissions(mContext);
        init();
    }

    private void init() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        //获取SurfaceHolder
        mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(this);
        //触摸屏幕完成对焦
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.autoFocus(null);
                //平滑的缩放：取值在0~mCamera.getParameters().getMaxZoom()
                mCamera.startSmoothZoom(2);
            }
        });
    }

    /**
     * 拍照
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startCapture(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setViewAnimator(view);
        }
        if (mCamera != null) {
            Log.i(TAG, "startCapture: 拍照");
            Camera.Parameters parameters = mCamera.getParameters();
            //设置拍照的图片格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            //设置图片的预览大小
            parameters.setPreviewSize(200, 300);
            //解决只能拍一张图片的bug
            setStartPreview(mCamera, mSurfaceViewHolder);
            //设置自动对焦
            parameters.setFocusMode(Camera.Parameters.ANTIBANDING_AUTO);
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        //动态对对焦成功后，获取拍摄的图片
                        if (success) {
                            Log.i(TAG, "startCapture: 对焦成功");
                            camera.takePicture(null, null, mPictureCallback);
                        }
                    }
                });
            } else {
                //前摄像头拍照
                mCamera.autoFocus(null);
                SystemClock.sleep(1000);//模拟对焦
                mCamera.takePicture(null, null, mPictureCallback);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setViewAnimator(View view) {
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, view.getWidth() / 2, view.getWidth() / 4);
        circularReveal.setInterpolator(new LinearInterpolator());
        circularReveal.setDuration(100);
        circularReveal.start();
    }

    /**
     * 拍摄成功后对图片的处理
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {


        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(Environment.getExternalStorageDirectory()
                            , getPhotoFileName());
                    Log.i(TAG, "onPictureTaken: " + file.getAbsolutePath());
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CustomCameraActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            Toast.makeText(CustomCameraActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                Toast.makeText(CustomCameraActivity.this, "SD不存在，图片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 获取Camera对象
     *
     * @return
     */
    private Camera getCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCamera = Camera.open();
        } else {
            mCamera = Camera.open(mCameraId);
        }
        return mCamera;
    }

    /**
     * 设置并且开启相机预览
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            //将Camera与SurfaceView开始绑定
            camera.setPreviewDisplay(holder);
            //调整拍摄的方向（默认横屏）
            camera.setDisplayOrientation(90);//旋转90度
            //开启预览
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放Camera资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);//取消回调
            stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 停止取景
     */
    private void stopPreview() {
        mCamera.stopPreview();
    }

    /**
     * 将Camera和Activity的生命周期绑定
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            if (mSurfaceViewHolder != null) {
                setStartPreview(mCamera, mSurfaceViewHolder);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                mCamera = getCamera();
                if (mSurfaceViewHolder != null) {
                    setStartPreview(mCamera, mSurfaceViewHolder);
                }
            } else {
                // Permission Denied
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }


    /**
     * SurfaceHolder 的回调处理
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceCreated: ");
        setStartPreview(mCamera, mSurfaceViewHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i(TAG, "surfaceChanged: ");
        stopPreview();//先停止取景，再重新打开
        setStartPreview(mCamera, mSurfaceViewHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceDestroyed: ");
        releaseCamera();
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /*****************************************摄像头切换************************************************/
    /**
     * 是否支持前置摄像头
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isSupportFrontCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            //2.3以下设备
            return false;
        }
        int numberOfCameras = Camera.getNumberOfCameras();
        if (2 == numberOfCameras) {
            return true;
        }
        return false;
    }

    /**
     * 切换摄像头
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void switchCamera(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setViewAnimator(view);
        }
        if (isFrontCamera()) {
            Log.i(TAG, "switchCamera: isFrontCamera()=" + isFrontCamera());
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        } else {
            if (isSupportFrontCamera()) {
                Log.i(TAG, "switchCamera: isSupportFrontCamera()=" + isSupportFrontCamera());
                switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                Toast.makeText(this, "当前设备不支持前摄像头", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void switchCamera(int cameraFacing) {
        mCameraId = cameraFacing;
        releaseCamera();
        mCamera = getCamera();
        setStartPreview(mCamera, mSurfaceViewHolder);
    }

    /**
     * 是否前置摄像头
     */
    public boolean isFrontCamera() {
        return mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

}

