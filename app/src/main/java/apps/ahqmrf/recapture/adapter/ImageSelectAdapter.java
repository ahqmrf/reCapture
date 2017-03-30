package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Lenovo on 3/30/2017.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter <ImageSelectAdapter.ImageViewHolder> {

    public interface ImageSelectCallback {
        void onImageClick(int position);
    }

    private Context context;
    private ImageSelectCallback callback;
    private ArrayList<String> items;

    public ImageSelectAdapter(Context context, ImageSelectCallback callback, ArrayList<String> items) {
        this.context = context;
        this.callback = callback;
        this.items = items;
    }

    @Override
    public ImageSelectAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ImageSelectAdapter.ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
