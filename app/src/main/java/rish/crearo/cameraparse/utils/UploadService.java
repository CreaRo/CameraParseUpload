package rish.crearo.cameraparse.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import rish.crearo.cameraparse.elements.HomeCardElement;

/**
 * Created by rish on 2/11/15.
 */
public class UploadService extends Service {

    private HomeCardElement element;

    public UploadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        System.out.println("Starting upload");
        element = intent.getExtras().getParcelable("element");
        System.out.println("uploading " + element.title);
        uploadImageToServer();
    }

    @Override
    public void onDestroy() {
    }

    private void uploadImageToServer() {
        Toast.makeText(getApplicationContext(), "Uploading " + element.title, Toast.LENGTH_LONG).show();
//        new UploadImage(getApplicationContext(), element, new UploadImage.UploadImageListener() {
//            @Override
//            public void onUploadComplete(boolean success) {
//                NotificationMaker.clearNotification(getApplicationContext(), success);
//                System.out.println("Uploading complete, success = " + success);
//            }
//        }).execute();



    }
}