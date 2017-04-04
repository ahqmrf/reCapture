package apps.ahqmrf.recapture.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.adapter.GalleryMainItemAdapter;
import apps.ahqmrf.recapture.adapter.ImageSelectAdapter;
import apps.ahqmrf.recapture.database.Database;
import apps.ahqmrf.recapture.interfaces.TabFragmentCallback;
import apps.ahqmrf.recapture.model.Memory;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private TabFragmentCallback mCallback;
    private Database mDatabase;
    private View rootView;
    private ArrayList<Memory> items;
    private ArrayList<Memory> itemsClone;
    private ImageSelectAdapter.ImageSelectCallback mImageCallback;
    private GalleryMainItemAdapter mAdapter;
    private RecyclerView mGalleryRecyclerView;
    private int size;
    private ImageView galleryView, listView;

    public void setImageCallback(ImageSelectAdapter.ImageSelectCallback mImageCallback) {
        this.mImageCallback = mImageCallback;
    }

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
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        prepareViews();
        return rootView;
    }

    private void prepareViews() {
        mGalleryRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_gallery);
        ViewTreeObserver vto = mGalleryRecyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGalleryRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = mGalleryRecyclerView.getMeasuredWidth();
                setRecycler();
            }
        });
        galleryView = (ImageView) rootView.findViewById(R.id.image_grid);
        galleryView.setOnClickListener(this);
        listView = (ImageView) rootView.findViewById(R.id.image_list);
        listView.setOnClickListener(this);
    }

    private void setRecycler() {
        mDatabase = new Database(getActivity(), null, null, 1);
        items = mDatabase.getAllMemories();
        itemsClone = new ArrayList<>();
        for (Memory memory : items) {
            if(memory.getImages().size() > 0) {
                itemsClone.add(memory);
            }
        }
        mAdapter = new GalleryMainItemAdapter(getActivity(), mImageCallback, itemsClone, size / 3, false);
        mGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mGalleryRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_grid:
                showGridView();
                break;
            case R.id.image_list:
                showListView();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mCallback = (TabFragmentCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void showListView() {
        mAdapter = new GalleryMainItemAdapter(getActivity(), mImageCallback, itemsClone, size, true);
        mGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mGalleryRecyclerView.setAdapter(mAdapter);

        galleryView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        listView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.whitey_grey));
    }

    private void showGridView() {
        mAdapter = new GalleryMainItemAdapter(getActivity(), mImageCallback, itemsClone, size / 3, false);
        mGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mGalleryRecyclerView.setAdapter(mAdapter);

        galleryView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.whitey_grey));
        listView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
    }
}
