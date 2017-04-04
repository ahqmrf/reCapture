package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.util.GridSpacingItemDecoration;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class GalleryMainItemAdapter extends RecyclerView.Adapter<GalleryMainItemAdapter.GalleryItemViewHolder> {

    private Context mContext;
    private ImageSelectAdapter.ImageSelectCallback mCallback;
    private ArrayList<Memory> items;
    private int size;
    private boolean horizontal;

    public GalleryMainItemAdapter(Context mContext, ImageSelectAdapter.ImageSelectCallback mCallback, ArrayList<Memory> items, int size, boolean horizontal) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.items = items;
        this.size = size;
        this.horizontal = horizontal;
    }

    @Override
    public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gallery_model, parent, false);
        return new GalleryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryItemViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        if(horizontal) {
            GalleryImagesListAdapter mAdapter = new GalleryImagesListAdapter(mContext, mCallback, items.get(position).getImages(), size, horizontal);
            holder.images.setLayoutManager(new GridLayoutManager(mContext, 1));
            holder.images.setAdapter(mAdapter);
        } else {
            GalleryImagesListAdapter mAdapter = new GalleryImagesListAdapter(mContext, mCallback, items.get(position).getImages(), size, horizontal);
            holder.images.setLayoutManager(new GridLayoutManager(mContext, 3));
            holder.images.addItemDecoration(new GridSpacingItemDecoration(3, 3, true));
            holder.images.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class GalleryItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView images;

        public GalleryItemViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.text_title);
            images = (RecyclerView) itemView.findViewById(R.id.recycler_gallery_item);
        }
    }
}
