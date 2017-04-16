package apps.ahqmrf.recapture.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class PasswordActivity extends AppCompatActivity {

    private EditText old, _new, confirm;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new SystemHelper(this).setupUI(findViewById(R.id.pass_layout));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        old = (EditText) findViewById(R.id.edit_old);
        _new = (EditText) findViewById(R.id.edit_new);
        confirm = (EditText) findViewById(R.id.edit_confirm);

        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
    }

    private void savePassword() {
        SharedPreferences preferences = getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String passwordStr = preferences.getString(Constants.Basic.PASSWORD, "");

        if (passwordStr.equals(old.getText().toString())) {
            if (_new.getText().toString().isEmpty()) {
                ToastMaker.showShortMessage(this, "Password cannot be empty");
                return;
            }
            if (!_new.getText().toString().equals(confirm.getText().toString())) {
                ToastMaker.showShortMessage(this, "Passwords do not match");
                return;
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.Basic.PASSWORD, _new.getText().toString());
            editor.apply();
            ToastMaker.showShortMessage(this, "Password updated");
            finish();
        } else {
            ToastMaker.showShortMessage(this, "Wrong password");
        }
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
