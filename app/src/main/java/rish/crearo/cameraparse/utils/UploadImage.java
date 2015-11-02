package rish.crearo.cameraparse.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import rish.crearo.cameraparse.elements.HomeCardElement;

/**
 * Created by rish on 1/11/15.
 */
public class UploadImage extends AsyncTask<Void, Void, Void> {

    HomeCardElement element;
    Context context;
    UploadImageListener uploadImageListener;

    public UploadImage(Context context, HomeCardElement element, UploadImageListener listener) {
        this.element = element;
        this.context = context;
        this.uploadImageListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        System.out.println("Started upload of " + element.title);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(element.imagePath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        ParseFile file = new ParseFile(element.title, image);
        // Upload the image into Parse Cloud

        Log.d("T", "Created file");

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Completed partial upload of " + element.title);
                } else {
                    uploadImageListener.onUploadComplete(false);
                    System.out.println("Failed to upload of " + element.title);
                }
            }
        });

//        , new ProgressCallback() {
//            @Override
//            public void done(Integer percentDone) {
//                NotificationMaker.makeNotification(context, percentDone);
//                System.out.println("file Upload " + percentDone + "%");
//            }
//        }


        Log.d("T", "Creating imgupload");
        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("ImageUpload");
        Log.d("T", "Created imgupload");
        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", element.title);

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);

        Log.d("T", "Put file");

        Log.d("T", "Saving in back");
        // Create the class and the columns
        imgupload.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    uploadImageListener.onUploadComplete(true);
                } else {
                    uploadImageListener.onUploadComplete(false);
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public interface UploadImageListener {
        void onUploadComplete(boolean success);
    }
}