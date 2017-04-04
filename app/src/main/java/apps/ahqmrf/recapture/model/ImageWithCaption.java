package apps.ahqmrf.recapture.model;

/**
 * Created by Lenovo on 4/4/2017.
 */

public class ImageWithCaption {
    private String memoryTitle;
    private Time time;
    private String imageUri;
    private String caption;
    private boolean addCaption;

    public ImageWithCaption(String memoryTitle, Time time, String imageUri, String caption, boolean addCaption) {
        this.memoryTitle = memoryTitle;
        this.time = time;
        this.imageUri = imageUri;
        this.caption = caption;
        this.addCaption = addCaption;
    }

    public String getMemoryTitle() {
        return memoryTitle;
    }

    public void setMemoryTitle(String memoryTitle) {
        this.memoryTitle = memoryTitle;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isAddCaption() {
        return addCaption;
    }

    public void setAddCaption(boolean addCaption) {
        this.addCaption = addCaption;
    }
}
