package apps.ahqmrf.recapture.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private TabFragmentCallback callback;
    private View viewRoot, infoLayout;
    private SwitchCompat mSwitchLock;
    private RelativeLayout passLayout;
    private Spinner mSpinner;

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
        mSpinner = (Spinner) viewRoot.findViewById(R.id.spinner_time_interval);
        infoLayout = viewRoot.findViewById(R.id.layout_info);
        infoLayout.setOnClickListener(this);
        passLayout = (RelativeLayout) viewRoot.findViewById(R.id.layout_password);
        passLayout.setOnClickListener(this);
        mSwitchLock = (SwitchCompat) viewRoot.findViewById(R.id.simpleSwitch);
        mSwitchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                changeLockMode(isChecked);
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Basic.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(Constants.Basic.SLIDE_SHOW_TIME_INTERVAL, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
        int timeInterval = preferences.getInt(Constants.Basic.SLIDE_SHOW_TIME_INTERVAL, 2);
        boolean lockMode = preferences.getBoolean(Constants.Basic.LOCK_MODE, false);

        mSwitchLock.setChecked(lockMode);
        mSpinner.setSelection(timeInterval);
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
            case R.id.layout_password:
                callback.onPasswordClick();
                break;
            case R.id.layout_info:
                showInfo();
                break;
        }
    }

    private void showInfo() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.info)
                .setCustomTitle(view)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        messageText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources().getDimension(R.dimen.textsize));
    }
}
