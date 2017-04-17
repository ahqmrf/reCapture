package apps.ahqmrf.recapture.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.Time;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import apps.ahqmrf.recapture.util.SystemHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.MemoryViewHolder> {

    public interface MemoryClickCallback {
        void onClickMemory(Memory memory);
        void onClickDelete(Memory memory);
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
        if(memory.isSpecial()) {
            holder.special.setImageResource(R.drawable.star_golden);
        } else holder.special.setVisibility(View.GONE);
        if(memory.getIconPath() == null) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.icon.setImageResource(R.drawable.re_capture);
        }
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
        String str = memory.getDescription().isEmpty()? "[No description added.]" : memory.getDescription();
        holder.description.setText(str);
        SystemHelper helper = new SystemHelper(mContext);
        Time cur = helper.getCurrentTime();
        int curDay = cur.getDay();
        int memDay = memory.getTime().getDay();
        int cmp = Time.compare(cur, memory.getTime());
        if(cmp == 0) {
            holder.date.setText(helper.get12HourTimeStamp(memory.getTime()));
        } else {
            if(curDay - memDay == 1) {
                holder.date.setText("Yesterday");
            } else holder.date.setText(memory.getTime().getDate());
        }
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public class MemoryViewHolder extends RecyclerView.ViewHolder {

        CircleImageView icon;
        TextView title, description, date;
        LinearLayout linearLayout;
        ProgressBar progressBar;
        RelativeLayout layout;
        ImageView special;

        public MemoryViewHolder(View itemView) {
            super(itemView);

            special = (ImageView) itemView.findViewById(R.id.image_special);
            icon = (CircleImageView) itemView.findViewById(R.id.circular_image_icon);
            title = (TextView) itemView.findViewById(R.id.text_title);
            description = (TextView) itemView.findViewById(R.id.text_description);
            date = (TextView) itemView.findViewById(R.id.text_date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_progressbar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            layout = (RelativeLayout) itemView.findViewById(R.id.relative_memory_layout_item);

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showContextMenu();
                    return true;
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallback != null) {
                        mCallback.onClickMemory(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        private void showContextMenu() {
            PopupMenu popupMenu = new PopupMenu(mContext, description);
            popupMenu.inflate(R.menu.memory_context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_view:
                            mCallback.onClickMemory(items.get(getAdapterPosition()));
                            break;
                        case R.id.menu_delete:
                            deleteMemory();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        private void deleteMemory() {
            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.deleteTitle))
                    .setMessage(
                            mContext.getResources().getString(R.string.promptDeleteMem))
                    .setIcon(
                            ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp))
                    .setPositiveButton(
                            mContext.getResources().getString(R.string.PostiveYesButton),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mCallback.onClickDelete(items.get(getAdapterPosition()));
                                    items.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            })
                    .setNegativeButton(
                            mContext.getResources().getString(R.string.NegativeNoButton),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do Something Here
                                }
                            }).show();
        }
    }
}
