package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.adapter.MemoryListAdapter;
import apps.ahqmrf.recapture.adapter.PeopleListAdapter;
import apps.ahqmrf.recapture.adapter.SimpleFragmentPagerAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.fragment.GalleryFragment;
import apps.ahqmrf.recapture.fragment.MemoryFragment;
import apps.ahqmrf.recapture.fragment.PeopleFragment;
import apps.ahqmrf.recapture.fragment.SettingsFragment;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.ToastMaker;

public class MainActivity extends AppCompatActivity implements TabFragmentCallback, ImageSelectAdapter.ImageSelectCallback, MemoryListAdapter.MemoryClickCallback, PeopleListAdapter.PeopleItemCallback {

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
        mFragments.add(memoryFragment);

        GalleryFragment galleryFragment = new GalleryFragment();
        mFragments.add(galleryFragment);

        PeopleFragment peopleFragment = new PeopleFragment();
        mFragments.add(peopleFragment);

        SettingsFragment settingsFragment = new SettingsFragment();
        mFragments.add(settingsFragment);
    }

    private void loadSplash() {
        mLayoutProgressBar = (RelativeLayout) findViewById(R.id.ll_progress);
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
    public void hideProgressBar() {
        mLayoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPasswordClick() {
        startActivity(new Intent(this, PasswordActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, SingleImageSelectionActivity.class);
                    startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);

                } else {
                    ToastMaker.showShortMessage(this, "Permission required to select media");
                }
            }
        }
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
    public void onImageClick(ArrayList<String> paths, int position) {
        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA, paths);
        intent.putExtra(Constants.IntentExtras.POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onImageClick(final String imagePath) {
        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putExtra(Constants.IntentExtras.IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    @Override
    public void onClickMemory(Memory memory) {
        Intent intent = new Intent(this, MemoryViewActivity.class);
        intent.putExtra(Constants.IntentExtras.MEMORY, memory);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(Memory memory) {
        mDatabase.remove(memory);
    }

    @Override
    public void onClickPeople(People people) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.IntentExtras.PEOPLE, people);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(People people) {
        mDatabase.remove(people);
    }
}
