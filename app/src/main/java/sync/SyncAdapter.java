package sync;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import api.Api;
import models.Earthquake;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    static final String LOG_TAG = SyncAdapter.class.getSimpleName();
    public static final String ENDPOINT = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/";

    private Context context;
    private static Retrofit retrofit = null;
    public static ArrayList<String> newPlaces = new ArrayList<String>();
    public static ArrayList<String> newMagnitude = new ArrayList<String>();

    public SyncAdapter(final Context context, final boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
    }

    @SuppressLint("NewApi")
    public SyncAdapter(final Context context, final boolean autoInitialize,
                       final boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

    }

    @Override
    public void onPerformSync(final Account account, final Bundle extras,
                              final String authority, final ContentProviderClient provider,
                              final SyncResult syncResult) {
        try {
            insertSports(provider);
            new Earthquake();
            Log.i(LOG_TAG, "Data synced successfully");
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Data sync failed. " + e.getMessage());
        }
    }

    private void insertSports(final ContentProviderClient contentProviderClient)
            throws RemoteException, IOException {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(ENDPOINT).addConverterFactory(GsonConverterFactory.create()).build();
        }
        final Api api = retrofit.create(Api.class);
        Call<Earthquake> call = api.getEarthquake();
        call.enqueue(new Callback<Earthquake>() {
            @Override
            public void onResponse(Call<Earthquake> call, Response<Earthquake> response) {
                Log.d("RESPONSE:", response.body().toString());
                newPlaces.clear();
                newMagnitude.clear();
                for (int i = 0; i < response.body().features.size(); i++) {
                    final String place = response.body().features.get(i).properties
                            .getPlace();
                    String magnitude = String.valueOf(response.body().features.get(i).properties
                            .getMag());
                    newPlaces.add(place);
                    newMagnitude.add(magnitude);
                    ContentValues values = new ContentValues();
                    values.put(StubProvider.place, place);
                    values.put(StubProvider.magnitude, magnitude);
                    try {
                        contentProviderClient.insert(StubProvider.CONTENT_URI, values);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Earthquake> call, Throwable t) {
                if (call != null) {
                    Log.d("RESPONSE ERROR:", call.toString());
                }
            }
        });
    }
}
