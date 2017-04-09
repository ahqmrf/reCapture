package apps.ahqmrf.recapture.interfaces;

public interface TabFragmentCallback {
    void onAddButtonClick();
    void addNewUser();
    void onProfilePhotoClick(String path);
    void onNameClick();
    void onAboutClick();
    void onQuoteClick();
    void hideProgressBar();
    void onCameraClick();
}