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
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lenovo on 3/31/2017.
 */

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.PeopleViewHolder> {

    public interface PeopleItemCallback {

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

        public PeopleViewHolder(View itemView) {
            super(itemView);

            optionsImage = (ImageView) itemView.findViewById(R.id.image_options);
            avatarImage = (CircleImageView) itemView.findViewById(R.id.circular_image_avatar);
            name = (TextView) itemView.findViewById(R.id.text_name);

            // TODO optionsImage should show popup menu
        }
    }
}
