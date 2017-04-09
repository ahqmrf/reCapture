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
    private String iconPath;
    private ArrayList<String> images;
    private ArrayList<People> peoples;
    private Time time;
    private boolean special;

    public Memory() {}

    public Memory(String title, String description, String iconPath, ArrayList<String> images, ArrayList<People> peoples, Time time, boolean special) {
        this.title = title;
        this.description = description;
        this.iconPath = iconPath;
        this.images = images;
        this.peoples = peoples;
        this.time = time;
        this.special = special;
    }

    protected Memory(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        iconPath = in.readString();
        images = in.createStringArrayList();
        peoples = in.createTypedArrayList(People.CREATOR);
        time = in.readParcelable(Time.class.getClassLoader());
        special = in.readByte() != 0;
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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<People> getPeoples() {
        return peoples;
    }

    public void setPeoples(ArrayList<People> peoples) {
        this.peoples = peoples;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
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
        dest.writeString(iconPath);
        dest.writeStringList(images);
        dest.writeTypedList(peoples);
        dest.writeParcelable(time, flags);
        dest.writeByte((byte) (special ? 1 : 0));
    }
}
