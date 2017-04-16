package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
    private TextView mNameSmall, mAbout, mFavoriteQuote;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private String imageUri;
    private SwitchCompat mSwitchLock;
    private RelativeLayout passLayout;

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
        passLayout = (RelativeLayout) viewRoot.findViewById(R.id.layout_password);
        passLayout.setOnClickListener(this);
        mSwitchLock = (SwitchCompat) viewRoot.findViewById(R.id.simpleSwitch);
        mSwitchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                changeLockMode(isChecked);
            }
        });
        nameLayout = viewRoot.findViewById(R.id.layout_username);
        quoteLayout = viewRoot.findViewById(R.id.layout_quote);
        aboutLayout = viewRoot.findViewById(R.id.layout_about);
        linearLayout = (LinearLayout) viewRoot.findViewById(R.id.linear_progressbar);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressbar);
        mNameSmall = (TextView) viewRoot.findViewById(R.id.text_username);
        mAbout = (TextView) viewRoot.findViewById(R.id.text_about);
        mFavoriteQuote = (TextView) viewRoot.findViewById(R.id.text_quote);

        nameLayout.setOnClickListener(this);
        quoteLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);


        setValuesToViews();
    }

    private void changeLockMode(boolean isChecked) {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.Basic.LOCK_MODE, isChecked);
        editor.apply();
    }

    private void setValuesToViews() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = preferences.getString(Constants.Basic.USERNAME, null);
        String quote = preferences.getString(Constants.Basic.FAVORITE_QUOTE, null);
        String about = preferences.getString(Constants.Basic.ABOUT, null);
        boolean lockMode = preferences.getBoolean(Constants.Basic.LOCK_MODE, false);


        if(name == null) {
            mNameSmall.setText("Click here to set username");
        } else {
            mNameSmall.setText(name);
        }

        if(quote == null) {
            mFavoriteQuote.setText("Click to set your favorite quote");
        } else mFavoriteQuote.setText(quote);

        if(about == null) {
            mAbout.setText("Click to write something about you");
        } else mAbout.setText(about);

        mSwitchLock.setChecked(lockMode);
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
            case R.id.layout_username:
                callback.onNameClick();
                break;
            case R.id.layout_about:
                callback.onAboutClick();
                break;
            case R.id.layout_quote:
                callback.onQuoteClick();
                break;
            case R.id.layout_password:
                callback.onPasswordClick();
                break;
        }
    }
}
