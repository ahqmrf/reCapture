package apps.ahqmrf.recapture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.FileComparator;
import apps.ahqmrf.recapture.util.GridSpacingItemDecoration;

public class GalleryActivity extends Activity implements ImageSelectAdapter.ImageSelectCallback {

    private ArrayList<ImageModel> mImageList;
    private ArrayList<File> mImageFiles;
    private final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    private RecyclerView mImagesRecyclerView;
    private LinearLayout mProgressbarLayout;
    private TreeSet<Integer> imagePathsSet;
    private ArrayList<String> imagePathsSelected;
    private ImageSelectAdapter mAdapter;
    private int size;
    private Button mDoneBtn;

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
        imagePathsSet = new TreeSet<>();
        imagePathsSelected = new ArrayList<>();
        mImagesRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);
        mDoneBtn = (Button) findViewById(R.id.btn_done);
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
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
        mImagesRecyclerView.setAdapter(mAdapter);
        int spanCount = 3; // 3 columns
        int spacing = 3; // 3px
        boolean includeEdge = false;
        mImagesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
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
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onImageClick(int position, ImageModel model) {
        if(model.isSelected()) imagePathsSet.add(position);
        else imagePathsSet.remove(position);
    }
}
