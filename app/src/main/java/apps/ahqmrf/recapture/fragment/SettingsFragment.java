package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private TabFragmentCallback callback;
    private View viewRoot, nameLayout, aboutLayout, quoteLayout;
    private ImageView mImageView;
    private TextView mNameBig, mNameSmall, mAbout, mFavoriteQuote;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TabFragmentCallback getCallback() {
        return callback;
    }

    public void setCallback(TabFragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_settings, container, false);
        prepareViews();
        return viewRoot;
    }

    private void prepareViews() {
        nameLayout = viewRoot.findViewById(R.id.layout_username);
        quoteLayout = viewRoot.findViewById(R.id.layout_quote);
        aboutLayout = viewRoot.findViewById(R.id.layout_about);
        linearLayout = (LinearLayout) viewRoot.findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressbar);
        mImageView = (ImageView) viewRoot.findViewById(R.id.image_profile);
        mNameBig = (TextView) viewRoot.findViewById(R.id.text_name);
        mNameSmall = (TextView) viewRoot.findViewById(R.id.text_username);
        mAbout = (TextView) viewRoot.findViewById(R.id.text_about);
        mFavoriteQuote = (TextView) viewRoot.findViewById(R.id.text_quote);

        mImageView.setOnClickListener(this);
        nameLayout.setOnClickListener(this);
        quoteLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);


        setValuesToViews();
    }

    private void setValuesToViews() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String imageUri = preferences.getString(Constants.Basic.PROFILE_IMAGE_PATH, null);
        String name = preferences.getString(Constants.Basic.USERNAME, null);
        String quote = preferences.getString(Constants.Basic.FAVORITE_QUOTE, null);
        String about = preferences.getString(Constants.Basic.ABOUT, null);

        if(imageUri == null) {
            linearLayout.setVisibility(View.GONE);
            mImageView.setImageResource(R.drawable.ic_account_circle_24dp);
        }
        else {
            ImageLoader.getInstance().displayImage(
                    "file://" + imageUri,
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

        if(name == null) {
            mNameSmall.setText("Click here to set username");
            mNameBig.setText("No name set yet");
        } else {
            mNameSmall.setText(name);
            mNameBig.setText(name);
        }

        if(quote == null) {
            mFavoriteQuote.setText("Click to set your favorite quote");
        } else mFavoriteQuote.setText(quote);

        if(about == null) {
            mAbout.setText("Click to write something about you");
        } else mAbout.setText(about);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.callback = (TabFragmentCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setValuesToViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_profile:
                callback.onProfilePhotoClick();
                break;
            case R.id.layout_username:
                callback.onNameClick();
                break;
            case R.id.layout_about:
                callback.onAboutClick();
                break;
            case R.id.layout_quote:
                callback.onQuoteClick();
                break;
        }
    }
}
