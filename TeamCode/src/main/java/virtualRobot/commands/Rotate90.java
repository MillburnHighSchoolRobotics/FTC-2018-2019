package virtualRobot.commands;

import virtualRobot.SallyJoeBot;
import virtualRobot.hardware.Motor;

/**
 * Created by david on 1/16/18.
 */

public class Rotate90 extends Command {
    int power;
    public static int NINETY_DEG_ENCODER = 900;
    SallyJoeBot robot;
    Motor LF, RF, LB, RB;

    public Rotate90() {
        this(1);
    }

    public Rotate90(int speed) {
        power = speed;
        robot = Command.ROBOT;
        LF = robot.getLFMotor();
        RF = robot.getRFMotor();
        LB = robot.getLBMotor();
        RB = robot.getRBMotor();
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        int lf = LF.getPosition();
        LF.setPower(power);
        RF.setPower(-power);
        LB.setPower(power);
        RB.setPower(-power);
        while (power >= 0 ? LF.getPosition() - lf < NINETY_DEG_ENCODER : LF.getPosition() - lf > -NINETY_DEG_ENCODER);
        robot.stopMotors();
        return false;
    }
}
