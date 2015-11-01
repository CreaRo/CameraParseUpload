package rish.crearo.cameraparse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rish.crearo.cameraparse.adapters.HomeCardRecycler;
import rish.crearo.cameraparse.elements.HomeCardElement;

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
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HomeCardRecycler(HomeActivity.this, getDataSet(), (RelativeLayout) findViewById(R.id.home_rellay));
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<HomeCardElement> getDataSet() {
        ArrayList<HomeCardElement> imageCards = new ArrayList<>();

        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath() + "/cameraparse");

        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();

            for (int i = 0; i < listFiles.length; i++) {
                imageCards.add(new HomeCardElement(listFiles[i].getName(), listFiles[i].getAbsolutePath()));
            }
        }
        return imageCards;
    }
}