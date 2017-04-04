package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class GalleryImagesListAdapter extends RecyclerView.Adapter<GalleryImagesListAdapter.ImageViewHolder> {

    private Context mContext;
    private ImageSelectAdapter.ImageSelectCallback mCallback;
    private ArrayList<String> items;
    private int size;
    private boolean horizontal;

    public GalleryImagesListAdapter(Context mContext, ImageSelectAdapter.ImageSelectCallback mCallback, ArrayList<String> items, int size, boolean horizontal) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.items = items;
        this.size = size;
        this.horizontal = horizontal;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gallery_single_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(
                "file://" + items.get(position),
                holder.mImage,
                MyDisplayImageOptions.getInstance().getDisplayImageOptions()
        );
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.image_main);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onImageClick(items.get(getAdapterPosition()));
                }
            });

            if(horizontal) {
                mImage.getLayoutParams().height = size / 2;
                mImage.getLayoutParams().width = size;
            }
            else mImage.getLayoutParams().height = mImage.getLayoutParams().width = size;
        }
    }
}
