package apps.ahqmrf.recapture.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
        peopleFragment.setItemCallback(this);
        mFragments.add(peopleFragment);

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setCallback(this);
        settingsFragment.setContext(this);
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
    public void onProfilePhotoClick(String path) {
        if(path == null) return;
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        onImageClick(paths, 0);
    }

    @Override
    public void hideProgressBar() {
        mLayoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCameraClick() {
        openDefaultGallery();
    }

    @Override
    public void onPasswordClick() {
        startActivity(new Intent(this, PasswordActivity.class));
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.RequestCodes.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {

            Intent intent = new Intent(this, SingleImageSelectionActivity.class);
            startActivityForResult(intent, Constants.RequestCodes.GALLERY_BROWSE_REQ);
        }
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


    private void openDefaultGallery() {
        checkReadExternalStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.RequestCodes.ADD_USER_REQ && resultCode == RESULT_OK) {
            People people = data.getParcelableExtra(Constants.IntentExtras.PEOPLE);
            mDatabase.insertUser(people);
        }
        if (requestCode == Constants.RequestCodes.GALLERY_BROWSE_REQ && resultCode == RESULT_OK) {
            String path = data.getStringExtra(Constants.Basic.IMAGE_EXTRA);
            SharedPreferences preferences = getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.Basic.PROFILE_IMAGE_PATH, path);
            editor.apply();
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
        /*Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                FullSizeImageDialogFragment fragment = FullSizeImageDialogFragment.newInstance(imagePath);
                fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_DialogActivity_Transparent);
                fragment.show(getSupportFragmentManager(), FullSizeImageDialogFragment.TAG);
            }
        });*/

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
        // TODO
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
