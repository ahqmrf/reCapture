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
import apps.ahqmrf.recapture.model.PeopleToTag;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class PeopleToTagListAdapter extends RecyclerView.Adapter<PeopleToTagListAdapter.PeopleToTagViewHolder> {

    public interface TagCallback {
        void onCheckBoxClick(int position, PeopleToTag peopleToTag);
    }

    private Context mContext;
    private TagCallback mCallback;
    private ArrayList<PeopleToTag> items;

    public PeopleToTagListAdapter(Context mContext, TagCallback mCallback, ArrayList<PeopleToTag> items) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.items = items;
    }

    @Override
    public PeopleToTagListAdapter.PeopleToTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_people_to_tag, parent, false);
        return new PeopleToTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeopleToTagListAdapter.PeopleToTagViewHolder holder, int position) {
        PeopleToTag model = items.get(position);
        People item = model.getPeople();
        if(item.getAvatar() == null) {
            holder.avatar.setImageResource(R.drawable.ic_account_circle_24dp);
        } else {
            ImageLoader.getInstance().displayImage(
                    "file://" + item.getAvatar(),
                    holder.avatar,
                    MyDisplayImageOptions.getInstance().getDisplayImageOptions()
            );
        }
        holder.name.setText(item.getName());
        holder.relation.setText(item.getRelation());
        if(model.isSelected()) {
            holder.checkBox.setImageResource(R.drawable.ic_check_box_red_24dp);
        } else holder.checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_red_24dp);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PeopleToTagViewHolder extends RecyclerView.ViewHolder {

        ImageView checkBox;
        CircleImageView avatar;
        TextView name, relation;

        public PeopleToTagViewHolder(final View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.text_name);
            relation = (TextView) itemView.findViewById(R.id.text_relation);
            checkBox = (ImageView) itemView.findViewById(R.id.image_check);
            avatar = (CircleImageView) itemView.findViewById(R.id.circular_image_avatar);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    PeopleToTag model = items.get(position);
                    model.setSelected(!model.isSelected());
                    items.set(position, model);
                    notifyItemChanged(position);
                    mCallback.onCheckBoxClick(position, model);
                }
            });
        }
    }
}
