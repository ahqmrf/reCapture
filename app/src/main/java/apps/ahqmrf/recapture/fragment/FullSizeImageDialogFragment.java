package apps.ahqmrf.recapture.fragment;

import android.app.Dialog;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.ahqmrf.recapture.R;

/**
 * Created by Iftekhar on 10/4/16.
 */

public class FullSizeImageDialogFragment extends DialogFragment {

    public static final String TAG = "FullSizeImage";

    private static final String ARG_IMAGE_URL = "_image_url";

    private ImageView mImageView;

    public static FullSizeImageDialogFragment newInstance(String imageUri) {
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUri);
        FullSizeImageDialogFragment fragment = new FullSizeImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FullSizeImageDialogFragment() {
        // required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        window.setLayout(size.x, size.y);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_full_size_image, container, false);
        viewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mImageView = (ImageView) viewRoot.findViewById(R.id.fullSizeImageView);
        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_IMAGE_URL)) {
            String imageUri = args.getString(ARG_IMAGE_URL);
            if (imageUri != null) {
                ImageLoader.getInstance().displayImage("file://" + imageUri.toString(), mImageView);
            }
        }
    }
}
