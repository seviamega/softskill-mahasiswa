package com.sidomafit.dosen.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAR5C74RA:APA91bEdJhL87eR5F2V8IuUhyicUStz5D42i3iaTRucnBPKiFu2nj0u-JMsLH2tZ-ioqaay3VmHgZSKXQN4V3ZTIdsgJmC-DN94rMYUDn2q5isuWOSzxD1LL6k1Hv0emNfrQ0E-h1qaZ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNofirication(@Body Sender body);
}
