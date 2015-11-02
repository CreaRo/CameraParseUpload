package rish.crearo.cameraparse.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import junit.framework.Assert;

import net.bozho.easycamera.EasyCamera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import rish.crearo.cameraparse.elements.HomeCardElement;

/**
 * Created by rish on 2/11/15.
 */
public class UploadImageParse extends AsyncTask<Void, Void, Void> {

    Context context;
    HomeCardElement element;

    int previousPercentage = 0;

    public UploadImageParse(Context context, HomeCardElement element) {
        this.context = context;
        this.element = element;
    }

    public void uploadInBackground(final HomeCardElement element, final Context context) {

        Bitmap bitmap = BitmapFactory.decodeFile(element.imagePath);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        System.out.println("Size of upload is " + (float) ((image.length)));

        ParseFile file = new ParseFile(element.title, image);

        final ParseObject imgUpload = new ParseObject("ImageUpload");

        imgUpload.put("ImageName", element.title);

        imgUpload.put("ImageFile", file);

        Log.d("T", "Saving in back");

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Completed file upload of " + element.title);
                    imgUpload.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            System.out.println("Done object upload to parse");
                            if (e != null) {
                                e.printStackTrace();
                                NotificationMaker.createNotification(context, false);
                            } else {
                                NotificationMaker.createNotification(context, true);
                            }
                        }
                    });
                } else {
                    NotificationMaker.createNotification(context, false);
                    System.out.println("Failed to upload of " + element.title);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                if (percentDone - previousPercentage >= 3) {
                    NotificationMaker.creatStickyNotification(context, percentDone);
                    System.out.println("file Upload " + percentDone + "%");
                    previousPercentage = percentDone;
                }
                if (percentDone >= 98) {
                    NotificationMaker.creatStickyNotification(context, percentDone);
                    System.out.println("file Upload " + percentDone + "%");
                }
            }
        });
    }

    public void uploadInBackgroundVideo(final HomeCardElement element, final Context context) {

        byte video[] = getBytesOfVideo(element);

        if (video == null) {
            System.out.println("Video size is null");
            return;
        }
        if (video.length >= 9.5 * 1024 * 1024) {
            System.out.println("Failed to create because file too big.");
            NotificationMaker.createNotification(context, false);
            return;
        }

        System.out.println("Size of upload is " + (float) ((video.length)));

        ParseFile file = new ParseFile(element.title, video);

        final ParseObject vidUpload = new ParseObject("VideoUpload");

        vidUpload.put("VideoName", element.title);

        vidUpload.put("VideoFile", file);

        Log.d("T", "Saving in back");

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Completed file upload of " + element.title);
                    vidUpload.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            System.out.println("Done object upload to parse");
                            if (e != null) {
                                e.printStackTrace();
                                NotificationMaker.createNotification(context, false);
                            } else {
                                NotificationMaker.createNotification(context, true);
                            }
                        }
                    });
                } else {
                    NotificationMaker.createNotification(context, false);
                    System.out.println("Failed to upload of " + element.title);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                if (percentDone - previousPercentage >= 3) {
                    NotificationMaker.creatStickyNotification(context, percentDone);
                    System.out.println("file Upload " + percentDone + "%");
                    previousPercentage = percentDone;
                }
                if (percentDone >= 98) {
                    NotificationMaker.creatStickyNotification(context, percentDone);
                    System.out.println("file Upload " + percentDone + "%");
                }
            }
        });
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (element.imagePath.contains(".png") || element.imagePath.contains(".jpg")) {
            uploadInBackground(element, context);
        } else {
            uploadInBackgroundVideo(element, context);
        }

        return null;
    }

    private byte[] getBytesOfVideo(HomeCardElement element) {

        try {
            System.out.println("Absolute Path = " + new File(element.imagePath).getAbsolutePath().toString());
            InputStream is = new FileInputStream(new File(element.imagePath).getAbsolutePath().toString());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte data[] = new byte[1024];
            int bytesRead = is.read(data);
            while ((bytesRead) != -1) {
                bos.write(data, 0, bytesRead);
                bytesRead = is.read(data);
            }
            return bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}