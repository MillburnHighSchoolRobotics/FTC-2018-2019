package virtualRobot.telemetry;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;

/**
 * Created by david on 2/23/18.
 */

public class NonInterferingCallback<T> implements Callback<T> {
    private final static SallyJoeBot robot = Command.ROBOT;
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d("Call", "response received");
        Call reservation = robot.getEndpointReservation(call.request().url().encodedPath());
        if (reservation != null && reservation.equals(call)) {
            robot.releaseEndpoint(call.request().url().encodedPath());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        Log.d("Call", "response failure");
        Call reservation = robot.getEndpointReservation(call.request().url().encodedPath());
        if (reservation != null && reservation.equals(call)) {
            robot.releaseEndpoint(call.request().url().encodedPath());
        }
    }
}
