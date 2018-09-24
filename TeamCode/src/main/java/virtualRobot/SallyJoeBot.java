package virtualRobot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.IMU;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.hardware.Servo;
import virtualRobot.hardware.StateSensor;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;
import virtualRobot.telemetry.NonInterferingCallback;
import virtualRobot.utils.GlobalUtils;


@SuppressWarnings("unchecked") //lmao
/**
 * Created by DOSullivan on 9/14/16.
 * All of our our virtual hardware and there getters are housed in SallyJoeBot
 */
public class SallyJoeBot {
    //Robot Constants
    public volatile double initialBattery;
    public final double wheelDiameter = 5;
    public final double botWidth = 5;
    public final double botLength = 5;

    //Data to pass to UpdateThread
    private ArrayList<String> robotProgress;
    private ConcurrentHashMap<String, Object> telemetry;

    //Motors and Servos
    @UpdateMotor(name = "leftFront", direction = DcMotorSimple.Direction.REVERSE)
    private Motor LFMotor;
    @UpdateMotor(name = "leftBack", direction = DcMotorSimple.Direction.REVERSE)
    private Motor LBMotor;
    @UpdateMotor(name = "rightFront")
    private Motor RFMotor;
    @UpdateMotor(name = "rightBack")
    private Motor RBMotor;
    @UpdateMotor(name = "rollerLeft", enabled = true)
    private Motor rollerLeft;
    @UpdateMotor(name = "rollerRight", direction = DcMotorSimple.Direction.REVERSE, enabled = true)
    private Motor rollerRight;
    @UpdateMotor(name = "lift", enabled = true)
    private Motor lift;
    @UpdateServo(name = "flipper", initpos = 0.65, enabled = true)
    private Servo flipper;
    @UpdateServo(name = "rollerLiftLeft",initpos = 0, enabled = true)
    private Servo rollerLiftLeft;
    @UpdateServo(name = "rollerLiftRight",initpos = 1, enabled = true)
    private Servo rollerLiftRight;
    @UpdateMotor(name = "relicArmWinch", direction = DcMotorSimple.Direction.REVERSE, enabled = true)
    private Motor relicArmWinch;
    @UpdateServo(name = "relicArmWrist", initpos = 1, enabled = true)
    private Servo relicArmWrist;
    @UpdateServo(name = "relicArmClaw", enabled = true)
    private Servo relicArmClaw;
    @UpdateColorSensor(name = "jewelColorSensor", enabled = true)
    private DumbColorSensor colorSensor;
    @UpdateServo(name = "jewelArm", initpos = 0.90, enabled = true)
    private Servo jewelServo;
    @UpdateServo(name = "jewelHitter", initpos = 0.5, enabled = true)
    private Servo jewelHitter;
    @UpdateServo(name = "phoneServo", initpos = 0.95, enabled = true)
    private Servo phoneServo;
    //Sensors
    private IMU imu;
    private Sensor voltageSensor;
    private JoystickController joystickController1, joystickController2;
    private StateSensor stateSensor;

    //CTelemetry
    private CTelemetry ctel;
    private final String ipaddr = org.firstinspires.ftc.teamcode.BuildConfig.CTELEM_SERVER_IP;
    private ConcurrentHashMap<String, Call> endpointController;

    public static double COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    public static double DRIVE_GEAR_REDUCTION;     // This is < 1.0 if geared UP
    public static double WHEEL_DIAMETER_INCHES;     // For figuring circumference
    public static double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
    public static double wheelbase;

    //Motors, sensors, servos instantiated (e.g Motor = new Motor(), some positions can also be set if desired
    public SallyJoeBot() {
        joystickController1 = new JoystickController();
        joystickController2 = new JoystickController();
        voltageSensor = new Sensor();
        robotProgress = new ArrayList<String>();
        telemetry = new ConcurrentHashMap<>();
        stateSensor = new StateSensor();
        imu = new IMU();

        LFMotor = new Motor();
        LBMotor = new Motor();
        RFMotor = new Motor();
        RBMotor = new Motor();

        lift = new Motor();
        flipper = new Servo();
        rollerLiftLeft = new Servo();
        rollerLiftRight = new Servo();

        rollerLeft = new Motor();
        rollerRight = new Motor();

        relicArmWinch = new Motor();
        relicArmWrist = new Servo();
        relicArmClaw = new Servo();

        jewelServo = new Servo();
        jewelHitter = new Servo();
        colorSensor = new DumbColorSensor();

        phoneServo = new Servo();
        //capLift = new SyncedMotors(LiftLeftMotor, LiftRightMotor, LiftLeftEncoder, LiftRightEncoder, KP, KI, KD, SyncedMotors.SyncAlgo.POSITION);
        //capLift.setRatio(1);

    }
    public synchronized void updateValues() {
        COUNTS_PER_MOTOR_REV = LFMotor.getMotorType().getTicksPerRev();
        DRIVE_GEAR_REDUCTION = 1;
        WHEEL_DIAMETER_INCHES = 4;
        COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
        wheelbase = 27.8058441;
    }
    //All of Autonomous and TeleopRobot's functions are created e.g. (public synchronized Motor getMotor() {return Motor;}

    public synchronized DumbColorSensor getColorSensor() {
        return colorSensor;
    }

    public synchronized Sensor getVoltageSensor() {
        return voltageSensor;
    }

    public synchronized IMU getImu() {
        return imu;
    }

    public synchronized Motor getLFMotor() {
        return LFMotor;
    }

    public synchronized Motor getLBMotor() {
        return LBMotor;
    }

    public synchronized Motor getRFMotor() {
        return RFMotor;
    }

    public synchronized Motor getRBMotor() {
        return RBMotor;
    }

