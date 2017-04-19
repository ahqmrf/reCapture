package apps.ahqmrf.recapture.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.ToastMaker;

public class SplashActivity extends AppCompatActivity {

    private EditText password;
    private Button enter;
    private String passwordStr;
    private ImageView key;
    private View layoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        layoutProgress = findViewById(R.id.linear_progressbar);

        SharedPreferences preferences = getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean lockMode = preferences.getBoolean(Constants.Basic.LOCK_MODE, false);
        passwordStr = preferences.getString(Constants.Basic.PASSWORD, "");
        if(!lockMode) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            password = (EditText) findViewById(R.id.edit_pass);
            enter = (Button) findViewById(R.id.btn_enter);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(passwordStr.equals(password.getText().toString())) {
                        go();
                    } else {
                        ToastMaker.showShortMessage(getApplicationContext(), "Wrong password!");
                    }
                }
            });

            key = (ImageView) findViewById(R.id.image_key);
            key.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {

                    switch ( event.getAction() ) {
                        case MotionEvent.ACTION_DOWN:
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case MotionEvent.ACTION_UP:
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void go() {
        layoutProgress.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                layoutProgress.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, Constants.Basic.SPLASH_SCREEN_DURATION);
    }
}
