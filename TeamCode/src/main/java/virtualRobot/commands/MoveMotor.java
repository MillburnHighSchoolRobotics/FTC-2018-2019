package virtualRobot.commands;

import virtualRobot.PIDController;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;

/**
 * Created by shant on 11/5/2015.
 * Moves a Motor
 */
public class MoveMotor extends Command {
	private Motor motor;
	private boolean clearEncoders;
	private double power;
	private PIDController pidController;
	private Translate.RunMode runMode;
	private double tolerance;
	private double timeLimit = Double.MAX_VALUE;

	public MoveMotor() {

		motor = null;
		power = 1;
		runMode = Translate.RunMode.CUSTOM;
		tolerance = 20;
		clearEncoders = false;

		pidController = new PIDController();
	}

	public MoveMotor(Motor motor) {
		this();
		this.motor = motor;
	}

	public MoveMotor(Motor motor, double power) {
		this(motor);
		this.power = power;
	}

	public MoveMotor(Motor motor, double power, double timeLimit) {
		this(motor, power);
		this.timeLimit = timeLimit;
	}

	public MoveMotor(Motor motor, double power, double target, Translate.RunMode runMode, boolean clearEncoders) {
		this(motor, power);

		this.runMode = runMode;
		this.clearEncoders = clearEncoders;

		this.pidController.setTarget(target);
	}

	public MoveMotor(Motor motor, double power, double target, Translate.RunMode runMode, boolean clearEncoders, double kP, double kI, double kD, double threshold, double tolerance) {
		this(motor, power, target, runMode, clearEncoders);

		this.pidController.setKP(kP);
		this.pidController.setKI(kI);
		this.pidController.setKD(kD);
		this.pidController.setThreshold(threshold);

		this.tolerance = tolerance;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public void setTarget(double target) {
		this.pidController.setTarget(target);
	}

	public void setRunMode(Translate.RunMode runMode) {
		this.runMode = runMode;
	}

	public void setPIDValues(double kP, double kI, double kD, double threshold, double tolerance) {
		this.pidController.setKP(kP);
		this.pidController.setKI(kI);
		this.pidController.setKD(kD);
		this.pidController.setThreshold(threshold);

		this.tolerance = tolerance;
	}

	@Override
	public boolean changeRobotState() throws InterruptedException {

		boolean isInterrupted = false;

		switch (runMode) {
		case CUSTOM:
			motor.setPower(power);
			long start = System.currentTimeMillis();

			MainLoop: while (!isInterrupted && (System.currentTimeMillis() - start < timeLimit)) {
				switch (checkConditionals()) {
					case BREAK:
						break MainLoop;
					case END:
						return isInterrupted;
				}

				if (Thread.currentThread().isInterrupted()) {
					isInterrupted = true;
					break;
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					isInterrupted = true;
					break;
				}
			}

			break;
		case WITH_ENCODERS:
			if (clearEncoders) motor.clearEncoder();
			motor.setPower(power);

			if (power > 0) {
				MainLoop: while (!isInterrupted && motor.getPosition() < pidController.getTarget()) {
					switch (checkConditionals()) {
						case BREAK:
							break MainLoop;
						case END:
							return isInterrupted;
					}

					if (Thread.currentThread().isInterrupted()) {
						isInterrupted = true;
						break;
					}

					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						isInterrupted = true;
						break;
					}
				}
			} else {
				MainLoop: while (!isInterrupted && motor.getPosition() > pidController.getTarget()) {
					switch (checkConditionals()) {
						case BREAK:
							break MainLoop;
						case END:
							return isInterrupted;
					}

					if (Thread.currentThread().isInterrupted()) {
						isInterrupted = true;
						break;
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						isInterrupted = true;
						break;
					}
				}
			}

			break;

		case WITH_PID:
			if (clearEncoders) motor.clearEncoder();

			MainLoop: while (!isInterrupted && Math.abs(motor.getPosition() - pidController.getTarget()) > tolerance) {
				switch (checkConditionals()) {
					case BREAK:
						break MainLoop;
					case END:
						return isInterrupted;
				}

				double pidOutput = pidController.getPIDOutput(Math.abs(motor.getPosition()));

				motor.setPower(pidOutput * power);

				if (Thread.currentThread().isInterrupted()) {
					isInterrupted = true;
					break;
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					isInterrupted = true;
					break;
				}

			}
		default:
			break;
		}

		motor.setPower(0);

		return isInterrupted;
	}
}
