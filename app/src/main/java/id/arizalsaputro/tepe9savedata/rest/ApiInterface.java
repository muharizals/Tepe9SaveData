package id.arizalsaputro.tepe9savedata.rest;

import java.util.ArrayList;

import id.arizalsaputro.tepe9savedata.model.Hewan;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by muharizals on 06/11/2016.
 */

public interface ApiInterface {

    @GET("hewan/{name}")
    Call<ArrayList<Hewan>> getHewan(@Path("name")String name);

}
