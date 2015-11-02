package rish.crearo.cameraparse.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by rish on 2/11/15.
 */
public class BasePath {

    public static String getBasePath(Context context) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/CameraParse");
        File dirThumbs = new File(sdCard.getAbsolutePath() + "/CameraParse/thumbnails");

        if (!dir.exists())
            dir.mkdirs();
        if (!dirThumbs.exists())
            dirThumbs.mkdirs();

        return dir.getAbsolutePath();
    }

    public static String getBasePathPhoto() {

        getBasePath(null);

        return "";
    }

    public static String PNG = "PNG";
    public static String JPG = "JPG";
    public static String PHOTO = "Photo";
    public static String VIDEO = "Video";


}