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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;

/**
 * Created by Lenovo on 3/31/2017.
 */

public class IconSelectionAdapter extends RecyclerView.Adapter<IconSelectionAdapter.IconViewHolder> {

    private Context context;
    private IconSelectCallback callback;
    private ArrayList<File> items;
    private int selectedId;

    public IconSelectionAdapter(Context context, IconSelectCallback callback, ArrayList<File> items) {
        this.context = context;
        this.callback = callback;
        this.items = items;
        this.selectedId = 0;
    }

    public interface IconSelectCallback {
        void onImageSelectClick(int id);

        void onImageClick(String imagePath);
    }

    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_icon, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IconViewHolder holder, int position) {
        File model = items.get(position);
        if (position == selectedId) {
            holder.selectImage.setBackgroundResource(R.drawable.circular_red_bg_item_gallery);
        } else {
            holder.selectImage.setBackgroundResource(R.drawable.circular_black_bg_item_gallery);
        }

        ImageLoader.getInstance().displayImage(
                "file://" + model.getAbsolutePath(),
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


        String info = "Name: " + model.getName() +
                "\nFile path: " + model.getAbsolutePath();
        holder.fileInfo.setText(info);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class IconViewHolder extends RecyclerView.ViewHolder {

        ImageView mainImage, selectImage;
        TextView fileInfo;
        LinearLayout linearLayout;
        ProgressBar progressBar;


        public IconViewHolder(final View itemView) {
            super(itemView);

            mainImage = (ImageView) itemView.findViewById(R.id.image_main);
            mainImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onImageClick(items.get(getAdapterPosition()).getAbsolutePath());
                }
            });
            selectImage = (ImageView) itemView.findViewById(R.id.image_select);
            selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int prev = selectedId;
                    selectedId = getAdapterPosition();
                    notifyItemChanged(prev);
                    notifyItemChanged(selectedId);
                    callback.onImageSelectClick(selectedId);
                }
            });

            fileInfo = (TextView) itemView.findViewById(R.id.text_file_info);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_progressbar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
