package rish.crearo.cameraparse.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import rish.crearo.cameraparse.utils.UploadImageParse;

/**
 * Created by rish on 1/11/15.
 */
public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.DataObjectHolder> {

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
                Snackbar.make(view, "Uploading Image to Parse", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public HomeCardAdapter(Context context, ArrayList<HomeCardElement> dataSet, RelativeLayout relativeLayout) {
        this.context = context;
        this.mDataset = dataSet;
        this.rellay = relativeLayout;
        thumbNails = new ArrayList<>();
        final int THUMBSIZE = 64;
        long start = System.currentTimeMillis();

        for (HomeCardElement card : mDataset) {
            Bitmap thumbBitmap = BitmapFactory.decodeFile(card.thumbnailPath);
            thumbNails.add(thumbBitmap);
        }
        long total = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Total time to setAdapter = " + total);
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
        new UploadImageParse(context, element).execute();
    }
}