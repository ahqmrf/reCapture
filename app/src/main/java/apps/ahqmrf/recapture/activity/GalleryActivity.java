package apps.ahqmrf.recapture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.FileComparator;
import apps.ahqmrf.recapture.util.GridSpacingItemDecoration;

public class GalleryActivity extends AppCompatActivity implements ImageSelectAdapter.ImageSelectCallback {

    private ArrayList<ImageModel> mImageList;
    private ArrayList<File> mImageFiles;
    private final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    private RecyclerView mImagesRecyclerView;
    private LinearLayout mProgressbarLayout;
    private TreeSet<Integer> imagePathsSet;
    private ArrayList<String> imagePathsSelected;
    private ImageSelectAdapter mAdapter;
    private int size;
    private TextView mAmountSelectedText;
    private RelativeLayout mRelativeDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getAllImages();
    }

    private void getAllImages() {
        mImageFiles = new ArrayList<>();
        mImageList = new ArrayList<>();

        File file = new File(ROOT_DIRECTORY);
        if (file.exists()) {
            if (file.isDirectory()) {
                File fileList[] = file.listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        if (f != null)
                            findImage(ROOT_DIRECTORY, f.getAbsolutePath());
                    }
                }
            }
        }

        Collections.sort(mImageFiles, new FileComparator());

        for (File imageFile : mImageFiles) {
            mImageList.add(new ImageModel(imageFile.getAbsolutePath(), false));
        }

        prepareViews();
    }

    private void prepareViews() {
        mAmountSelectedText = (TextView) findViewById(R.id.text_selected_photo_amount);
        imagePathsSet = new TreeSet<>();
        imagePathsSelected = new ArrayList<>();
        mImagesRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);
        mRelativeDone = (RelativeLayout) findViewById(R.id.relative_done);
        mRelativeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        ViewTreeObserver vto = mImagesRecyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImagesRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = mImagesRecyclerView.getMeasuredWidth() / 3;
                setRecycler();

            }
        });
        mProgressbarLayout = (LinearLayout) findViewById(R.id.ll_progress);
    }

    private void goBack() {
        for(Integer i : imagePathsSet) {
            imagePathsSelected.add(mImageList.get(i).getFilePath());
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Constants.Basic.IMAGE_LIST_EXTRA, imagePathsSelected);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setRecycler() {
        mAdapter = new ImageSelectAdapter(this, this, mImageList, size);
        mImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mImagesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 3, true));
        mImagesRecyclerView.setAdapter(mAdapter);
        mProgressbarLayout.setVisibility(View.GONE);
    }

    private void findImage(String parent, String path) {
        if (path == null) return;
        File file = new File(path);
        if (path.contains("Android")) return;
        if (file.exists()) {
            if (file.isDirectory()) {
                File fileList[] = file.listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        if (f != null)
                            findImage(path, f.getAbsolutePath());
                    }
                }
            } else {
                if (path.endsWith(".jpeg") || path.endsWith(".JPEG")
                        || path.endsWith(".png") || path.endsWith(".PNG")
                        || path.endsWith(".bmp") || path.endsWith(".BMP")
                        || path.endsWith(".jpg") || path.endsWith(".JPG")
                        || path.endsWith(".gif") || path.endsWith(".GIF")) {
                    mImageFiles.add(file);
                }
            }
        }
    }

    @Override
    public void onImageSelectClick(int position, ImageModel model) {
        if(model.isSelected()) imagePathsSet.add(position);
        else imagePathsSet.remove(position);
        if(imagePathsSet.size() > 0) {
            mAmountSelectedText.setVisibility(View.VISIBLE);
            mAmountSelectedText.setText(imagePathsSet.size() + "");
        } else {
            mAmountSelectedText.setVisibility(View.GONE);
        }
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
}
