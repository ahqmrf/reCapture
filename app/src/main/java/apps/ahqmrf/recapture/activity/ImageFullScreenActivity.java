package apps.ahqmrf.recapture.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.OnSwipeTouchListener;

public class ImageFullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private ImageView mBackImage;
    private ImageView mLeftArrow;
    private ImageView mRightArrow;
    private ImageView mMore;
    private ArrayList<String> imageUri;
    private int position;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mMore = (ImageView) findViewById(R.id.image_more);
        mMore.setOnClickListener(this);

        mImageView = (ImageView) findViewById(R.id.image_full_size);
        mBackImage = (ImageView) findViewById(R.id.image_back);
        mLeftArrow = (ImageView) findViewById(R.id.image_left);
        mRightArrow = (ImageView) findViewById(R.id.image_right);
        imageUri = getIntent().getStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA);

        position = getIntent().getIntExtra(Constants.IntentExtras.POSITION, 0);
        linearLayout = (LinearLayout) findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        setValuesIntoViews();
    }

    private void setValuesIntoViews() {
        if (position == 0) mLeftArrow.setVisibility(View.GONE);
        if (position == imageUri.size() - 1) mRightArrow.setVisibility(View.GONE);

        showImageAt(position);

        mImageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                showLeftImage();
            }

            public void onSwipeLeft() {
                showRightImage();
            }

            public void onSwipeBottom() {

            }

        });

        mBackImage.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        mRightArrow.setOnClickListener(this);
    }

    private void showImageAt(int index) {
        ImageLoader.getInstance().displayImage(
                "file://" + imageUri.get(index),
                mImageView,
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
                        mBitmap = loadedImage;
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_left:
                showLeftImage();
                break;
            case R.id.image_right:
                showRightImage();
                break;
            case R.id.image_more:
                showPopup();
                break;
        }
    }

    private void showPopup() {
        PopupMenu popupMenu = new PopupMenu(this, mMore);
        popupMenu.inflate(R.menu.image_context_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rotate_left:
                        rotateLeft();
                        break;
                    case R.id.rotate_right:
                        rotateRight();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void rotateRight() {
        if (mBitmap != null) {
            mBitmap = rotateBitmap(90, mBitmap);
            mImageView.setImageBitmap(mBitmap);
        }
    }

    private void rotateLeft() {
        if (mBitmap != null) {
            mBitmap = rotateBitmap(-90, mBitmap);
            mImageView.setImageBitmap(mBitmap);
        }
    }

    private void showRightImage() {
        if (position < imageUri.size() - 1) {
            position++;
            showImageAt(position);
        }
        if (imageUri.size() > 1) mLeftArrow.setVisibility(View.VISIBLE);
        if (position == imageUri.size() - 1) mRightArrow.setVisibility(View.GONE);
    }

    private void showLeftImage() {
        if (position > 0) {
            position--;
            showImageAt(position);
        }
        if (imageUri.size() > 1) mRightArrow.setVisibility(View.VISIBLE);
        if (position == 0) mLeftArrow.setVisibility(View.GONE);
    }

    public Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        if (angle != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }
        return bitmap;
    }
}
