package rish.crearo.cameraparse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import rish.crearo.cameraparse.utils.BasePath;


public class CapturePhotoActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

    Camera mCamera;
    SurfaceView mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        if (findFrontFacingCamera() < 0)
            mCamera = Camera.open();
        else
            mCamera = Camera.open(findFrontFacingCamera());

    }

    @Override
    public void onPause() {
        super.onPause();
//        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.startPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        Log.d("CAMERA", "Destroy");
    }

    public void onVideoClick(View v) {
        finish();
        startActivity(new Intent(CapturePhotoActivity.this, CaptureVideoActivity.class));
    }

    public void onSnapClick(View v) {
        mCamera.takePicture(this, null, null, this);
    }

    @Override
    public void onShutter() {
        Snackbar.make(findViewById(R.id.main_rellay), "Capture Successful.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Here, we chose internal storage
        try {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            int THUMBSIZE = 64;
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, THUMBSIZE, THUMBSIZE);

            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            thumbImage.compress(Bitmap.CompressFormat.PNG, 0, blob);
            byte[] thumbnailData = blob.toByteArray();

            String dir = BasePath.getBasePath(getApplicationContext());

            long currentTime = System.currentTimeMillis() / 1000;
            File file = new File(new File(dir), "p_" + currentTime + ".png");
            File fileThumb = new File(new File(dir + "/thumbnails/"), "p_" + currentTime + ".png");

            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();

            out = new FileOutputStream(fileThumb);
            out.write(thumbnailData);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.stopPreview();
        mCamera.release();
        startActivity(new Intent(CapturePhotoActivity.this, HomeActivity.class));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width, selected.height);
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW", "surfaceDestroyed");
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("t", "Camera found id = " + i);
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
}