package rish.crearo.cameraparse.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rish.crearo.cameraparse.R;
import rish.crearo.cameraparse.elements.HomeCardElement;
import rish.crearo.cameraparse.utils.UploadImage;

/**
 * Created by rish on 1/11/15.
 */
public class HomeCardRecycler extends RecyclerView.Adapter<HomeCardRecycler.DataObjectHolder> {

    private ArrayList<HomeCardElement> mDataset;
    private Context context;
    private ArrayList<Bitmap> thumbNails;

    private RelativeLayout rellay; //necessary call to display snackbar.

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_home_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final HomeCardElement imageCard = mDataset.get(position);
        holder.title.setText(imageCard.title);
        holder.imageView.setImageBitmap(thumbNails.get(position));
        holder.uploadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToServer(imageCard);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public HomeCardRecycler(Context context, ArrayList<HomeCardElement> dataSet, RelativeLayout relativeLayout) {
        this.context = context;
        this.mDataset = dataSet;
        this.rellay = relativeLayout;
        thumbNails = new ArrayList<>();
        final int THUMBSIZE = 64;
        for (HomeCardElement card : mDataset) {
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(card.imageBytes), THUMBSIZE, THUMBSIZE);
            thumbNails.add(thumbImage);
        }
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView title, uploadStatus;
        ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.element_layout);
            title = (TextView) itemView.findViewById(R.id.element_title);
            imageView = (ImageView) itemView.findViewById(R.id.element_imageView);
            uploadStatus = (TextView) itemView.findViewById(R.id.element_upload_status);
        }
    }

    private void uploadImageToServer(final HomeCardElement element) {
        new UploadImage(context, element, rellay).execute();
    }
}