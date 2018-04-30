package com.example.rajat.cryptocurrency;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chivu on 23/3/18.
 */

public interface ApiInterfaceformarket {

    @GET("top-headlines?sources=google-news-in&apiKey=24e5d521314646b38aca31ab13a74ff5")
    Call<ResponseBody> getJSON();
}
