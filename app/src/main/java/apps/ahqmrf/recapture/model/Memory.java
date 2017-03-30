package apps.ahqmrf.recapture.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class Memory implements Parcelable{
    private int id;
    private String title;
    private String description;
    private String timeStamp;
    private String date;
    private Uri icon;
    private ArrayList<Uri> photos;
    private ArrayList<People> peoples;

    public Memory() {}

    public Memory(String title, String description, Time time, Uri icon, ArrayList<Uri> photos, ArrayList<People> peoples) {
        this.title = title;
        this.description = description;
        this.timeStamp = time.getTimeStamp();
        this.date = time.getDate();
        this.icon = icon;
        this.photos = photos;
        this.peoples = peoples;
    }

    protected Memory(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        timeStamp = in.readString();
        date = in.readString();
        icon = in.readParcelable(Uri.class.getClassLoader());
        photos = in.createTypedArrayList(Uri.CREATOR);
        peoples = in.createTypedArrayList(People.CREATOR);
    }

    public static final Creator<Memory> CREATOR = new Creator<Memory>() {
        @Override
        public Memory createFromParcel(Parcel in) {
            return new Memory(in);
        }

        @Override
        public Memory[] newArray(int size) {
            return new Memory[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    public ArrayList<Uri> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Uri> photos) {
        this.photos = photos;
    }

    public ArrayList<People> getPeoples() {
        return peoples;
    }

    public void setPeoples(ArrayList<People> peoples) {
        this.peoples = peoples;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(timeStamp);
        dest.writeString(date);
        dest.writeParcelable(icon, flags);
        dest.writeTypedList(photos);
        dest.writeTypedList(peoples);
    }
}
