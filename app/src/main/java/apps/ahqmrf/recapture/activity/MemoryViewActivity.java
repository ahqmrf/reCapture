package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.GalleryImagesListAdapter;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.adapter.PeopleListAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.Time;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.GridSpacingItemDecoration;
import apps.ahqmrf.recapture.util.SystemHelper;

public class MemoryViewActivity extends AppCompatActivity implements PeopleListAdapter.PeopleItemCallback, ImageSelectAdapter.ImageSelectCallback, View.OnClickListener{

    private ScrollView scrollView;
    private Memory memory;
    private TextView title, time, description, amount, peopleAmount;
    private RecyclerView relatedImages, relatedPeople;
    private int size;
    private GalleryImagesListAdapter mAdapter;
    private ImageView star;
    private boolean special;
    private Database database;

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
        special = false;

        prepareViews();
    }

    private void prepareViews() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        title = (TextView) findViewById(R.id.text_title);
        title.setText(memory.getTitle());
        time = (TextView) findViewById(R.id.text_time);
        Time t = memory.getTime();
        String timeStamp = new SystemHelper(this).get12HourTimeStamp(t);
        String date = memory.getTime().getDate();
        time.setText("Created at " + timeStamp + ", " + date);
        description = (TextView) findViewById(R.id.text_description);
        description.setText(memory.getDescription());
        amount = (TextView) findViewById(R.id.text_amount);
        amount.setText("Related Photos (" + memory.getImages().size() + ")");
        peopleAmount = (TextView) findViewById(R.id.text_related_people);
        peopleAmount.setText("Related People (" + memory.getPeoples().size() + ")");
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
        star.setOnClickListener(this);
    }

    private void setRecycler() {
        int column = 3;
        mAdapter = new GalleryImagesListAdapter(this, this, memory, size / column, true);
        relatedImages.setLayoutManager(new GridLayoutManager(this, column));
        relatedImages.addItemDecoration(new GridSpacingItemDecoration(column, 3, true));
        relatedImages.setAdapter(mAdapter);
        relatedPeople = (RecyclerView) findViewById(R.id.recycler_related_people);
        relatedPeople.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        PeopleListAdapter adapter = new PeopleListAdapter(this, this, database.getAllTaggedPeople(memory));
        relatedPeople.setAdapter(adapter);
        scrollView.smoothScrollTo(0,0);
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
        startActivity(intent);
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
        }
    }

    private void toggleSpecial() {
        if(special) {
            star.setImageResource(R.drawable.star_normal);
            special = false;
        } else {
            star.setImageResource(R.drawable.star_golden);
            special = true;
        }
    }
}
