package rish.crearo.cameraparse.elements;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rish on 1/11/15.
 */
public class HomeCardElement implements Parcelable{

    public String title, imagePath, thumbnailPath;

    public HomeCardElement(String title, String imagePath, String thumbnailPath) {
        this.title = title;
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
    }

    protected HomeCardElement(Parcel in) {
        title = in.readString();
        imagePath = in.readString();
        thumbnailPath = in.readString();
    }

    public static final Creator<HomeCardElement> CREATOR = new Creator<HomeCardElement>() {
        @Override
        public HomeCardElement createFromParcel(Parcel in) {
            return new HomeCardElement(in);
        }

        @Override
        public HomeCardElement[] newArray(int size) {
            return new HomeCardElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imagePath);
        parcel.writeString(thumbnailPath);
    }
}
