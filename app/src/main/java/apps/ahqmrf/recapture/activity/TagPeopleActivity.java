package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.PeopleListAdapter;
import apps.ahqmrf.recapture.adapter.PeopleToTagListAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.model.PeopleToTag;
import apps.ahqmrf.recapture.model.Time;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;
import apps.ahqmrf.recapture.util.ToastMaker;

public class TagPeopleActivity extends AppCompatActivity implements PeopleToTagListAdapter.TagCallback{

    private LinearLayout mAddNewUser;
    private TextView noUserText;
    private Database mDatabase;
    private ArrayList<People> peoples;
    private ArrayList<PeopleToTag> peoplesToTag;
    private RecyclerView mRecyclerPeopleList;
    private PeopleToTagListAdapter mAdapter;
    private ImageView mImageRefresh;
    private LinearLayout mLinearProgressBar;
    private TreeSet<Integer> peopleSet;
    private ArrayList<People> selectedPeople;
    private Button mCreateButton;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_people);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        memory = getIntent().getParcelableExtra(Constants.IntentExtras.MEMORY);

        mDatabase = new Database(this, null, null, 1);
        peopleSet = new TreeSet<>();
        mImageRefresh = (ImageView) findViewById(R.id.image_refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareViews();
                mLinearProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mLinearProgressBar.setVisibility(View.GONE);
                    }
                }, Constants.Basic.PROGRESS_BAR_DURATION);
            }
        });
        mCreateButton = (Button) findViewById(R.id.btn_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMemory();
            }
        });
        prepareViews();
    }

    private void createMemory() {
        selectedPeople = new ArrayList<>();
        for (Integer i : peopleSet) {
            selectedPeople.add(peoples.get(i));
        }
        memory.setPeoples(selectedPeople);
        Time time = new SystemHelper(this).getCurrentTime();
        memory.setTime(time);
        mDatabase.insertMemory(memory);
        //ToastMaker.showShortMessage(this, new SystemHelper(this).get12HourTimeStamp(time));
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onCheckBoxClick(int position, PeopleToTag peopleToTag) {
        if(peopleToTag.isSelected()) peopleSet.add(position);
        else peopleSet.remove(position);
    }

    private void prepareViews() {
        mLinearProgressBar = (LinearLayout) findViewById(R.id.linear_progressbar);
        peoples = mDatabase.getAllPeople();
        peoplesToTag = new ArrayList<>();
        for (People people : peoples) {
            peoplesToTag.add(new PeopleToTag(people, false));
        }
        noUserText = (TextView) findViewById(R.id.text_no_user);
        mAddNewUser = (LinearLayout) findViewById(R.id.linear_add_new_user);
        mAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUser();
            }
        });
        mRecyclerPeopleList = (RecyclerView) findViewById(R.id.recycler_people_list);
        if(peoples.size() > 0) {
            noUserText.setVisibility(View.GONE);
            mRecyclerPeopleList.setVisibility(View.VISIBLE);
            mRecyclerPeopleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mAdapter = new PeopleToTagListAdapter(this, this, peoplesToTag);
            mRecyclerPeopleList.setAdapter(mAdapter);
        } else {
            noUserText.setVisibility(View.VISIBLE);
            mRecyclerPeopleList.setVisibility(View.GONE);
        }
    }

    public void addNewUser() {
        startActivityForResult(new Intent(this, AddUserActivity.class), Constants.RequestCodes.ADD_USER_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.RequestCodes.ADD_USER_REQ && resultCode == RESULT_OK) {
            People people = data.getParcelableExtra(Constants.IntentExtras.PEOPLE);
            mDatabase.insertUser(people);
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

}
