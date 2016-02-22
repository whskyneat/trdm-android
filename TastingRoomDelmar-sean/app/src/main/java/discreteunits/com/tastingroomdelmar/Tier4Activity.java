package discreteunits.com.tastingroomdelmar;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parseUtils.ItemListObject;
import parseUtils.ListObject;
import utils.TagManager;

public class Tier4Activity extends AppCompatActivity {
    private static final String TAG = Tier4Activity.class.getSimpleName();

    DrawerLayout drawer;

    ArrayList<String> topListItem = new ArrayList<>();
    ArrayList<ItemListObject> listItem = new ArrayList<>();
    Tier4TopListViewAdapter topAdapter;
    Tier4ListViewAdapter adapter;

    ProgressBar mProgressBar;

    String currentActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tier4);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);

        final ImageView mIVUp = (ImageView) findViewById(R.id.up_button);
        mIVUp.setVisibility(View.VISIBLE);
        mIVUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final AssetManager assetManager = getAssets();
        final Typeface nexarust = Typeface.createFromAsset(assetManager, "fonts/nexarust/NexaRustScriptL-0.otf");

        final TextView mTVPreviousActivityName = (TextView) findViewById(R.id.tv_prev_activity);
        final TextView mTVCurrentActivityName = (TextView) findViewById(R.id.tv_curr_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentActivity = extras.getString("TIER4_DEST");
            String prevActivity = extras.getString("TIER4_ORIG");

            mTVCurrentActivityName.setText(currentActivity);
            mTVCurrentActivityName.setTypeface(nexarust);

            mTVPreviousActivityName.setText(prevActivity);
            mTVPreviousActivityName.setTypeface(nexarust);
            mTVPreviousActivityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mProgressBar = (ProgressBar) findViewById(R.id.pb_tier4);
        mProgressBar.setVisibility(View.VISIBLE);

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

        final ListView topListView = (ListView) findViewById(R.id.lv_top_tier4);
        topAdapter = new Tier4TopListViewAdapter(this, topListItem);
        topListView.setAdapter(topAdapter);

        getTopListFromParse();

        final ListView listView = (ListView) findViewById(R.id.lv_tier4);
        adapter = new Tier4ListViewAdapter(this,listItem);
        listView.setAdapter(adapter);

        getListFromParse();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "position " + position + " clicked");

            }
        });
    }

    @Override
    protected void onDestroy() {
        TagManager.popFromList();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getTopListFromParse() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WineVarietal");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    for(ParseObject objects : objectList) {
                        String name = objects.getString("name");
                        JSONArray tier3JSONArray = objects.getJSONArray("tier3"); // reds, whites,...


                        Log.d(TAG, "parse object name : " + name);
                        try {
                            String objectId = tier3JSONArray.getJSONObject(0).getString("objectId");

                            if (TagManager.isInList(objectId))
                                topListItem.add(name);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    Collections.sort(topListItem);

                    topAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getListFromParse() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Item");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    for(ParseObject objects : objectList) {
                        String name = objects.getString("name");
                        String tag = objects.getString("tag");
                        String altName = objects.getString("alternateName");

                        Log.d(TAG, "parse object name : " + name);

                        listItem.add(new ItemListObject(tag, name, altName));
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