package apps.ahqmrf.recapture.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;

import apps.ahqmrf.recapture.R;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<Fragment> mFragmentsList;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context,  ArrayList<Fragment> fragments) {
        super(fm);
        mContext = context;
        mFragmentsList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    private int[] imageResId = {
            R.drawable.ic_history_white_24dp,
            R.drawable.ic_collections_white_24dp,
            R.drawable.ic_people_white_24dp,
            R.drawable.ic_person_white_24dp,
            R.drawable.ic_settings_white_24dp
    };

// ...

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
