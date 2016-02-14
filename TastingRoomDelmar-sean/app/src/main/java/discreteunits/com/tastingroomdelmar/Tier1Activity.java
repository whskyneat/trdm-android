package discreteunits.com.tastingroomdelmar;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parseUtils.ListObject;

public class Tier1Activity extends AppCompatActivity {
    private static final String TAG = Tier1Activity.class.getSimpleName();

    DrawerLayout drawer;
    ArrayList<ListObject> listItem = new ArrayList<>();
    Tier1ListViewAdapter adapter;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tier1);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);

        final ImageView mIVUp = (ImageView) findViewById(R.id.up_button);
        mIVUp.setVisibility(View.GONE);

        final AssetManager assetManager = getAssets();
        final Typeface nexarust = Typeface.createFromAsset(assetManager, "fonts/nexarust/NexaRustScriptL-0.otf");
        final Typeface bebas = Typeface.createFromAsset(assetManager, "fonts/bebas/BebasNeue Regular.otf");

        final TextView mTVPreviousActivityName = (TextView) findViewById(R.id.tv_prev_activity);
        final TextView mTVCurrentActivityName = (TextView) findViewById(R.id.tv_curr_activity);

        mTVPreviousActivityName.setText("Del Mar");
        mTVPreviousActivityName.setTypeface(nexarust);

        mTVCurrentActivityName.setText("TASTING ROOM");
        mTVCurrentActivityName.setTypeface(bebas);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ImageButton mImageButtonDrawer = (ImageButton) findViewById(R.id.nav_button);

        final ImageButton mImageButtonTab = (ImageButton) findViewById(R.id.current_order);

        mImageButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.pb_tier1);
        mProgressBar.setVisibility(View.VISIBLE);

        final ListView listView = (ListView) findViewById(R.id.lv_tier1);

        adapter = new Tier1ListViewAdapter(this, listItem);

        listView.setAdapter(adapter);

        getListFromParse();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "position " + position + " clicked");

                Intent intent = new Intent(Tier1Activity.this, Tier2Activity.class);
                intent.putExtra("TIER2_DEST", listItem.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void getListFromParse() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tier1");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    for(ParseObject objects : objectList) {
                        Log.d(TAG, "parse object name : " + objects.getString("name"));
                        listItem.add(new ListObject(objects.getInt("sortOrder"), objects.getString("name")));
                    }

                    Collections.sort(listItem);
                    adapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}