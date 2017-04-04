package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
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
    public void onBindViewHolder(MemoryViewHolder holder, int position) {
        Memory memory = items.get(position);
        if(memory.getIconPath() == null) holder.icon.setImageResource(R.drawable.re_capture);
        else {
            ImageLoader.getInstance().displayImage(
                    "file://" + memory.getIconPath(),
                    holder.icon,
                    MyDisplayImageOptions.getInstance().getDisplayImageOptions()
            );
        }

        holder.title.setText(memory.getTitle());
        holder.description.setText(memory.getDescription());
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class MemoryViewHolder extends RecyclerView.ViewHolder {

        CircleImageView icon;
        TextView title, description;
        ImageView more;

        public MemoryViewHolder(View itemView) {
            super(itemView);

            icon = (CircleImageView) itemView.findViewById(R.id.circular_image_icon);
            title = (TextView) itemView.findViewById(R.id.text_title);
            description = (TextView) itemView.findViewById(R.id.text_description);
            more = (ImageView) itemView.findViewById(R.id.image_more);
        }
    }
}
