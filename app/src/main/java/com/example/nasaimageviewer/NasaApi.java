package com.example.nasaimageviewer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for NASA API calls.
 */
public interface NasaApi {
    @GET("planetary/apod")
    Call<NasaImage> getImage(@Query("api_key") String apiKey, @Query("date") String date);
}
