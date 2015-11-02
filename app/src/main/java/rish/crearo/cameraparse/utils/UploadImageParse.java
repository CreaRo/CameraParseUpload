package rish.crearo.cameraparse.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import rish.crearo.cameraparse.elements.HomeCardElement;

/**
 * Created by rish on 2/11/15.
 */
public class UploadImageParse {

    public void saveInBack(final HomeCardElement element) {

        Bitmap bitmap = BitmapFactory.decodeFile(element.imagePath);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        System.out.println("Size of upload is " + (float) ((image.length)));

        ParseFile file = new ParseFile(element.title, image);

        final ParseObject imgupload = new ParseObject("ImageUpload");

        imgupload.put("ImageName", element.title);

        imgupload.put("ImageFile", file);

        Log.d("T", "Saving in back");

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Completed partial upload of " + element.title);
                    imgupload.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            System.out.println("Done imgupload to parse");
                            if (e != null)
                                e.printStackTrace();
                        }
                    });
                } else {
//                    uploadImageListener.onUploadComplete(false);
                    System.out.println("Failed to upload of " + element.title);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                System.out.println("file Upload " + percentDone + "%");
            }
        });
    }
}