package apps.ahqmrf.recapture.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class PeopleToTag implements Parcelable {
    private People people;
    private boolean selected;

    public PeopleToTag(People people, boolean selected) {
        this.people = people;
        this.selected = selected;
    }

    protected PeopleToTag(Parcel in) {
        people = in.readParcelable(People.class.getClassLoader());
        selected = in.readByte() != 0;
    }

    public static final Creator<PeopleToTag> CREATOR = new Creator<PeopleToTag>() {
        @Override
        public PeopleToTag createFromParcel(Parcel in) {
            return new PeopleToTag(in);
        }

        @Override
        public PeopleToTag[] newArray(int size) {
            return new PeopleToTag[size];
        }
    };

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(people, flags);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
