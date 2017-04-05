package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.MemoryListAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.Memory;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemoryFragment extends Fragment {

    private Context mContext;
    private TabFragmentCallback mCallback;
    private View mRootView;
    private TextView mEmptyListText;
    private RecyclerView mMemoryRecyclerView;
    private FloatingActionButton mAddNewBtn;
    private ArrayList<Memory> mListMemory;
    private Database mDatabase;
    private MemoryListAdapter mAdapter;
    private MemoryListAdapter.MemoryClickCallback memoryClickCallback;

    public MemoryFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_memory, container, false);
        mDatabase = new Database(getActivity(), null, null, 1);
        initComponents();
        return mRootView;
    }

    private void initComponents() {
        mEmptyListText = (TextView) mRootView.findViewById(R.id.text_empty_list);
        mMemoryRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_history);
        mAddNewBtn = (FloatingActionButton) mRootView.findViewById(R.id.btn_add);
        mAddNewBtn.setOnClickListener(mOnClickListener);

        manipulateViews();
    }

    private void manipulateViews() {
        mListMemory = mDatabase.getAllMemories();
        if(mListMemory == null || mListMemory.size() == 0) {
            mEmptyListText.setVisibility(View.VISIBLE);
            mMemoryRecyclerView.setVisibility(GONE);
        } else {
            mEmptyListText.setVisibility(View.GONE);
            mMemoryRecyclerView.setVisibility(View.VISIBLE);
            mMemoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mAdapter = new MemoryListAdapter(getActivity(), memoryClickCallback, mListMemory);
            mMemoryRecyclerView.setAdapter(mAdapter);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onAddButtonClick();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mCallback = (TabFragmentCallback) context;
            this.memoryClickCallback = (MemoryListAdapter.MemoryClickCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
