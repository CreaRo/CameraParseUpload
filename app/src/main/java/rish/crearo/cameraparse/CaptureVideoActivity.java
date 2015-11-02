package rish.crearo.cameraparse;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rish.crearo.cameraparse.utils.BasePath;

public class CaptureVideoActivity extends Activity implements SurfaceHolder.Callback {

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private View mToggleButton;
    private boolean mInitSuccesful;
    private long currentTime = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);

        currentTime = System.currentTimeMillis() / 1000;

        // we shall take the video in landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mToggleButton = (ToggleButton) findViewById(R.id.toggleRecordingButton);
        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    mMediaRecorder.start();
                } else {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();

                    try {
                        initRecorder(mHolder.getSurface());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //save thumbnail here
                    String basePathThumb = BasePath.getBasePath(getApplicationContext()) + "/thumbnails";
                    String basePath = BasePath.getBasePath(getApplicationContext());
                    String VIDEO_THUMB_PATH_NAME = new File(basePathThumb, "v_" + currentTime) + ".png";
                    String VIDEO_PATH_NAME = new File(basePath, "v_" + currentTime) + ".mp4";

                    Log.d("VID", "PATH VIDEO = " + VIDEO_PATH_NAME);

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(VIDEO_PATH_NAME, MediaStore.Images.Thumbnails.MINI_KIND);
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    thumb.compress(Bitmap.CompressFormat.PNG, 0, blob);
                    byte[] thumbnailData = blob.toByteArray();

                    try {
                        FileOutputStream out = new FileOutputStream(VIDEO_THUMB_PATH_NAME);
                        out.write(thumbnailData);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentTime = System.currentTimeMillis() / 1000;
    }

    /* Init the MediaRecorder, the order the methods are called is vital to
         * its correct functioning */
    private void initRecorder(Surface surface) throws IOException {
        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
        if (mCamera == null) {
            if (findFrontFacingCamera() < 0)
                mCamera = Camera.open();
            else
                mCamera = Camera.open(findFrontFacingCamera());
            mCamera.unlock();
        }

        if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(640, 480);

        String basePath = BasePath.getBasePath(getApplicationContext());

        String VIDEO_PATH_NAME = new File(basePath, "v_" + currentTime) + ".mp4";
        mMediaRecorder.setOutputFile(VIDEO_PATH_NAME);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();

        // once the objects have been released they can't be reused
        mMediaRecorder = null;
        mCamera = null;
    }

    public void onCameraClick(View v) {
        finish();
        startActivity(new Intent(CaptureVideoActivity.this, CapturePhotoActivity.class));
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