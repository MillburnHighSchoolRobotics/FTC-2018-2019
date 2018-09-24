package virtualRobot.hardware;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;

/*
The virtual Motor component
 */
public class Motor {
    protected SallyJoeBot robot = Command.ROBOT;
    public static final double MAX_POWER = 1;
    public static final double STATIONARY = 0;
    Sensor position;
    boolean positionReverse;
    MotorConfigurationType motorType;
    private boolean alreadySet = false;

    DcMotor.RunMode runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;


    private volatile double power;
    private volatile int target;
    private volatile boolean busy;

    public Motor() {
        power = 0;
        target = 0;
        busy = false;
        position = new Sensor();
        positionReverse = false;
    }

    public synchronized double getPower() {
//		if (robot != null) {
//			robot.addToTelemetry("motorTime: ", System.currentTimeMillis());
//		}
        return power;
    }

    public synchronized void setPower(double newPower) {
        if (Double.isNaN(newPower)) {
            throw new IllegalArgumentException("Motor power cannot be NaN");
        }
        power = newPower;
        if (power > MAX_POWER) {
            power = 1;
        }

        if (power < -MAX_POWER) {
            power = -1;
        }
    }

    public synchronized void setTargetPosition(int position) {
        target = position;
    }

    public synchronized int getTargetPosition() {
        return target;
    }

    public synchronized void setBusy(boolean b) {
        if (!alreadySet) {
            this.busy = b;
        } else {
            this.busy = true;
            alreadySet = false;
        }
    }

    public synchronized boolean isBusy() {
        return busy;
    }

    public synchronized Motor setMotorType(MotorConfigurationType type) {
        motorType = type;
        return this;
    }

    public synchronized void setMode(DcMotor.RunMode mode) {
        runMode = mode;
        if (mode == DcMotor.RunMode.RUN_TO_POSITION) {
            busy = true;
            alreadySet = true;
        }
    }

    public synchronized DcMotor.RunMode getMode() {
        return runMode;
    }

    public synchronized MotorConfigurationType getMotorType() {
        return motorType;
    }

    public synchronized void setPosition(int position) {
        this.position.setRawValue((positionReverse ? -1 : 1) * position);
    }

    public synchronized void setPositionReversed(boolean isRev) {
        positionReverse = isRev;
    }

    public synchronized boolean isPositionReversed() {
        return positionReverse;
    }

    public synchronized int getPosition() {
        return (int) position.getValue();
    }

    public synchronized int getRawValue() {
        return (int) position.getRawValue();
    }

    public synchronized void clearEncoder() {
        position.clearValue();
    }
}