package apps.ahqmrf.recapture.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.GalleryImagesListAdapter;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.adapter.ImagesPagerAdapter;
import apps.ahqmrf.recapture.adapter.PeopleListAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.model.Time;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.GridSpacingItemDecoration;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.SystemHelper;

public class MemoryViewActivity extends AppCompatActivity implements PeopleListAdapter.PeopleItemCallback, ImageSelectAdapter.ImageSelectCallback, View.OnClickListener {

    private ScrollView scrollView;
    private Memory memory;
    private TextView title, time, description, amount, peopleAmount, hTime;
    private RecyclerView relatedImages, relatedPeople;
    private int size;
    private GalleryImagesListAdapter mAdapter;
    private ImageView star, play, delete, edit, icon;
    private boolean special;
    private Database database;
    private ArrayList<People> peoples;
    private int sizePeople;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private ViewPager mViewPager;
    private int NUM_PAGES, currentPage;
    private ImagesPagerAdapter mPagerAdapter;
    private View bottombar;
    private boolean flipping;
    private CirclePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        memory = getIntent().getParcelableExtra(Constants.IntentExtras.MEMORY);
        database = new Database(this, null, null, 1);
        special = memory.isSpecial();
        flipping = false;
        prepareViews();
    }

    private void prepareViews() {
        mIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
        bottombar = findViewById(R.id.action);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        icon = (ImageView) findViewById(R.id.image_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memory.getIconPath() == null) return;
                ArrayList<String> paths = new ArrayList<String>();
                paths.add(memory.getIconPath());
                onImageClick(paths, 0);
            }
        });
        if(memory.getIconPath() == null) {
            linearLayout.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.re_capture);
        }
        else {
            ImageLoader.getInstance().displayImage(
                    "file://" + memory.getIconPath(),
                    icon,
                    MyDisplayImageOptions.getInstance().getDisplayImageOptions(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setProgress(0);
                            linearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            linearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });
        }

        hTime = (TextView) findViewById(R.id.text_h_time);
        hTime.setText(memory.getHappenedTime() + ", " + memory.getHappenedDate());

        title = (TextView) findViewById(R.id.text_title);
        title.setText(memory.getTitle());
        time = (TextView) findViewById(R.id.text_time);
        Time t = memory.getTime();
        String timeStamp = new SystemHelper(this).get12HourTimeStamp(t);
        String date = memory.getTime().getDate();
        time.setText("Created at " + timeStamp + ", " + date);
        description = (TextView) findViewById(R.id.text_description);
        String str = memory.getDescription();
        if(str.isEmpty()) str = "No description added.";
        description.setText(str);
        amount = (TextView) findViewById(R.id.text_amount);
        amount.setText("Related Photos (" + memory.getImages().size() + ")");
        peopleAmount = (TextView) findViewById(R.id.text_related_people);
        sizePeople = memory.getPeoples().size();
        peopleAmount.setText("Related People (" + sizePeople + ")");
        relatedImages = (RecyclerView) findViewById(R.id.recycler_related_photos);
        ViewTreeObserver vto = relatedImages.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relatedImages.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = relatedImages.getMeasuredWidth();
                setRecycler();
            }
        });
        star = (ImageView) findViewById(R.id.image_star);
        delete = (ImageView) findViewById(R.id.image_trash);
        play = (ImageView) findViewById(R.id.image_play);
        play.setOnClickListener(this);
        if(memory.getImages().size() == 0) play.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        else play.setColorFilter(ContextCompat.getColor(this, R.color.black));
        star.setOnClickListener(this);
        delete.setOnClickListener(this);
        if (special) star.setImageResource(R.drawable.star_golden);
        else star.setImageResource(R.drawable.star_normal);

        preparePager();
    }

    private void preparePager() {
        if(memory.getImages().size() == 0) return;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ArrayList<String> photos = memory.getImages();
        NUM_PAGES = photos.size();
        mPagerAdapter = new ImagesPagerAdapter(this, photos);
        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    private void setRecycler() {
        int column = 3;
        mAdapter = new GalleryImagesListAdapter(this, this, memory, size / column);
        relatedImages.setLayoutManager(new GridLayoutManager(this, column));
        relatedImages.addItemDecoration(new GridSpacingItemDecoration(column, 3, true));
        relatedImages.setAdapter(mAdapter);
        relatedPeople = (RecyclerView) findViewById(R.id.recycler_related_people);
        peoples = database.getAllTaggedPeople(memory);
        relatedPeople.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        PeopleListAdapter adapter = new PeopleListAdapter(this, this, peoples);
        relatedPeople.setAdapter(adapter);
        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageSelectClick(int position, ImageModel model) {

    }

    @Override
    public void onImageClick(ArrayList<String> paths, int position) {
        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA, paths);
        intent.putExtra(Constants.IntentExtras.POSITION, position);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        startActivity(intent, options.toBundle());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_star:
                toggleSpecial();
                break;
            case R.id.image_trash:
                deleteMemory();
                break;
            case R.id.image_play:
                startFlipping();
                break;
        }
    }

    private void hideViewPager() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Memory");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        }
        mViewPager.setVisibility(View.GONE);
        flipping = false;
        mIndicator.setVisibility(View.GONE);
        bottombar.setVisibility(View.VISIBLE);
        play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }

    private void startFlipping() {
        play.setImageResource(R.drawable.ic_slide_show);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Slide Show");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.black_transparent)));
        }
        flipping = true;
        bottombar.setVisibility(View.INVISIBLE);
        mIndicator.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setCurrentItem(0);
        currentPage = 0;
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 4000, 4000);

    }

    private void deleteMemory() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.deleteTitle))
                .setMessage(
                        getResources().getString(R.string.promptDeleteMem))
                .setIcon(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_black_24dp))
                .setPositiveButton(
                        getResources().getString(R.string.PostiveYesButton),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.remove(memory);
                                finishAffinity();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.NegativeNoButton),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do Something Here
                            }
                        }).show();
    }

    private void toggleSpecial() {
        if (special) {
            star.setImageResource(R.drawable.star_normal);
            special = false;
        } else {
            star.setImageResource(R.drawable.star_golden);
            special = true;
        }
        memory.setSpecial(special);
        database.updateMemory(memory);
    }

    @Override
    public void onClickPeople(People people) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.IntentExtras.PEOPLE, people);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(People people) {
        database.remove(people);
        sizePeople--;
        peopleAmount.setText("Related People (" +sizePeople + ")");
    }

    @Override
    public void onBackPressed() {
        if(flipping) hideViewPager();
        else super.onBackPressed();
    }
}
