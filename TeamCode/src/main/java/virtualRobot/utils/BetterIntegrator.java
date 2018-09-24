package virtualRobot.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by ethan on 9/21/17.
 */

public class BetterIntegrator implements BNO055IMU.AccelerationIntegrator {
    private Position currentPosition;
    private Velocity currentVelocity;
    private Acceleration currentAcceleration;

    private BNO055IMU.Parameters parameters;

    public BetterIntegrator() {
        currentPosition = new Position();
        currentVelocity = new Velocity();
        currentAcceleration = new Acceleration();
    }

    @Override
    public void initialize(@NonNull BNO055IMU.Parameters parameters, @Nullable Position initialPosition, @Nullable Velocity initialVelocity) {
        currentPosition = initialPosition == null ? currentPosition : initialPosition;
        currentVelocity = initialVelocity == null ? currentVelocity : initialVelocity;

        this.parameters = parameters;
    }

    @Override
    public Position getPosition() {
        return currentPosition;
    }

    @Override
    public Velocity getVelocity() {
        return currentVelocity;
    }

    @Override
    public Acceleration getAcceleration() {
        return currentAcceleration;
    }

    @Override
    public void update(Acceleration linearAcceleration) {
        if (linearAcceleration.acquisitionTime == 0)
            return;

        if (parameters.loggingEnabled)
            Log.d(parameters.loggingTag, "Position: " + currentPosition.toString() + " Velocity: " + currentVelocity.toString() + " Acceleration: " + currentAcceleration.toString());
    }
}
