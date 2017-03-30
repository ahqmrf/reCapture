package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Context context;
    private TabFragmentCallback callback;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
