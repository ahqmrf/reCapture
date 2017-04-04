package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

/**
 * Created by bsse0 on 4/3/2017.
 */

public class SingleImageSelectListAdapter extends RecyclerView.Adapter <SingleImageSelectListAdapter.ImageViewHolder> {

    public interface ImageSelectCallback {
        void onImageSelectClick(int position);
    }

    private Context context;
    private ImageSelectCallback callback;
    private ArrayList<String> items;
    private int size;
    private int selectedId;

    public SingleImageSelectListAdapter(Context context, ImageSelectCallback callback, ArrayList<String> items, int size) {
        this.context = context;
        this.callback = callback;
        this.items = items;
        this.size = size;
        this.selectedId = -1;
    }

    @Override
    public SingleImageSelectListAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_single_image, parent, false);
        return new SingleImageSelectListAdapter.ImageViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(final SingleImageSelectListAdapter.ImageViewHolder holder, int position) {
        String model = items.get(position);
        ImageLoader.getInstance().displayImage(
                "file://" + model,
                holder.mainImage,
                MyDisplayImageOptions.getInstance().getDisplayImageOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setProgress(0);
                        holder.linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.linearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.linearLayout.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });

        if(position == selectedId) {
            //holder.mainImage.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            holder.overLayer.setVisibility(View.VISIBLE);
        } else {
            //holder.mainImage.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            holder.overLayer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView mainImage;
        TextView overLayer;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        public ImageViewHolder(final View itemView, final ViewGroup parent) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.layout_item);
            mainImage = (ImageView) itemView.findViewById(R.id.image_main);
            mainImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo
                    int prevId = selectedId;
                    selectedId = getAdapterPosition();
                    notifyItemChanged(getAdapterPosition());
                    notifyItemChanged(prevId);
                    callback.onImageSelectClick(getAdapterPosition());
                }
            });

            overLayer = (TextView) itemView.findViewById(R.id.view_layer);

            layout.getLayoutParams().height = size;
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_progressbar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
