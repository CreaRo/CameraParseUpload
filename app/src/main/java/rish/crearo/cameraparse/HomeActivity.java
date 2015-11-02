package rish.crearo.cameraparse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rish.crearo.cameraparse.adapters.HomeCardAdapter;
import rish.crearo.cameraparse.elements.HomeCardElement;
import rish.crearo.cameraparse.utils.BasePath;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.home_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar_home)
    Toolbar toolbar;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CapturePhotoActivity.class));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HomeCardAdapter(HomeActivity.this, getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refreshing
        mAdapter = new HomeCardAdapter(HomeActivity.this, getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<HomeCardElement> getDataSet() {
        long start = System.currentTimeMillis();
        ArrayList<HomeCardElement> imageCards = new ArrayList<>();

        String imagesFile = BasePath.getBasePath(getApplicationContext());

        File[] listFiles = new File(imagesFile).listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getName().contains(".png") || listFiles[i].getName().contains(".jpg")) {
                HomeCardElement element = new HomeCardElement(listFiles[i].getName(), listFiles[i].getAbsolutePath(), new File(imagesFile).getAbsolutePath() + "/thumbnails/" + listFiles[i].getName());
                imageCards.add(element);
//                System.out.println(element.title + " | " + element.imagePath + " | " + element.thumbnailPath);
            } else if (listFiles[i].getName().contains(".mp4")) {
                HomeCardElement element = new HomeCardElement(listFiles[i].getName(), listFiles[i].getAbsolutePath(), new File(imagesFile).getAbsolutePath() + "/thumbnails/" + listFiles[i].getName().substring(0, listFiles[i].getName().length() - 4) + ".png");
                imageCards.add(element);
//                System.out.println(element.title + " | " + element.imagePath + " | " + element.thumbnailPath);
            }
        }

        long total = (System.currentTimeMillis() - start) / 1000;
//        System.out.println("Total time to getDataSet() = " + total);
        return imageCards;
    }
}