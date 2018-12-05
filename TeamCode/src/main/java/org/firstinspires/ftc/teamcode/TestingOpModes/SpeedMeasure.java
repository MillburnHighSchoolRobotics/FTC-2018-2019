package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.JeffBot;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

public class SpeedMeasure extends OpMode {
    private DcMotorEx lf;
    private DcMotorEx rf;
    private DcMotorEx lb;
    private DcMotorEx rb;
    private JeffBot mv;
    private BNO055IMU imu;
    @Override
    public void init() {
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setDirection(REVERSE);
        rb.setDirection(REVERSE);
//        WatchdogManager.getInstance().setHardwareMap(hardwareMap);
//        WatchdogManager.getInstance().provision("IMUWatch", IMUWatchdog.class, "imu");
        mv = new JeffBot(lf, lb, rf, rb);
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = null;//new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        lf.setPower(-1);
        lb.setPower(-1);
        rf.setPower(1);
        rb.setPower(1);
    }

    @Override
    public void loop() {
        telemetry.addData("RotationRate", imu.getAngularVelocity().zRotationRate);
        telemetry.addData("EncoderSpeed", rf.getVelocity());
    }
}
