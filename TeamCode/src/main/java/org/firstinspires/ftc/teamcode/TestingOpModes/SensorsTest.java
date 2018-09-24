package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ethan Mak on 2/17/2018.
 */

@Autonomous(name = "Sensor: Sensor Test NO BACKEND",group = "Sensor")
public class SensorsTest extends OpMode {
    String data;
    Set<Map.Entry<String, DcMotor>> motors;
    Set<Map.Entry<String, ColorSensor>> colorSensor;
    Set<Map.Entry<String, UltrasonicSensor>> sonars;
    Set<Map.Entry<String, AnalogInput>> analog;
//    Set<Map.Entry<String, DigitalChannel>> digital;
    BNO055IMU imu;
    @Override
    public void init() {
        motors = hardwareMap.dcMotor.entrySet();
        for (Map.Entry<String, DcMotor> e : motors) {
            e.getValue().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            e.getValue().setPower(0);
        }
        colorSensor = hardwareMap.colorSensor.entrySet();
        sonars = hardwareMap.ultrasonicSensor.entrySet();
        analog = hardwareMap.analogInput.entrySet();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = null;//new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    @Override
    public void loop() {
        data = "";
        for (Map.Entry<String, DcMotor> e : motors) {
            data += "[" + e.getKey() + "," + e.getValue().getCurrentPosition() + "] ";
        }
        telemetry.addData("Motors: ", data);
        data = "";
        for (Map.Entry<String, ColorSensor> e : colorSensor) {
            data += "[" + e.getKey() + "," + e.getValue().red() + "," + e.getValue().green() + "," + e.getValue().blue() + "] ";
        }
        telemetry.addData("Color Sensor: ", data);
        data = "";
        for (Map.Entry<String, UltrasonicSensor> e : sonars) {
            data += "[" + e.getKey() + "," + e.getValue() + "] ";
        }
        telemetry.addData("Sonars: ", data);
        data = "";
        for (Map.Entry<String, AnalogInput> e : analog) {
            data += "[" + e.getKey() + "," + e.getValue() + "] ";
        }
        telemetry.addData("Analog: ", data);
        telemetry.addData("IMU: ", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).toString());
    }

    @Override
    public void stop() {
        super.stop();
        for (Map.Entry<String, DcMotor> e : motors) {
            e.getValue().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            e.getValue().setPower(0);
        }
    }
}
