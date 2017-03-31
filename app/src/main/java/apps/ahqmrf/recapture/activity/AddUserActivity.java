package apps.ahqmrf.recapture.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class AddUserActivity extends AppCompatActivity {

    private Button mBrowseBtn;
    private Button mAddUserBtn;
    private TextView selectedImagePath;
    private EditText userName, userRelation;
    private String path;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_browse:
                    openDefaultGallery();
                    break;
                case R.id.btn_add_user:
                    addUser();
                    break;
            }
        }
    };

    private void addUser() {
        if(userName.getText().toString().isEmpty()) {
            ToastMaker.showShortMessage(this,"Name is required");
            return;
        }
        if(userRelation.getText().toString().isEmpty()) {
            ToastMaker.showShortMessage(this, "Please specify the relation with you to this user");
            return;
        }
        People user = new People(userName.getText().toString(), path, userRelation.getText().toString());
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentExtras.PEOPLE, user);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void openDefaultGallery() {
        checkReadExternalStoragePermission();
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(AddUserActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(AddUserActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {

                    ActivityCompat.requestPermissions(AddUserActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(AddUserActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Constants.RequestCodes.GALLERY_BROWSE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, Constants.RequestCodes.GALLERY_BROWSE_REQ_CODE);

                } else {
                    ToastMaker.showShortMessage(this, "Permission required to select media");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCodes.GALLERY_BROWSE_REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                path = new SystemHelper(this).getRealPathFromUri(uri);
                selectedImagePath.setText(path);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        new SystemHelper(this).setupUI(findViewById(R.id.content_frame));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        prepareViews();
    }

    private void prepareViews() {
        mBrowseBtn = (Button) findViewById(R.id.btn_browse);
        mBrowseBtn.setOnClickListener(mOnClickListener);
        mAddUserBtn = (Button) findViewById(R.id.btn_add_user);
        mAddUserBtn.setOnClickListener(mOnClickListener);
        selectedImagePath = (TextView) findViewById(R.id.text_selected_photo);
        userName = (EditText) findViewById(R.id.edit_name);
        userRelation = (EditText) findViewById(R.id.edit_relation);
    }
}
