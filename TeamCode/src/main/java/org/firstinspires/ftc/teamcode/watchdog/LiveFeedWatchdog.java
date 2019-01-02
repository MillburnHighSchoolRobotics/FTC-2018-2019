package org.firstinspires.ftc.teamcode.watchdog;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;

public class LiveFeedWatchdog extends Watchdog {
    CTelemetry ctel;
    public LiveFeedWatchdog(Thread parentThread, HardwareMap hardwareMap) {
        super(parentThread, hardwareMap);
        ctel = new Retrofit.Builder()
                .baseUrl(BuildConfig.CTELEM_SERVER_IP)
                .addConverterFactory(MatConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(CTelemetry.class);

    }
    @Override
    protected void loop() {

    }
}
