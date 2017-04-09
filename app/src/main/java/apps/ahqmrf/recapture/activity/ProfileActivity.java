package apps.ahqmrf.recapture.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.ToastMaker;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private People people;
    private ImageView mImageView, mCameraImage;
    private TextView mAbout, mRelation, mUsernameBig, mUsernameSmall;
    private String imageUri;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database = new Database(this, null, null, 1);
        people = getIntent().getParcelableExtra(Constants.IntentExtras.PEOPLE);
        imageUri = people.getAvatar();

        prepareViews();
    }

    private void prepareViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mImageView = (ImageView) findViewById(R.id.image_profile);
        mImageView.setOnClickListener(this);
        mCameraImage = (ImageView) findViewById(R.id.image_camera);
        mCameraImage.setOnClickListener(this);
        mAbout = (TextView) findViewById(R.id.text_about);
        mUsernameSmall = (TextView) findViewById(R.id.text_username);
        mUsernameBig = (TextView) findViewById(R.id.text_name);
        mRelation = (TextView) findViewById(R.id.text_relation);

        setValuesToViews();

    }

    private void setValuesToViews() {
        showImageProfile(imageUri);
        mUsernameBig.setText(people.getName());
        mUsernameSmall.setText(people.getName());
        mRelation.setText(people.getRelation());
    }

    private void showImageProfile(String path) {
        ImageLoader.getInstance().displayImage(
                "file://" + path,
                mImageView,
                MyDisplayImageOptions.getInstance().getDisplayImageOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setProgress(0);
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        linearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        linearLayout.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_profile:
                onProfilePhotoClick(people.getAvatar());
                break;
            case R.id.image_camera:
                onCameraClick();
                break;
        }
    }

    private void onCameraClick() {
        openDefaultGallery();
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {

            Intent intent = new Intent(this, SingleImageSelectionActivity.class);
            startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, SingleImageSelectionActivity.class);
                    startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);

                } else {
                    ToastMaker.showShortMessage(this, "Permission required to select media");
                }
            }
        }
    }


    private void openDefaultGallery() {
        checkReadExternalStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCodes.GALLERY_BROWSE_REQ && resultCode == RESULT_OK) {
            String path = data.getStringExtra(Constants.Basic.IMAGE_EXTRA);
            showImageProfile(path);
            database.remove(people);
            people.setAvatar(path);
            database.insertUser(people);

            // TODO: 4/9/2017

        }
    }

    private void onProfilePhotoClick(String path) {
        if(path == null) return;
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        onImageClick(paths, 0);
    }

    public void onImageClick(ArrayList<String> paths, int position) {
        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA, paths);
        intent.putExtra(Constants.IntentExtras.POSITION, position);
        startActivity(intent);
    }
}
