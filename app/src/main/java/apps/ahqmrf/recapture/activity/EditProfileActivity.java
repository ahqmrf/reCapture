package apps.ahqmrf.recapture.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mName, mAbout, mQuote;
    private Button mSaveChangeBtn;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new SystemHelper(this).setupUI(findViewById(R.id.layout_edit_profile));

        preferences = getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        mName = (EditText) findViewById(R.id.edit_username);
        mAbout = (EditText) findViewById(R.id.edit_about);
        mQuote = (EditText) findViewById(R.id.edit_quote);
        mSaveChangeBtn = (Button) findViewById(R.id.btn_save);
        mSaveChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                String name = mName.getText().toString();
                editor.putString(Constants.Basic.USERNAME, name.length() == 0? null : name);
                String about = mAbout.getText().toString();
                editor.putString(Constants.Basic.ABOUT, about.length() == 0? null : about);
                String quote = mQuote.getText().toString();
                editor.putString(Constants.Basic.FAVORITE_QUOTE, quote.length() == 0? null : quote);
                editor.apply();
                ToastMaker.showShortMessage(getApplicationContext(), "Changes saved!");
                finish();
            }
        });

        String name = preferences.getString(Constants.Basic.USERNAME, null);
        String quote = preferences.getString(Constants.Basic.FAVORITE_QUOTE, null);
        String about = preferences.getString(Constants.Basic.ABOUT, null);

        if(name != null) mName.setText(name);
        if(quote != null) mQuote.setText(quote);
        if(about != null) mAbout.setText(about);
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
