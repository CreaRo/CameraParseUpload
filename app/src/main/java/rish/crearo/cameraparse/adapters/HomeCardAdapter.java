package rish.crearo.cameraparse.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
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
                Snackbar.make(view, "Upload to Parse will start shortly", Snackbar.LENGTH_LONG).show();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageCard.imagePath.contains(".png") || imageCard.imagePath.contains(".jpg")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(imageCard.imagePath)), "image/*");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(imageCard.imagePath)), "video/*");
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public HomeCardAdapter(Context context, ArrayList<HomeCardElement> dataSet) {
        this.context = context;
        this.mDataset = dataSet;
        thumbNails = new ArrayList<>();
        final int THUMBSIZE = 64;
        long start = System.currentTimeMillis();

        for (HomeCardElement card : mDataset) {
            Bitmap thumbBitmap = BitmapFactory.decodeFile(card.thumbnailPath);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            thumbBitmap = Bitmap.createBitmap(thumbBitmap, 0, 0, thumbBitmap.getWidth(), thumbBitmap.getHeight(), matrix, true);
            thumbNails.add(thumbBitmap);
        }
        long total = (System.currentTimeMillis() - start) / 1000;
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