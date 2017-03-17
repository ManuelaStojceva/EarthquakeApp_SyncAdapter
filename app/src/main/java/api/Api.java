package api;

import models.Earthquake;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public interface Api {
    /**
     * using retrofit library to get json data from Url
     * @return sync result from Url
     */
    @GET("summary/all_hour.geojson")
    Call<Earthquake> getEarthquake();
}
