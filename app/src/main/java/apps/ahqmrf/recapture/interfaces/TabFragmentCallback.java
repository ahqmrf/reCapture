package apps.ahqmrf.recapture.interfaces;

public interface TabFragmentCallback {
    void onAddButtonClick();
    void addNewUser();
    void onProfilePhotoClick(String path);
    void hideProgressBar();
    void onCameraClick();
    void onPasswordClick();
}