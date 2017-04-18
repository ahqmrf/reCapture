package apps.ahqmrf.recapture.adapter;

/**
 * Created by Lenovo on 4/18/2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;
import apps.ahqmrf.recapture.util.MyDisplayImageOptions;


public class ImagesPagerAdapter extends PagerAdapter {

    private ArrayList<String> mItems;
    private Context mContext;

    public ImagesPagerAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mItems = items;
    }

    private void updateView(String imageUri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(
                "file://" + imageUri, imageView, MyDisplayImageOptions.getInstance().getDisplayImageOptions());
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        String item = mItems.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_flipping_image, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_main);
        updateView(item, imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
