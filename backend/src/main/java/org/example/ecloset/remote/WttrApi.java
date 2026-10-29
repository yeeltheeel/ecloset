package org.example.ecloset.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WttrApi {

    @GET("{location}")
    Call<ResponseBody> getCurrentWeather(
            @Path(value = "location", encoded = true) String location,
            @Query("format") String format
    );
}