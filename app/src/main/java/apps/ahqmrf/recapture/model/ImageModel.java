package apps.ahqmrf.recapture.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 3/31/2017.
 */

public class ImageModel implements Parcelable{
    private String filePath;
    private boolean selected;

    public ImageModel(String filePath, boolean selected) {
        this.filePath = filePath;
        this.selected = selected;
    }

    protected ImageModel(Parcel in) {
        filePath = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getFilePath() {
        return filePath;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
        dest.writeString(filePath);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
