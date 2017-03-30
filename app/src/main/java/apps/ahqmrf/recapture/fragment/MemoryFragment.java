package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.Memory;

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
        if(mListMemory == null) {
            mEmptyListText.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onAddButtonClick();
        }
    };
}
