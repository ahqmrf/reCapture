package apps.ahqmrf.recapture.activity;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.adapter.SingleImageSelectListAdapter;
import apps.ahqmrf.recapture.fragment.FullSizeImageDialogFragment;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.FileComparator;

public class SingleImageSelectionActivity extends AppCompatActivity implements SingleImageSelectListAdapter.ImageSelectCallback{

    private int selectedImageId;
    private ArrayList<String> mImageList;
    private ArrayList<File> mImageFiles;
    private final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    private RecyclerView mImagesRecyclerView;
    private String imagePathSelected;
    private SingleImageSelectListAdapter mAdapter;
    private int size;
    private ImageView mDoneImage, mCancelImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_selection);

        selectedImageId = -1;
        getAllImages();
    }

    @Override
    public void onImageSelectClick(int position) {
        selectedImageId = position;
        imagePathSelected = mImageList.get(selectedImageId);
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
            mImageList.add(imageFile.getAbsolutePath());
        }

        prepareViews();
    }

    private void prepareViews() {
        mDoneImage = (ImageView) findViewById(R.id.image_done);
        mDoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackOnSuccess();
            }
        });
        mCancelImage = (ImageView) findViewById(R.id.image_cancel);
        mCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackOnFailure();
            }
        });
        mImagesRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);

        ViewTreeObserver vto = mImagesRecyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImagesRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = mImagesRecyclerView.getMeasuredWidth() / 3;
                setRecycler();

            }
        });
    }

    private void goBackOnSuccess() {
        if (imagePathSelected == null || selectedImageId == -1) {
            goBackOnFailure();
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.Basic.IMAGE_EXTRA, imagePathSelected);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void goBackOnFailure() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void setRecycler() {
        mAdapter = new SingleImageSelectListAdapter(this, this, mImageList, size);
        mImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mImagesRecyclerView.setAdapter(mAdapter);
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

}
