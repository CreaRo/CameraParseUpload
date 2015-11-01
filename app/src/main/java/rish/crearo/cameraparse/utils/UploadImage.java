package rish.crearo.cameraparse.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

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
    RelativeLayout rellay;
    Context context;
    final ProgressDialog progress;

    public UploadImage(Context context, HomeCardElement element, RelativeLayout relativeLayout) {
        this.element = element;
        this.context = context;
        this.rellay = relativeLayout;
        progress = ProgressDialog.show(context, "Uploading file.", "Hold on while the image is being uploaded.", true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(element.imagePath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile(element.title + ".png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                    Snackbar.make(rellay, "Unsuccessful :(", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("ImageUpload");

        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", element.title);

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);


        // Create the class and the columns
        imgupload.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progress.dismiss();
                if (e == null) {
                    Snackbar.make(rellay, "Uploaded Successfully!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(rellay, "Uploaded Unsuccessful :(", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}