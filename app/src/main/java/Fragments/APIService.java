package Fragments;

import Notifications.MyResponse;
import Notifications.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAM3sjxtc:APA91bF9GvmU_o9iaPRyLgqx_DnuZ0ZUwOHImriKfDd_w-7sd3BFNHbYmKRblqXUWvJH--dzxvdTRN8GVExEpSvHcrs1k8xwv5H-eXhz2EsWfcaW1p4BWhgtPuxkfWNYQ0semsG11rRM"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
