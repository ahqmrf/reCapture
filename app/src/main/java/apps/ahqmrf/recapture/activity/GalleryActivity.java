package apps.ahqmrf.recapture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.util.FileComparator;

public class GalleryActivity extends Activity implements ImageSelectAdapter.ImageSelectCallback {

    private ArrayList<String> mImageFilePaths;
    private ArrayList<File> mImageFiles;
    private final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    private RecyclerView mImagesRecyclerView;
    private LinearLayout mProgressbarLayout;
    private TreeSet<Integer> imagePathsSet;
    private ArrayList<String> imagePathsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getAllImages();
    }

    private void getAllImages() {
        mImageFiles = new ArrayList<>();
        mImageFilePaths = new ArrayList<>();

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
            mImageFilePaths.add(imageFile.getAbsolutePath());
        }

        prepareViews();
    }

    private void prepareViews() {
        imagePathsSet = new TreeSet<>();
        imagePathsSelected = new ArrayList<>();
        mImagesRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);
        mProgressbarLayout = (LinearLayout) findViewById(R.id.ll_progress);

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
    public void onImageClick(int position) {
        imagePathsSet.add(position);
    }
}
