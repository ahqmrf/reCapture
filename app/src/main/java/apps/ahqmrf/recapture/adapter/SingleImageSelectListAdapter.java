package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

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
    public void onBindViewHolder(SingleImageSelectListAdapter.ImageViewHolder holder, int position) {
        String model = items.get(position);
        ImageLoader.getInstance().displayImage(
                "file://" + model,
                holder.mainImage,
                MyDisplayImageOptions.getInstance().getDisplayImageOptions()
        );
        if(position == selectedId) {
            holder.mainImage.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.mainImage.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView mainImage;

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

            layout.getLayoutParams().height = layout.getLayoutParams().width = size;
        }
    }
}
