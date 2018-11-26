package org.firstinspires.ftc.teamcode.watchdog;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.BuildConfig;

import java.io.IOException;

import retrofit2.Retrofit;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;

public class PIDValueWatchdog extends Watchdog {
    CTelemetry ctel;
    public PIDValueWatchdog(Thread parentThread, HardwareMap hardwareMap) {
        super(parentThread, hardwareMap);
        ctel = new Retrofit.Builder()
                .baseUrl(BuildConfig.CTELEM_SERVER_IP)
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
    }
    @Override
    protected void loop() {
        try {
            double p = Double.parseDouble(ctel.getValue("p").execute().body());
            double i = Double.parseDouble(ctel.getValue("i").execute().body());
            double d = Double.parseDouble(ctel.getValue("d").execute().body());
            double target = Double.parseDouble(ctel.getValue("target").execute().body());
            WatchdogManager.getInstance().setValue("p", p);
            WatchdogManager.getInstance().setValue("i", i);
            WatchdogManager.getInstance().setValue("d", d);
            WatchdogManager.getInstance().setValue("target", target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
