package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.IconSelectionAdapter;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.SystemHelper;

public class SelectIconActivity extends AppCompatActivity implements IconSelectionAdapter.IconSelectCallback{

    private TextView mTitleMemoryText;
    private RecyclerView mIconsRecyclerView;
    private String memoryTitle;
    private String memoryDescription;
    private String hDate, hTime;
    private String iconPath;
    private ArrayList<String> memoryImages;
    private ArrayList<File> imageFiles;
    private int selectedImageId;
    private IconSelectionAdapter mAdapter;
    private Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        catchData();
        prepareViews();
    }

    private void catchData() {
        memoryTitle = getIntent().getStringExtra(Constants.IntentExtras.MEMORY_TITLE);
        memoryDescription = getIntent().getStringExtra(Constants.IntentExtras.MEMORY_DESCRIPTION);
        memoryImages = getIntent().getStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA);
        hDate = getIntent().getStringExtra(Constants.IntentExtras.HAPPENED_DATE);
        hTime = getIntent().getStringExtra(Constants.IntentExtras.HAPPENED_TIME);
        if(memoryImages == null) memoryImages = new ArrayList<>();
        imageFiles = new ArrayList<>();

        for (String path : memoryImages) {
            imageFiles.add(new File(path));
        }

        if(memoryImages.size() > 0) iconPath = memoryImages.get(0);
    }

    private void prepareViews() {
        mTitleMemoryText = (TextView) findViewById(R.id.text_memory_title);
        if(memoryImages.size() > 0) mTitleMemoryText.setText("Select an icon for \"" + memoryTitle + "\" memory and click next to proceed.");
        else {
            mTitleMemoryText.setText("You selected no images. No images to show for selecting icon. Click next to proceed.");
        }
        mIconsRecyclerView = (RecyclerView) findViewById(R.id.recycler_icon_list);
        mIconsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new IconSelectionAdapter(this, this, imageFiles);
        mIconsRecyclerView.setAdapter(mAdapter);

        mNextBtn = (Button) findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });
    }

    private void proceed() {
        Memory memory = new Memory();
        memory.setTitle(memoryTitle);
        memory.setDescription(memoryDescription);
        memory.setIconPath(iconPath);
        memory.setImages(memoryImages);
        memory.setHappenedDate(hDate);
        memory.setHappenedTime(hTime);

        Intent intent = new Intent(this, TagPeopleActivity.class);
        intent.putExtra(Constants.IntentExtras.MEMORY, memory);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageSelectClick(int id) {
        selectedImageId = id;
        iconPath = imageFiles.get(id).getAbsolutePath();
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
