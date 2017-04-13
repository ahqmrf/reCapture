package apps.ahqmrf.recapture.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.activity.MainActivity;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lenovo on 3/31/2017.
 */

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.PeopleViewHolder> {

    public interface PeopleItemCallback {
        void onClickPeople(People people);

        void onClickDelete(People people);
    }

    private Context mContext;
    private PeopleItemCallback mCallback;
    private ArrayList<People> items;

    public PeopleListAdapter(Context mContext, PeopleItemCallback mCallback, ArrayList<People> items) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.items = items;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_people, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        People item = items.get(position);
        holder.name.setText(item.getName());
        if(item.getAvatar() == null) {
            holder.avatarImage.setImageResource(R.drawable.ic_account_circle_24dp);
        } else {
            ImageLoader.getInstance().displayImage(
                    "file://" + item.getAvatar(),
                    holder.avatarImage,
                    MyDisplayImageOptions.getInstance().getDisplayImageOptions()
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        ImageView optionsImage;
        CircleImageView avatarImage;
        TextView name;
        View layout;

        public PeopleViewHolder(View itemView) {
            super(itemView);

            optionsImage = (ImageView) itemView.findViewById(R.id.image_options);
            optionsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showContextMenu();
                }
            });
            avatarImage = (CircleImageView) itemView.findViewById(R.id.circular_image_avatar);
            name = (TextView) itemView.findViewById(R.id.text_name);
            layout = itemView.findViewById(R.id.layout_people);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClickPeople(items.get(getAdapterPosition()));
                }
            });

            // TODO optionsImage should show popup menu
        }

        private void showContextMenu() {
            PopupMenu popupMenu = new PopupMenu(mContext, optionsImage);
            popupMenu.inflate(R.menu.memory_context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_view:
                            mCallback.onClickPeople(items.get(getAdapterPosition()));
                            break;
                        case R.id.menu_delete:
                            deletePeople();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        private void deletePeople() {
            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.deleteTitle))
                    .setMessage(
                            mContext.getResources().getString(R.string.promptDeletePeople))
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
