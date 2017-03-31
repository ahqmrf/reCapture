package apps.ahqmrf.recapture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;

public class PeopleFragment extends Fragment {

    private Context context;
    private TabFragmentCallback callback;
    private View root;
    private LinearLayout mAddNewUser;

    public PeopleFragment() {
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
        root = inflater.inflate(R.layout.fragment_people, container, false);
        mAddNewUser = (LinearLayout) root.findViewById(R.id.linear_add_new_user);
        mAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addNewUser();
            }
        });
        return root;
    }

}
