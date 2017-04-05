package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.adapter.SimpleFragmentPagerAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.fragment.GalleryFragment;
import apps.ahqmrf.recapture.fragment.MemoryFragment;
import apps.ahqmrf.recapture.fragment.PeopleFragment;
import apps.ahqmrf.recapture.fragment.ProfileFragment;
import apps.ahqmrf.recapture.fragment.SettingsFragment;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;

public class MainActivity extends AppCompatActivity implements TabFragmentCallback, ImageSelectAdapter.ImageSelectCallback {

    private RelativeLayout mLayoutProgressBar;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSplash();
        initComponents();
        mDatabase = new Database(this, null, null, 1);
    }

    private void initComponents() {
        mFragmentManager = getSupportFragmentManager();
        prepareFragments();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(mFragmentManager, this, mFragments));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void prepareFragments() {
        mFragments = new ArrayList<>();
        MemoryFragment memoryFragment = new MemoryFragment();
        memoryFragment.setCallback(this);
        memoryFragment.setContext(this);
        mFragments.add(memoryFragment);

        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setCallback(this);
        galleryFragment.setContext(this);
        galleryFragment.setImageCallback(this);
        mFragments.add(galleryFragment);

        PeopleFragment peopleFragment = new PeopleFragment();
        peopleFragment.setCallback(this);
        peopleFragment.setContext(this);
        mFragments.add(peopleFragment);

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setContext(this);
        profileFragment.setCallback(this);
        mFragments.add(profileFragment);

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setCallback(this);
        settingsFragment.setContext(this);
        mFragments.add(settingsFragment);
    }

    private void loadSplash() {
        mLayoutProgressBar = (RelativeLayout) findViewById(R.id.ll_progress);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                mLayoutProgressBar.setVisibility(View.GONE);
            }
        }, Constants.Basic.SPLASH_SCREEN_DURATION);
    }

    @Override
    public void onAddButtonClick() {
        startActivity(new Intent(this, CreateMemoryActivity.class));
    }

    @Override
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
    public void onImageSelectClick(int position, ImageModel model) {

    }

    @Override
    public void onImageClick(final String imagePath) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                FullSizeImageDialogFragment fragment = FullSizeImageDialogFragment.newInstance(imagePath);
                fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_DialogActivity_Transparent);
                fragment.show(getSupportFragmentManager(), FullSizeImageDialogFragment.TAG);
            }
        });
    }
}
