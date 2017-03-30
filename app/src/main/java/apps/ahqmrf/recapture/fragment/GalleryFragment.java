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
public class GalleryFragment extends Fragment {
    private Context mContext;
    private TabFragmentCallback mCallback;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public TabFragmentCallback getCallback() {
        return mCallback;
    }

    public void setCallback(TabFragmentCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

}
