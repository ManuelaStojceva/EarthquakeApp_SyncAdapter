package com.earthquakeapp.syncadapter;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRecyclerAdapter;
import models.Earthquake;
import sync.StubProvider;
import sync.SyncAdapter;
import sync.SyncUtils;

public class MainActivity extends FragmentActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    Account mAccount;
    private String place;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private String magnitude;
    CursorLoader cursorLoader;
    private MyRecyclerAdapter recyclerAdapter;
    private Earthquake earthquakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        mAccount = SyncUtils.CreateSyncAccount(this);
        getSupportLoaderManager().initLoader(1, null, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ImageView refresh = (ImageView) findViewById(R.id.imgrefresh);
        progressBar.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                SyncUtils.TriggerRefresh(mAccount);
                Toast.makeText(getApplicationContext(), "REFRESH",
                        Toast.LENGTH_LONG).show();
            }
        });
        earthquakeList = new Earthquake();
    }
    public void updateEarthquakeList() {
        ContentResolver cr = getContentResolver ();
        Cursor  cursor = cr.query (StubProvider.CONTENT_URI , null,null, null, "place");
        cursor.moveToFirst();

        ArrayList<String> places = new ArrayList<String>();
        ArrayList<String> magnitudes = new ArrayList<String>();
        List<String> temp = new ArrayList<String>();
        List<String> tempMag = new ArrayList<String>();
        ArrayList<String> oldPlaces = SyncAdapter.newPlaces;
        ArrayList<String> oldMag = SyncAdapter.newMagnitude;
        if (cursor.moveToFirst()) {
            do{
                place = cursor.getString(cursor.getColumnIndex("place"));
                magnitude = cursor.getString(cursor.getColumnIndex("magnitude"));

                if(!useLoop(places, place)) {
                    places.add(place);
                }
                if(!checkMagnitude(magnitudes, magnitude)){
                    magnitudes.add(magnitude);
                }
                temp.addAll(places);
                tempMag.addAll(magnitudes);
                temp.removeAll(oldPlaces);
                tempMag.removeAll(oldMag);
                places.removeAll(temp);
                magnitudes.removeAll(tempMag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        recyclerAdapter = new MyRecyclerAdapter(getApplicationContext(),places, magnitudes);
        mRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();

    }
    public static boolean useLoop(ArrayList<String> arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }
    public static boolean checkMagnitude(ArrayList<String> arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.imgrefresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        cursorLoader= new CursorLoader(this, Uri.parse("content://sync/cte"), null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        updateEarthquakeList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

    }
}
