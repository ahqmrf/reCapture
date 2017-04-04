package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.SystemHelper;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class GalleryImagesListAdapter extends RecyclerView.Adapter<GalleryImagesListAdapter.ImageViewHolder> {

    private Context mContext;
    private ImageSelectAdapter.ImageSelectCallback mCallback;
    private ArrayList<String> items;
    private Memory memory;
    private int size;

    public GalleryImagesListAdapter(Context mContext, ImageSelectAdapter.ImageSelectCallback mCallback, Memory memory, int size) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.memory = memory;
        this.items = memory.getImages();
        this.size = size;
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
        holder.title.setText(memory.getTitle());
        holder.time.setText(new SystemHelper(mContext).get12HourTimeStamp(memory.getTime()));
        holder.date.setText(memory.getTime().getDate());
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        RelativeLayout layout;
        TextView title, time, date;

        public ImageViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.text_title);
            time = (TextView) itemView.findViewById(R.id.text_time);
            date = (TextView) itemView.findViewById(R.id.text_date);

            mImage = (ImageView) itemView.findViewById(R.id.image_main);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onImageClick(items.get(getAdapterPosition()));
                }
            });

            layout = (RelativeLayout) itemView.findViewById(R.id.layout_item);

            layout.getLayoutParams().height = size;
        }
    }
}