    public synchronized Motor getRelicArmWinch() {
        return relicArmWinch;
    }

    public synchronized Servo getRelicArmWrist() {
        return relicArmWrist;
    }

    public synchronized Servo getRelicArmClaw() {
        return relicArmClaw;
    }

    public synchronized Servo getJewelServo() {
        return jewelServo;
    }

    public synchronized void moveJewelServo(boolean isOpen) {
        jewelServo.setPosition(isOpen ? 0.42 : 0.90);
    }

    public synchronized void moveJewelRotater(int dir) {
        if (dir < 0)
            jewelHitter.setPosition(0.75);
        else if (dir > 0)
            jewelHitter.setPosition(0.25);
        else
            jewelHitter.setPosition(0.5);
    }

    public synchronized StateSensor getStateSensor() {
        return stateSensor;
    }

    public synchronized void stopMotors() {
        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);
    }

    public void encoderDrive(double speed, double LFPos, double LBPos, double RFPos, double RBPos, double timeoutS) {
        // Ensure that the opmode is still active
        DcMotor.RunMode LFMode = LFMotor.getMode();
        DcMotor.RunMode LBMode = LBMotor.getMode();
        DcMotor.RunMode RFMode = RFMotor.getMode();
        DcMotor.RunMode RBMode = RBMotor.getMode();

        Log.d("Progress", "hit encoderDrive " + timeoutS + " " + LFPos);
        int LFTarget = (int) (LFMotor.getPosition() + LFPos);
        int LBTarget = (int) (LBMotor.getPosition() + LBPos);
        int RFTarget = (int) (RFMotor.getPosition() + RFPos);
        int RBTarget = (int) (RBMotor.getPosition() + RBPos);
        LFMotor.setTargetPosition(LFTarget);
        LBMotor.setTargetPosition(LBTarget);
        RFMotor.setTargetPosition(RFTarget);
        RBMotor.setTargetPosition(RBTarget);

        // Turn On RUN_TO_POSITION
        LFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        GlobalUtils.runtime.reset();
        LFMotor.setPower(Math.abs(speed));
        LBMotor.setPower(Math.abs(speed));
        RFMotor.setPower(Math.abs(speed));
        RBMotor.setPower(Math.abs(speed));

        while ((GlobalUtils.runtime.milliseconds() < timeoutS) &&
                (LFMotor.isBusy() && LBMotor.isBusy() && RFMotor.isBusy() && RBMotor.isBusy()) &&
                (!Thread.currentThread().isInterrupted())) {
        }
        // Stop all motion;
        stopMotors();

        // Turn off RUN_TO_POSITION
        LFMotor.setMode(LFMode);
        LBMotor.setMode(LBMode);
        RFMotor.setMode(RFMode);
        RBMotor.setMode(RBMode);
        //  sleep(250);   // optional pause after each move
    }

    public synchronized void moveRollerLifts(boolean isOpen) {
        rollerLiftLeft.setPosition(isOpen ? 0 : 1);
        rollerLiftRight.setPosition(isOpen ? 1 : 0);
    }

    public synchronized JoystickController getJoystickController1() {
        return joystickController1;
    }

    public synchronized JoystickController getJoystickController2() {
        return joystickController2;
    }

    public synchronized void addToProgress(String s) {
        robotProgress.add(s);
    }

    public synchronized ArrayList<String> getProgress() {
        return robotProgress;
    }

    public synchronized void setRollerPower(double power) {
        rollerLeft.setPower(power);
        rollerRight.setPower(power);
    }

    public synchronized void addToTelemetry(String s, Object arg) {
        telemetry.put(s, arg);
    }

    public synchronized ConcurrentHashMap<String, Object> getTelemetry() {
        return telemetry;
    }

    public synchronized void initCTelemetry() {
        ctel = new Retrofit.Builder()
                .baseUrl(ipaddr)
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
        endpointController = new ConcurrentHashMap<String, Call>();
    }

    public synchronized @NonNull Call reserveEndpoint(String endpoint, Call call) {
        if (!endpointController.contains(endpoint) || endpointController.get(endpoint) == null) {
            endpointController.put(endpoint, call);
            return call;
        } else {
            return endpointController.get(endpoint);
        }
    }

    public synchronized Call getEndpointReservation(String endpoint) {
        return endpointController.get(endpoint);
    }

    public synchronized void releaseEndpoint(String endpoint) {
        endpointController.remove(endpoint);
    }

    public synchronized boolean safeCall(Call call, boolean override) {
        String endpoint = call.request().url().encodedPath();
        if (getEndpointReservation(endpoint) == null) {
            reserveEndpoint(endpoint, call);
            call.enqueue(new NonInterferingCallback());
            return true;
        } else if (override) {
            getEndpointReservation(endpoint).cancel();
            releaseEndpoint(endpoint);
            reserveEndpoint(endpoint, call); //lol
            call.enqueue(new NonInterferingCallback());
            return true;
        } else {
            return false;
        }
    }

    public synchronized CTelemetry getCTelemetry() {
        return ctel;
    }

    public Servo getJewelHitter() {
        return jewelHitter;
    }

    public Motor getRollerRight() {
        return rollerRight;
    }

    public Motor getRollerLeft() {
        return rollerLeft;
    }

    public Motor getLift() {
        return lift;
    }

    public Servo getFlipper() {
        return flipper;
    }

    public synchronized void moveFlipper(boolean isOpen) {
        flipper.setPosition(isOpen ? 1 : 0.65);
    }

    public Servo getRollerLiftLeft() {
        return rollerLiftLeft;
    }

    public Servo getRollerLiftRight() {
        return rollerLiftRight;
    }

    public Servo getPhoneServo() {
        return phoneServo;
    }

    public enum Team {
        BLUE, RED
    }
}
