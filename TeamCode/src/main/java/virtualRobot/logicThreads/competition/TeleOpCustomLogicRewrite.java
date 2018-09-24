package virtualRobot.logicThreads.competition;

import com.qualcomm.robotcore.robot.Robot;

import java.util.Date;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.utils.MathUtils;

/**
 * Created by david on 9/29/17.
 */
@Deprecated
public class TeleOpCustomLogicRewrite extends LogicThread {

    double leftPos = 0;
    double rightPos = 0;
    double gearCoefficient = 1;
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        robot.getJewelServo().setPosition(0);
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
        long lastMilli = -1;
        boolean clawMove = false;
        final int POWER_MATRIX[][] = { //for each of the directions

                {1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, -1, -1, 1},
                {0, -1, -1, 0},
                {-1, -1, -1, -1},
                {-1, 0, 0, -1},
                {-1, 1, 1, -1},
                {0, 1, 1, 0}
        };

        while (true) {
//            robot.addToTelemetry("TeleOp timestamp: ", System.currentTimeMillis());
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1));
            double translateMag = controller1.getValue(JoystickController.R_1);
            double rotateX = controller1.getValue(JoystickController.X_2);
            if (translateTheta < 0) translateTheta += 360;
            double scale;
            double RF = 0, RB = 0, LF = 0, LB = 0;
            robot.addToTelemetry("mag", translateMag);

            if (controller1.isDpadUp()) {
                gearCoefficient = 0.666;
            } else if (controller1.isDpadDown()) {
                gearCoefficient = 0.333;
            }
            if (!MathUtils.equals(rotateX, 0, 0.05)) {
                robot.getRFMotor().setPower(-rotateX * gearCoefficient);
                robot.getRBMotor().setPower(-rotateX * gearCoefficient);
                robot.getLFMotor().setPower(rotateX * gearCoefficient);
                robot.getLBMotor().setPower(rotateX * gearCoefficient);
//                robot.addToTelemetry("TeleOp if statement lvl", 0);
            } else if (!MathUtils.equals(translateMag, 0, 0.05)) {
                double translatePower = translateMag * 0.666;
                if (translateTheta >= 0 && translateTheta <= 90) { //quadrant 1
                    scale = MathUtils.sinDegrees(translateTheta - 45) / MathUtils.cosDegrees(translateTheta - 45);
                    LF = translatePower * POWER_MATRIX[0][0];
                    LB = translatePower * POWER_MATRIX[0][1] * scale;
                    RF = translatePower * POWER_MATRIX[0][2] * scale;
                    RB = translatePower * POWER_MATRIX[0][3];
                } else if (translateTheta > 90 && translateTheta <= 180) { //quadrant 2
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 135) / MathUtils.cosDegrees(translateTheta - 135);
                    LF = (translatePower * POWER_MATRIX[2][0] * scale);
                    LB = (translatePower * POWER_MATRIX[2][1]);
                    RF = (translatePower * POWER_MATRIX[2][2]);
                    RB = (translatePower * POWER_MATRIX[2][3] * scale);
                } else if (translateTheta > 180 && translateTheta <= 270) { //quadrant 3
                    scale = MathUtils.sinDegrees(translateTheta - 225) / MathUtils.cosDegrees(translateTheta - 225);
                    LF = (translatePower * POWER_MATRIX[4][0]);
                    LB = (translatePower * POWER_MATRIX[4][1] * scale);
                    RF = (translatePower * POWER_MATRIX[4][2] * scale);
                    RB = (translatePower * POWER_MATRIX[4][3]);
//                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
                } else if (translateTheta > 270 && translateTheta < 360) { //quadrant 4
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 315) / MathUtils.cosDegrees(translateTheta - 315);
                    LF = (translatePower * POWER_MATRIX[6][0] * scale);
                    LB = (translatePower * POWER_MATRIX[6][1]);
                    RF = (translatePower * POWER_MATRIX[6][2]);
                    RB = (translatePower * POWER_MATRIX[6][3] * scale);
                }
                robot.addToTelemetry("1", LF + "\t" + RF);
                robot.addToTelemetry("2", LB + "\t" + RB);
                robot.getLFMotor().setPower(LF * gearCoefficient);
                robot.getLBMotor().setPower(LB * gearCoefficient);
                robot.getRBMotor().setPower(RB * gearCoefficient);
                robot.getRFMotor().setPower(RF * gearCoefficient);

            } else {
//                robot.addToTelemetry("TeleOp if statement lvl", 2);
                robot.stopMotors();
            }

            if (controller2.isPressed(JoystickController.BUTTON_LT)) {
                leftPos = MathUtils.clamp(leftPos -= .1,0,1);
                rightPos = MathUtils.clamp(rightPos -= .1,0,1);
            } else if (controller2.isPressed(JoystickController.BUTTON_RT)) {
                leftPos = MathUtils.clamp(leftPos += .1, 0, 1);
                rightPos = MathUtils.clamp(rightPos += .1, 0, 1);
            } else if (controller2.isPressed(JoystickController.BUTTON_A))
                rightPos = MathUtils.clamp(rightPos -= .1,0,1);
            else if (controller2.isPressed(JoystickController.BUTTON_X))
                leftPos = MathUtils.clamp(leftPos -= .1,0,1);
            else if (controller2.isPressed(JoystickController.BUTTON_B))
                rightPos = MathUtils.clamp(rightPos += .1,0,1);
            else if (controller2.isPressed(JoystickController.BUTTON_Y))
                leftPos = MathUtils.clamp(leftPos += .1,0,1);
            if(controller2.isDpadUp()) {
                leftPos = 1;
                rightPos = 1;
            }
            if(controller2.isDpadRight()) {
                leftPos = 0.666;
                rightPos = 0.666;
            }
            if(controller2.isDpadDown()) {
                leftPos = 0;
                rightPos = 0;
            }
            if(controller2.isDpadLeft()) {
                leftPos = 0.333;
                rightPos = 0.333;
            }
            robot.addToTelemetry("leftPos", leftPos);
            robot.addToTelemetry("rightPos", rightPos);
           // robot.getGlyphLiftLeft().setPosition(1-leftPos);
            //robot.getGlyphLiftRight().setPosition(rightPos);

            if (lastMilli < 0 || System.currentTimeMillis() - lastMilli >= 1000) {
//                robot.moveClaw(clawMove);
                clawMove = !clawMove;
                lastMilli = System.currentTimeMillis();
            }

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
