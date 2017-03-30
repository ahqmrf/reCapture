package apps.ahqmrf.recapture.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class People implements Parcelable{
    private String name;
    private Uri avatar;
    private String relation;

    public People(String name, Uri avatar, String relation) {
        this.name = name;
        this.avatar = avatar;
        this.relation = relation;
    }

    protected People(Parcel in) {
        name = in.readString();
        avatar = in.readParcelable(Uri.class.getClassLoader());
        relation = in.readString();
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(avatar, flags);
        dest.writeString(relation);
    }
}
