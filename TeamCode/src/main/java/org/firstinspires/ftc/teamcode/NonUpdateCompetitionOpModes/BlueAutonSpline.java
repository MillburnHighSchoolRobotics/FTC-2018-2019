package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Spline;

public class BlueAutonSpline extends LinearOpMode {
    DcMotor lf = hardwareMap.dcMotor.get("lf");
    DcMotor lb = hardwareMap.dcMotor.get("lb");
    DcMotor rf = hardwareMap.dcMotor.get("rf");
    DcMotor rb = hardwareMap.dcMotor.get("rb");

    @Override
    public void runOpMode() throws InterruptedException {
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
        for (int x = 0; x < 4; x++) {
            initializeMotor(new DcMotor[]{lf, lb, rf, rb});
        }

        double[] currentPosition = {58.75,82.25};
        double[] samplingPosition = {47,94};
        double[] claimingPosition = {11.75,23.5};
        double[] parkingPosition = {8.8125,99.875};
        Spline.addPoint(currentPosition);
        Spline.addPoint(samplingPosition);
        Spline.addPoint(claimingPosition);
        Spline.addPoint(parkingPosition);

        Spline.interpolate();

        //moving to sampling position
        double buffer = 10;
        while (!((currentPosition[0]+(buffer/2) < samplingPosition[0]) || (currentPosition[0]-(buffer/2) > samplingPosition[0]) &&
                (currentPosition[1]+(buffer/2) < samplingPosition[1]) || (currentPosition[1]-(buffer/2) > samplingPosition[1]))) {
            double movement = 1.0;
            double angle = Spline.getAngle(currentPosition, movement);
            double distance = Spline.getDistance(currentPosition, movement);
            //move according to angle and distance
            //update currentPosition
        }
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition(0);
        }
    }

    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
        }
        while (motors[0].isBusy() || motors[1].isBusy() || motors[2].isBusy() || motors[3].isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
        }
    }

    public void translate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,power,power}, new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()+positionChange),(rb.getCurrentPosition()+positionChange)});
    }

    public void rotate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()-positionChange),(rb.getCurrentPosition()-positionChange)});
    }
}