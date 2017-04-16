package apps.ahqmrf.recapture.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class CreateMemoryActivity extends AppCompatActivity {

    private Button mBrowseBtn;
    private TextView mSelectedAmountText;
    private ArrayList<String> imagePaths;
    private Button mNextBtn, mBtnSetDate, mBtnSetTime;
    private EditText mTitleEdit, mEditDate, mEditTime;
    private EditText mDescriptionEdit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String date, time;
    private Calendar c = Calendar.getInstance();

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
                case R.id.btn_set_date:
                    setDate();
                    break;
                case R.id.btn_set_time:
                    setTime();
                    break;
                case R.id.edit_date:
                    setDate();
                    break;
                case R.id.edit_time:
                    setTime();
                    break;
            }
        }
    };

    private void setTime() {
        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String str = "AM";
                        if(hourOfDay > 12) {
                            hourOfDay -= 12;
                            str = "PM";
                        }

                        time = (hourOfDay < 10? "0" : "") + hourOfDay + ":" + (minute < 10? "0" : "") + minute + " " + str;
                        mEditTime.setText(time);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void setDate() {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                        mEditDate.setText(date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void proceed() {
        if (mTitleEdit.getText().toString().isEmpty()) {
            ToastMaker.showShortMessage(this, "A title is required for the memory");
            return;
        }

        Intent intent = new Intent(this, SelectIconActivity.class);
        intent.putStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA, imagePaths);
        intent.putExtra(Constants.IntentExtras.MEMORY_TITLE, mTitleEdit.getText().toString());
        intent.putExtra(Constants.IntentExtras.MEMORY_DESCRIPTION, mDescriptionEdit.getText().toString());
        intent.putExtra(Constants.IntentExtras.HAPPENED_DATE, date);
        intent.putExtra(Constants.IntentExtras.HAPPENED_TIME, time);

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
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        String str = "AM";
        if(mHour > 12) {
            mHour -= 12;
            str = "PM";
        }

        time = (mHour < 10? "0" : "") + mHour + ":" + (mMinute < 10? "0" : "") + mMinute + " " + str;

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        date = mYear + "/" + mMonth + "/" + mDay;


        mEditDate = (EditText) findViewById(R.id.edit_date);
        mEditDate.setText(date);
        mEditDate.setOnClickListener(mOnClickListener);
        mEditTime = (EditText) findViewById(R.id.edit_time);
        mEditTime.setText(time);
        mEditTime.setOnClickListener(mOnClickListener);
        mBtnSetDate = (Button) findViewById(R.id.btn_set_date);
        mBtnSetDate.setOnClickListener(mOnClickListener);
        mBtnSetTime = (Button) findViewById(R.id.btn_set_time);
        mBtnSetTime.setOnClickListener(mOnClickListener);
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
