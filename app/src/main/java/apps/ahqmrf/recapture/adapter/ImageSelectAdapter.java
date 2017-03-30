package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.ImageModel;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

/**
 * Created by Lenovo on 3/30/2017.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter <ImageSelectAdapter.ImageViewHolder> {

    public interface ImageSelectCallback {
        void onImageClick(int position, ImageModel model);
    }

    private Context context;
    private ImageSelectCallback callback;
    private ArrayList<ImageModel> items;
    private int size;

    public ImageSelectAdapter(Context context, ImageSelectCallback callback, ArrayList<ImageModel> items, int size) {
        this.context = context;
        this.callback = callback;
        this.items = items;
        this.size = size;
    }

    @Override
    public ImageSelectAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_gallery_select_image, parent, false);
        return new ImageViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(ImageSelectAdapter.ImageViewHolder holder, int position) {
        ImageModel model = items.get(position);
        ImageLoader.getInstance().displayImage(
                "file://" + model.getFilePath(),
                holder.mainImage,
                MyDisplayImageOptions.getInstance().getDisplayImageOptions()
        );

        if(model.isSelected()) {
            holder.selectImage.setImageResource(R.drawable.ic_done_green_24dp);
            holder.selectImage.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.selectImage.setImageResource(R.drawable.ic_done_white_24dp);
            holder.selectImage.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_transparent));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView mainImage, selectImage;

        public ImageViewHolder(final View itemView, final ViewGroup parent) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.layout_item);
            mainImage = (ImageView) itemView.findViewById(R.id.image_main);
            selectImage = (ImageView) itemView.findViewById(R.id.image_select);
            selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageModel model = items.get(getAdapterPosition());
                    model.setSelected(!model.isSelected());
                    items.set(getAdapterPosition(), model);
                    notifyItemChanged(getAdapterPosition());
                    callback.onImageClick(getAdapterPosition(), model);
                }
            });

            layout.getLayoutParams().height = layout.getLayoutParams().width = size;
        }
    }
}
