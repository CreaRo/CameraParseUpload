package rish.crearo.cameraparse;

//import android.app.Activity;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class MainActivity extends Activity implements SurfaceHolder.Callback {
//    Camera camera;
//    SurfaceView surfaceView;
//    SurfaceHolder surfaceHolder;
//    Camera.PictureCallback rawCallback;
//    Camera.ShutterCallback shutterCallback;
//    Camera.PictureCallback jpegCallback;
//    private final String tag = "MainActivity";
//
//    Button start, stop, capture;
//
//    /**
//     * Called when the activity is first created.
//     */
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        start = (Button) findViewById(R.id.buttonstart);
//        start.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View arg0) {
//                start_camera();
//            }
//        });
//        stop = (Button) findViewById(R.id.buttonstop);
//        capture = (Button) findViewById(R.id.buttoncapture);
//        stop.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View arg0) {
//                stop_camera();
//            }
//        });
//        capture.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                captureImage();
//            }
//        });
//
//        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        rawCallback = new Camera.PictureCallback() {
//            public void onPictureTaken(byte[] data, Camera camera) {
//                Log.d("Log", "onPictureTaken - raw");
//            }
//        };
//
//        /** Handles data for jpeg picture */
//        shutterCallback = new Camera.ShutterCallback() {
//            public void onShutter() {
//                Log.i("Log", "onShutter'd");
//            }
//        };
//        jpegCallback = new Camera.PictureCallback() {
//            public void onPictureTaken(byte[] data, Camera camera) {
//                FileOutputStream outStream = null;
//                try {
//                    outStream = new FileOutputStream(String.format(
//                            "/sdcard/%d.jpg", System.currentTimeMillis()));
//                    outStream.write(data);
//                    outStream.close();
//                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                }
//                Log.d("Log", "onPictureTaken - jpeg");
//            }
//        };
//    }
//
//    private void captureImage() {
//        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//    }
//
//    private void start_camera() {
//        try {
//            camera = Camera.open();
//        } catch (RuntimeException e) {
//            Log.e(tag, "init_camera: " + e);
//            return;
//        }
//        Camera.Parameters param;
//        param = camera.getParameters();
//        //modify parameter
//        param.setPreviewFrameRate(20);
//        param.setPreviewSize(176, 144);
//        camera.setParameters(param);
//        try {
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
//            //camera.takePicture(shutter, raw, jpeg)
//        } catch (Exception e) {
//            Log.e(tag, "init_camera: " + e);
//            return;
//        }
//    }
//
//    private void stop_camera() {
//        camera.stopPreview();
//        camera.release();
//    }
//
//    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
//
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
//}


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

    Camera mCamera;
    SurfaceView mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera = Camera.open();

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

    public void onCancelClick(View v) {
        finish();
    }

    public void onSnapClick(View v) {
        mCamera.takePicture(this, null, null, this);
    }

    @Override
    public void onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
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

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/cameraparse");
            File dirThumbs = new File(sdCard.getAbsolutePath() + "/cameraparse/thumbnails");
            dir.mkdirs();
            dirThumbs.mkdirs();
            long currentTime = System.currentTimeMillis() / 1000;
            File file = new File(dir, "p_" + currentTime + ".png");
            File fileThumb = new File(dirThumbs, "p_" + currentTime + ".png");

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
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
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


}
