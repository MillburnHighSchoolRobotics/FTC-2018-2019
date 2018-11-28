package virtualRobot.telemetry;

import org.opencv.core.Mat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by david on 11/7/17.
 */

public interface CTelemetry {
    @POST("display/submit")
    Call<Void> sendImage(@Header("window-name") String windowName, @Body Mat img);

    @GET("values")
    Call<String> getValue(@Query("key") String key);
}
