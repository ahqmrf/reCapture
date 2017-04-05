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

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.SystemHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.MemoryViewHolder> {

    public interface MemoryClickCallback {

    }

    private Context mContext;
    private MemoryClickCallback mCallback;
    private ArrayList<Memory> items;

    public MemoryListAdapter(Context mContext, MemoryClickCallback mCallback, ArrayList<Memory> items) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.items = items;
    }

    @Override
    public MemoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_memory, parent, false);
        return new MemoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemoryViewHolder holder, int position) {
        Memory memory = items.get(position);
        if(memory.getIconPath() == null) holder.icon.setImageResource(R.drawable.re_capture);
        else {
            ImageLoader.getInstance().displayImage(
                    "file://" + memory.getIconPath(),
                    holder.icon,
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
        }

        holder.title.setText(memory.getTitle());
        holder.description.setText(memory.getDescription());
        String str = new SystemHelper(mContext).get12HourTimeStamp(memory.getTime());
        holder.time.setText(str);
        holder.date.setText(memory.getTime().getDate());
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class MemoryViewHolder extends RecyclerView.ViewHolder {

        CircleImageView icon;
        TextView title, description, date, time;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        public MemoryViewHolder(View itemView) {
            super(itemView);

            icon = (CircleImageView) itemView.findViewById(R.id.circular_image_icon);
            title = (TextView) itemView.findViewById(R.id.text_title);
            description = (TextView) itemView.findViewById(R.id.text_description);
            time = (TextView) itemView.findViewById(R.id.text_time);
            date = (TextView) itemView.findViewById(R.id.text_date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_progressbar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
