package srm.isca.com.isca;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;


public class CardViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        Animation scaleAnimation = AnimationUtils.loadAnimation
                (getApplicationContext(),R.anim.slide_in_up);
        mRecyclerView.startAnimation(scaleAnimation) ;
        Animation scaleAnimation1 = AnimationUtils.loadAnimation
                (getApplicationContext(),R.anim.slide_out_up);
        mRecyclerView.startAnimation(scaleAnimation1) ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);


            }

        });

    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<>();
        DataObject obj = new DataObject("");
        results.add( obj);
        return results;
    }

}