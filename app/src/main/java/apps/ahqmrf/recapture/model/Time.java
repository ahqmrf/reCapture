package apps.ahqmrf.recapture.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class Time implements Parcelable{
    private String timeStamp;
    private String date;

    public Time(String timeStamp, String date) {
        this.timeStamp = timeStamp;
        this.date = date;
    }

    protected Time(Parcel in) {
        timeStamp = in.readString();
        date = in.readString();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timeStamp);
        dest.writeString(date);
    }
}
