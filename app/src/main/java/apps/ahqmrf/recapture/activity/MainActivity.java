package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.SimpleFragmentPagerAdapter;
import apps.ahqmrf.recapture.fragment.GalleryFragment;
import apps.ahqmrf.recapture.fragment.MemoryFragment;
import apps.ahqmrf.recapture.fragment.PeopleFragment;
import apps.ahqmrf.recapture.fragment.ProfileFragment;
import apps.ahqmrf.recapture.fragment.SettingsFragment;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.util.Constants;

public class MainActivity extends AppCompatActivity implements TabFragmentCallback {

    private LinearLayout mLayoutProgressBar;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSplash();
        initComponents();
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
        mLayoutProgressBar = (LinearLayout) findViewById(R.id.ll_progress);

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
}
