package apps.ahqmrf.recapture.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class CreateMemoryActivity extends AppCompatActivity {

    private Button mBrowseBtn;
    private TextView mSelectedAmountText;
    private ArrayList<String> imagePaths;
    private Button mNextBtn;
    private EditText mTitleEdit;
    private EditText mDescriptionEdit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_browse:
                    openGallery();
                    break;
                case R.id.btn_next:
                    proceed();
                    break;
            }
        }
    };

    private void proceed() {
        if (mTitleEdit.getText().toString().isEmpty()) {
            ToastMaker.showShortMessage(this, "A title is required for the memory");
            return;
        }

        Intent intent = new Intent(this, SelectIconActivity.class);
        intent.putStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA, imagePaths);
        intent.putExtra(Constants.IntentExtras.MEMORY_TITLE, mTitleEdit.getText().toString());
        intent.putExtra(Constants.IntentExtras.MEMORY_DESCRIPTION, mDescriptionEdit.getText().toString());

        startActivity(intent);
    }

    private void openGallery() {
        checkReadExternalStoragePermission();
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(CreateMemoryActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(CreateMemoryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {

                    ActivityCompat.requestPermissions(CreateMemoryActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(CreateMemoryActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {

            Intent intent = new Intent(this, GalleryActivity.class);
            startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, GalleryActivity.class);
                    startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);

                } else {
                    ToastMaker.showShortMessage(this, "Permission required to select media");
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCodes.GALLERY_BROWSE_REQ && resultCode == RESULT_OK) {
            imagePaths = data.getStringArrayListExtra(Constants.Basic.IMAGE_LIST_EXTRA);
            int amount = imagePaths.size();
            String text = amount > 1 ? "photos" : "photo";
            mSelectedAmountText.setText("Selected " + amount + " " + text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        new SystemHelper(this).setupUI(findViewById(R.id.content_frame));


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle("Create a new memory");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prepareViews();
    }

    private void prepareViews() {
        mBrowseBtn = (Button) findViewById(R.id.btn_browse);
        mBrowseBtn.setOnClickListener(mOnClickListener);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(mOnClickListener);
        mSelectedAmountText = (TextView) findViewById(R.id.text_selected_photo_amount);
        mTitleEdit = (EditText) findViewById(R.id.edit_title);
        mDescriptionEdit = (EditText) findViewById(R.id.edit_description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
