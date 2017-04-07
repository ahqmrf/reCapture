package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

/**
 * Created by Lenovo on 3/30/2017.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.ImageViewHolder> {

    public interface ImageSelectCallback {
        void onImageSelectClick(int position, ImageModel model);
        void onImageClick(ArrayList<String> paths, int position);
        void onImageClick(String imagePath);
    }

    private Context context;
    private ImageSelectCallback callback;
    private ArrayList<ImageModel> items;
    private ArrayList<String> paths;
    private int size;

    public ImageSelectAdapter(Context context, ImageSelectCallback callback, ArrayList<ImageModel> items, int size) {
        this.context = context;
        this.callback = callback;
        this.items = items;
        this.size = size;

        paths = new ArrayList<>();

        for (ImageModel imageModel : items) {
            paths.add(imageModel.getFilePath());
        }
    }

    @Override
    public ImageSelectAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_gallery_select_image, parent, false);
        return new ImageViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(final ImageSelectAdapter.ImageViewHolder holder, int position) {
        ImageModel model = items.get(position);
        ImageLoader.getInstance().displayImage(
                "file://" + model.getFilePath(),
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

        if (model.isSelected()) {
            holder.selectImage.setBackgroundResource(R.drawable.circular_red_bg_item_gallery);
        } else {
            holder.selectImage.setBackgroundResource(R.drawable.circular_black_bg_item_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView mainImage, selectImage;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        public ImageViewHolder(final View itemView, final ViewGroup parent) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.layout_item);
            mainImage = (ImageView) itemView.findViewById(R.id.image_main);
            mainImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onImageClick(paths, getAdapterPosition());
                }
            });
            selectImage = (ImageView) itemView.findViewById(R.id.image_select);
            selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageModel model = items.get(getAdapterPosition());
                    model.setSelected(!model.isSelected());
                    items.set(getAdapterPosition(), model);
                    notifyItemChanged(getAdapterPosition());
                    callback.onImageSelectClick(getAdapterPosition(), model);
                }
            });

            layout.getLayoutParams().height = size;
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_progressbar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
