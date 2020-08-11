package com.sidomafit.dosen.notifications;

import android.content.Context;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit=null;

    public static Retrofit getClient(String url, Context context)
    {
        if(retrofit==null)
        {

            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
