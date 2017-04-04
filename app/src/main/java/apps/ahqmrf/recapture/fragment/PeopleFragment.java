package apps.ahqmrf.recapture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.activity.MainActivity;
import apps.ahqmrf.recapture.adapter.PeopleListAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.Constants;

public class PeopleFragment extends Fragment implements PeopleListAdapter.PeopleItemCallback{

    private Context context;
    private TabFragmentCallback callback;
    private View root;
    private LinearLayout mAddNewUser;
    private TextView noUserText;
    private Database mDatabase;
    private ArrayList<People> peoples;
    private RecyclerView mRecyclerPeopleList;
    private PeopleListAdapter mAdapter;
    private ImageView mImageRefresh;
    private LinearLayout mLinearProgressBar;

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

        mDatabase = new Database(getActivity(), null, null, 1);
        root = inflater.inflate(R.layout.fragment_people, container, false);
        mImageRefresh = (ImageView) root.findViewById(R.id.image_refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareViews();
                mLinearProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mLinearProgressBar.setVisibility(View.GONE);
                    }
                }, Constants.Basic.PROGRESS_BAR_DURATION);
            }
        });
        prepareViews();
        return root;
    }

    private void prepareViews() {
        mLinearProgressBar = (LinearLayout) root.findViewById(R.id.linear_progressbar);
        peoples = mDatabase.getAllPeople();
        noUserText = (TextView) root.findViewById(R.id.text_no_user);
        mAddNewUser = (LinearLayout) root.findViewById(R.id.linear_add_new_user);
        mAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addNewUser();
            }
        });
        mRecyclerPeopleList = (RecyclerView) root.findViewById(R.id.recycler_people_list);
        if(peoples.size() > 0) {
            noUserText.setVisibility(View.GONE);
            mRecyclerPeopleList.setVisibility(View.VISIBLE);
            mRecyclerPeopleList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mAdapter = new PeopleListAdapter(getActivity(), this, peoples);
            mRecyclerPeopleList.setAdapter(mAdapter);
        } else {
            noUserText.setVisibility(View.VISIBLE);
            mRecyclerPeopleList.setVisibility(View.GONE);
        }
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
}
