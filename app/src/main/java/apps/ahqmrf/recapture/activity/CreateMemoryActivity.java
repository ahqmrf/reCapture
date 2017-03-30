package apps.ahqmrf.recapture.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
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

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;

public class CreateMemoryActivity extends AppCompatActivity {

    private Button mNextBtn;
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

    }

    private void openGallery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.RequestCodes.GALLERY_BROWSE_REQ && resultCode == RESULT_OK) {
            // TODO
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        setupUI(findViewById(R.id.content_frame));


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle("Create a new memory");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(CreateMemoryActivity.this);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
