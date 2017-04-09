package apps.ahqmrf.recapture.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class People implements Parcelable{
    private String name;
    private String avatar;
    private String relation;
    private String hashValue;

    public People(String name, String avatar, String relation, String hashValue) {
        this.name = name;
        this.avatar = avatar;
        this.relation = relation;
        this.hashValue = hashValue;
    }

    protected People(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        relation = in.readString();
        hashValue = in.readString();
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(relation);
        dest.writeString(hashValue);
    }
}
