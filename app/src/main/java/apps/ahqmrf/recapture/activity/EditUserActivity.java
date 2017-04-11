package apps.ahqmrf.recapture.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;

public class EditUserActivity extends AppCompatActivity {

    private People people;
    private ImageView mImageView;
    private SystemHelper mHelper;
    private Database mDatabase;
    private EditText mUsername, mAbout, mRelation;
    private Button saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mHelper = new SystemHelper(this);
        mDatabase = new Database(this, null, null, 1);
        people = getIntent().getParcelableExtra(Constants.IntentExtras.PEOPLE);
        prepareViews();
    }

    private void prepareViews() {
        mHelper.setupUI(findViewById(R.id.scrollView));
        mImageView = (ImageView) findViewById(R.id.image_profile);
        if(people.getAvatar() != null) mImageView.setImageBitmap(BitmapFactory.decodeFile(people.getAvatar()));
        mUsername = (EditText) findViewById(R.id.edit_name);
        mRelation = (EditText) findViewById(R.id.edit_relation);
        mAbout = (EditText) findViewById(R.id.edit_about);

        if(people.getName() != null) mUsername.setText(people.getName());
        if(people.getRelation() != null) mRelation.setText(people.getRelation());
        if(people.getAbout() != null) mAbout.setText(people.getAbout());

        saveChanges = (Button) findViewById(R.id.btn_save);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllChanges();
            }
        });
    }

    private void saveAllChanges() {
        String name = mUsername.getText().toString();
        String relation = mRelation.getText().toString();
        String about = mAbout.getText().toString();

        people.setName(name);
        people.setRelation(relation);
        people.setAbout(about);

        mDatabase.updatePeople(people);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
