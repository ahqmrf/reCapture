package apps.ahqmrf.recapture.activity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.OnSwipeTouchListener;

public class ImageFullScreenActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mBackImage;
    private ArrayList<String> imageUri;
    private int position;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        Window window = getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        window.setLayout((int) (size.x), (int) (size.y));
        window.setGravity(Gravity.CENTER);

        mImageView = (ImageView) findViewById(R.id.image_full_size);
        mBackImage = (ImageView) findViewById(R.id.image_back);
        imageUri = getIntent().getStringArrayListExtra(Constants.IntentExtras.IMAGE_LIST_EXTRA);
        position = getIntent().getIntExtra(Constants.IntentExtras.POSITION, 0);
        linearLayout = (LinearLayout) findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        setValuesIntoViews();
    }

    private void setValuesIntoViews() {
        showImageAt(position);

        mImageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {
                if(position > 0) {
                    position--;
                    showImageAt(position);
                }
            }

            public void onSwipeLeft() {
                if(position < imageUri.size() - 1) {
                    position++;
                    showImageAt(position);
                }
            }

            public void onSwipeBottom() {

            }

        });

        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });
    }
}
