package virtualRobot.commands;

import virtualRobot.SallyJoeBot;

/**
 * Created by Ethan Mak on 2/18/2018.
 */

public class RotateEncoder extends Command {

    private double target;
    private double speed;
    private double timeLimit;

    public RotateEncoder(double target, double speed, double timeLimit) {
        this.target = target;
        this.speed = speed;
        this.timeLimit = timeLimit;
    }

    public RotateEncoder(double target, double speed) {
        this(target, speed, -1);
    }

    public RotateEncoder(double target) {
        this(target, 1);
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        double delta = (target - robot.getImu().getHeading());
        double targetEncoder = (delta / 360) * (SallyJoeBot.wheelbase * Math.PI) * SallyJoeBot.COUNTS_PER_INCH;
        timeLimit = timeLimit < 0 ? 30000 : timeLimit;
        robot.addToTelemetry("TARGET", targetEncoder);
        robot.encoderDrive(speed, targetEncoder, targetEncoder, -targetEncoder, -targetEncoder, timeLimit);
        return false;
    }
}
