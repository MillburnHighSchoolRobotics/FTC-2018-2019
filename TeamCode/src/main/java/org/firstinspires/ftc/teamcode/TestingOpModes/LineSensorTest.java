package org.firstinspires.ftc.teamcode.TestingOpModes;

//import com.kauailabs.navx.ftc.MPU9250;
import com.kauailabs.navx.ftc.MPU9250;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;


/**
 * Created by 17osullivand on 10/5/16.
 */
//note that to test componenets just create a basic OpMode
@Disabled
@Autonomous(name ="Sensor: Testing All Sensors", group="Sensor")
public class LineSensorTest extends OpMode {
    ColorSensor linetest;
    UltrasonicSensor sonar1, sonar2;
    LightSensor light1, light2, light3, light4;
    private MPU9250 imu;
    @Override
    public void init() {
        linetest = hardwareMap.colorSensor.get("color");
        sonar1 = hardwareMap.ultrasonicSensor.get("sonarLeft");
        sonar2 = hardwareMap.ultrasonicSensor.get("sonarRight");
        light1 = hardwareMap.lightSensor.get("nxtLight1");
        light2 = hardwareMap.lightSensor.get("nxtLight2");
        light3 = hardwareMap.lightSensor.get("nxtLight3");
        light4 = hardwareMap.lightSensor.get("nxtLight4");
//        light1.enableLed(true);
//        light2.enableLed(true);
//        light3.enableLed(true);
//        light4.enableLed(true);

        imu = MPU9250.getInstance(hardwareMap.deviceInterfaceModule.get("dim"), 1);
        imu.zeroPitch();
        imu.zeroRoll();
        imu.zeroYaw();
        //ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        //colorSensor = hardwareMap.colorSensor.get("color");
    }

    @Override
    public void loop() {
       int argb = linetest.argb();
       int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);
        int alpha =(argb & 0xFF000000) >> 24;

        telemetry.addData("Color: ", "RED: " + red + " GREEN: " + green + " BLUE: " + blue + " ALPHA: " + alpha + " argb:" + linetest.argb());
     double headingAngle = imu.getIntegratedYaw();
     double Pitch = imu.getIntegratedPitch();
      double Roll = imu.getIntegratedRoll();
    telemetry.addData("Angle, Pitch, Roll: ", headingAngle + " " + Pitch + " " + Roll);
        telemetry.addData("UltraSound: ", sonar1.getUltrasonicLevel() + " " + sonar2.getUltrasonicLevel());
        telemetry.addData(" LIGHT", light1.getRawLightDetected() + " " + light2.getRawLightDetected() + " " + light3.getRawLightDetected() + " " + light4.getRawLightDetected());
        telemetry.addData("LIGHT REDONE: ", light1.getLightDetected() + " " + light2.getLightDetected() + " " + light3.getLightDetected() + " " + light4.getLightDetected());
    }
    public void stop() {
        imu.close();

    }

}
