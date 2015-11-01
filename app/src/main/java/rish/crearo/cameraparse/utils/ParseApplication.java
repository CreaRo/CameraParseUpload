package rish.crearo.cameraparse.utils;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

    private static final String YOUR_APPLICATION_ID = "toEO8v1qAg9toJdCZAv8HkndZ8fAofiNtk9Llqvf";
    private static final String YOUR_CLIENT_KEY = "SpfuI0jJm31PqOJn4eibQqXvHEcBCWZLfgBNP80Z";

    @Override
    public void onCreate() {
        super.onCreate();


        // Add your initialization code here
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
