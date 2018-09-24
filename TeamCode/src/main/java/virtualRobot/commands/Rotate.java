package virtualRobot.commands;

import virtualRobot.utils.BetterLog;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.PIDController;
import virtualRobot.SallyJoeBot;
import virtualRobot.utils.MathUtils;

/**
 * Created by shant on 10/27/2015.
 * Rotates the Robot in place
 */
public class Rotate extends Command {
    private Condition condition;
    public static RunMode defaultMode = RunMode.WITH_ANGLE_SENSOR;

    public static void setDefaultMode(RunMode runMode) {
        Rotate.defaultMode = runMode;
    }

    public static final double THRESHOLD = 0;
    //KU:  0.0351875, 0.0377188, 0.04025, 0.04102
    //KU: 0.0377188; TU: 106 0.04102 TU = 80
    public static final double KP =  0.0117;
    public static final double KI = 0.00039; //0.0005131034;
    public static final double KD = 0.08775; //0.24273;

    public static final double MIN_MAX_POWER = .99;

    public static final double TOLERANCE = .6;

    private double power;
    private double angleInDegrees;
    private double initAngle;
    private RunMode runMode;
    private static double globalMaxPower = 1;
    public static boolean onBlue = false;
    private String name;

    private double time;
    private double timeLimit;
    private boolean isTesting = false;
    private AtomicBoolean stop = new AtomicBoolean(false);

    private PIDController pidController;

    private static SallyJoeBot robot = Command.ROBOT;
    private static double currentAngle = 0;

    public static double getCurrentAngle() {
        return currentAngle;
    }

    public static void setCurrentAngle(double currentAngle) {
        Rotate.currentAngle = currentAngle;
    }

    public static void setGlobalMaxPower(double p) {
        globalMaxPower = p;
    }
    public static void setOnBlueSide() {onBlue = true;}

    public Rotate() {

        power = globalMaxPower;

        condition = new Condition() {
            @Override
            public boolean isConditionMet() {
                return false;
            }
        };

        pidController = new PIDController(KP, KI, KD, THRESHOLD);

        runMode = defaultMode;

        timeLimit = -1;
    }

    public Rotate (double target) {
        this();
        this.angleInDegrees = !onBlue ? target : target-180;

        pidController.setTarget(!onBlue ? target : target-180);
    }

    public Rotate (double angleInDegrees, double power) {
        this(angleInDegrees);
        this.power = power;
    }

    public Rotate (double angleInDegrees, double power, String name) {
        this (angleInDegrees, power);
        this.name = name;
    }

    //boolean is just to differentiate from the previous constructor
    public Rotate (double kP, double target, double timeLimit, AtomicBoolean sS) {
        this(target, 1.0, timeLimit);
        pidController.setKP(kP);
        pidController.setKD(0);
        pidController.setKI(0);
        this.stop = sS;
        this.isTesting = true;
    }

    public Rotate(double angleInDegrees, double power, double timeLimit) {
        this(angleInDegrees, power);
        this.timeLimit = timeLimit;
    }

    private Rotate (double angleInDegrees, double power, double timeLimit, String name) {
        this(angleInDegrees, power, timeLimit);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }


    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getAngleInDegrees() {
        return angleInDegrees;
    }

    public void setAngleInDegrees(double angleInDegrees) {
        this.angleInDegrees = angleInDegrees;
    }


    public void addCondition(Condition e) {
        condition = e;
    }


    public Condition getCondition() {
        return condition;
    }

    public void setTHRESHOLD (double THRESHOLD) {

    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        boolean isInterrupted = false;
        time = System.currentTimeMillis();
        initAngle = robot.getImu().getHeading();
        double adjustedPower;
        switch (runMode) {
            case WITH_ANGLE_SENSOR:

                MainLoop: while ((Math.abs(angleInDegrees - robot.getImu().getHeading()) > TOLERANCE || isTesting) && (timeLimit == -1 || (System.currentTimeMillis() - time) < timeLimit)) {
                    switch (checkConditionals()) {
                        case BREAK:
                            break MainLoop;
                        case END:
                            return isInterrupted;
                    }

                    if (stop.get()) {
                        robot.stopMotors();
                        return isInterrupted;
                    }
                    adjustedPower = pidController.getPIDOutput(robot.getImu().getHeading());
                    adjustedPower = MathUtils.clamp(adjustedPower, -1, 1);

                    /*double ratio = Math.abs(angleInDegrees - robot.getImu().getHeading()) / (Math.abs(angleInDegrees - initAngle));
                    double powerScaler = power;
                    if (power > MIN_MAX_POWER) {
                        powerScaler = (power-MIN_MAX_POWER)*ratio + MIN_MAX_POWER;
                    }
                    adjustedPower *= powerScaler;
*/                  //robot.getLeftRotate().setPower(adjustedPower);
                    //robot.getRightRotate().setPower(-adjustedPower);
                    robot.getLBMotor().setPower(adjustedPower);
                    robot.getLFMotor().setPower(adjustedPower);
                    robot.getRFMotor().setPower(-adjustedPower);
                    robot.getRBMotor().setPower(-adjustedPower);
                    BetterLog.d("PIDOUTROTATE", "" + adjustedPower + " " + robot.getImu().getHeading());

                    if (Thread.currentThread().isInterrupted()) {
                        isInterrupted = true;
                        break;
                    }

                    BetterLog.d("PIDOUTPUT", "PID OUTPUT: " + Double.toString(adjustedPower) + "HEADING: " + Double.toString(robot.getImu().getHeading()));

                    try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        isInterrupted = true;
                        break;
                    }
                }
                break;
            case WITH_ENCODER:
                robot.addToProgress("Mode: " + "Encoder");

                robot.getLFMotor().clearEncoder();
                robot.getLBMotor().clearEncoder();
                robot.getRFMotor().clearEncoder();
                robot.getRBMotor().clearEncoder();
                robot.addToProgress("Rotate Angle: " + currentAngle);
                double angle = currentAngle;
                MainLoop: while ((Math.abs(angleInDegrees - currentAngle) > TOLERANCE || isTesting) && (timeLimit == -1 || (System.currentTimeMillis() - time) < timeLimit)) {//Mehmet: Unsure of relevance of 20, may need to be changed.
                    switch (checkConditionals()) {
                        case BREAK:
                            break MainLoop;
                        case END:
                            return isInterrupted;
                    }

                    currentAngle = angle + ((robot.getRFMotor().getPosition() / robot.getRFMotor().getMotorType().getTicksPerRev()) * robot.wheelDiameter * Math.PI) / (Math.sqrt((Math.pow(robot.botWidth, 2) + Math.pow(robot.botLength, 2))) * Math.PI) * 360;
                    robot.addToTelemetry("Rotate: ", currentAngle);
                    adjustedPower = MathUtils.clamp(pidController.getPIDOutput(currentAngle), -1, 1);
                    robot.getLBMotor().setPower(adjustedPower);
                    robot.getLFMotor().setPower(adjustedPower);
                    robot.getRFMotor().setPower(-adjustedPower);
                    robot.getRBMotor().setPower(-adjustedPower);

                    if (Thread.currentThread().isInterrupted()) {
                        isInterrupted = true;
                        break;
                    }

                    Thread.currentThread().sleep(10);
                }
                break;

        }
        stop.set(true);
        robot.stopMotors();

        return isInterrupted;

    }
    public enum RunMode {
        WITH_ANGLE_SENSOR,
        WITH_ENCODER,
        WALL_ALIGN
    }

}
