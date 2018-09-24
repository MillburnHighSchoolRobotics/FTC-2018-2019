package virtualRobot.hardware;

import virtualRobot.PIDController;
import virtualRobot.utils.MathUtils;

/**
 * Created by ethachu19 on 9/23/2016.
 * A purelely virtual component with no direct physical component.
 * It represents two other virtual motors (or two sets of motors),
 * and syncs those two motors or two sets of motors
 */
public class SyncedMotors{
    private SyncMode type;
    private SyncAlgo algo;
    Motor masterA;
    Motor slaveA;
    private long oldTimeA,oldTimeB;
    private double oldEncoderA,oldEncoderB;
    private Sensor encoderA;
    private Sensor encoderB;
    SyncedMotors masterB;
    SyncedMotors slaveB;
    private PIDController pid;
    private double ratio;
    private double lastRatio;
    private double power;
    private double lastSpeedRatio;
    private boolean second0 = false;
    private boolean first0 = false;

    public SyncedMotors(Motor a, Motor b, Sensor eA, Sensor eB ,double KP, double KI, double KD, SyncAlgo algo) {
        this.masterA = a;
        this.slaveA = b;
        this.encoderA = eA;
        this.encoderB = eB;
        type = SyncMode.MOTORS;
        this.algo = algo;
        if (algo == SyncAlgo.SPEED){
            pid = new PIDController(KP,KI,KD,0.01,1);
        } else {
            pid = new PIDController(KP,KI,KD,10,1);
        }
        this.encoderA.clearValue();
        this.encoderB.clearValue();
    }

    public SyncedMotors(SyncedMotors a, SyncedMotors b, double KP, double KI, double KD, SyncAlgo algo) {
        this.masterB = a;
        this.slaveB = b;
        this.algo = algo;
        if (algo == SyncAlgo.SPEED){
            pid = new PIDController(KP,KI,KD,0.01,1);
        } else {
            pid = new PIDController(KP,KI,KD,5,0);
        }
        type = SyncMode.SIDES;
    }

    public synchronized void setRatio(double ratio) {
        this.ratio = ratio;
        this.pid.setTarget(ratio);
    }

    public synchronized void setRatioAndPower(double pw1, double pw2) {
        if(pw1 == 0){
            this.first0 = true;
            this.power = pw2;
            move();
            return;
        } else
            this.first0 = false;
        if(pw2 == 0){
            this.second0 = true;
            this.power = pw1;
            move();
            return;}
        else
            this.second0 = false;
        this.ratio = pw1/pw2;
        this.pid.setTarget(ratio);
        this.power = pw1;
        if (!MathUtils.equals(ratio,lastRatio)) {
            zeroEncoders();
            lastRatio = ratio;
        }
        move();
    }

    public synchronized void setPower(double power) {
        this.power = MathUtils.clamp(power, -1, 1);
        move();
    }

    public synchronized double getSpeedRatio() {
        return lastSpeedRatio;
    }

    public synchronized double getSpeedA() {
        if (type == SyncMode.MOTORS) {
            double temp = encoderA.getValue();
            long tempTime = System.currentTimeMillis();
            double res = (temp - oldEncoderA) * 1000 / ((tempTime - oldTimeA) == 0 ? 1 : (tempTime - oldTimeA));
            oldTimeA = tempTime;
            oldEncoderA = temp;
            return res;
        }
        return masterB.getSpeedA();
    }

    public synchronized double getSpeedB() {
        if (type == SyncMode.MOTORS) {
            double temp = encoderB.getValue();
            long tempTime = System.currentTimeMillis();
            double res = (temp - oldEncoderB) * 1000 / ((tempTime - oldTimeB == 0 ? 1 : (tempTime - oldTimeB )));
            oldTimeB = tempTime;
            oldEncoderB = temp;
            return res;
        }
        return slaveB.getSpeedA();
    }

    public synchronized void move() {
        double adjust = 0;
        if (!first0 || !second0) {
            if (algo == SyncAlgo.SPEED) {
                double speedA = getSpeedA();
                double speedRatio = speedA == 0 ? 0 : getSpeedB() / speedA;
                lastSpeedRatio = speedRatio;
                adjust = pid.getPIDOutput(speedRatio);
            } else {
                adjust = type == SyncMode.MOTORS ? pid.getPIDOutput(encoderA.getValue()*ratio - encoderB.getValue()) : pid.getPIDOutput(masterB.getEncoder().getValue()*ratio - slaveB.getEncoder().getValue());
                lastSpeedRatio = adjust;
            }
        }
        double slavePower = MathUtils.clamp(power*ratio + adjust,-1,1);
        double realPower = MathUtils.clamp(power-adjust, -1, 1);
        if (first0) {realPower = 0; slavePower = power;}
        if (second0) {slavePower = 0; realPower = power;}
        if (type == SyncMode.MOTORS) {
            masterA.setPower(realPower);
            slaveA.setPower(slavePower);
        }else {
            masterB.setPower(realPower);
            slaveB.setPower(slavePower);
            masterB.move();
            slaveB.move();
        }
    }

    public Sensor getEncoder() {
        return encoderA;
    }

    public synchronized void desync() {
        
    }

    public synchronized void zeroEncoders() {
        if (type == SyncMode.MOTORS) {
            encoderA.clearValue();
            encoderB.clearValue();
        } else {
            masterB.zeroEncoders();
            slaveB.zeroEncoders();
        }
    }

    enum SyncMode {
        MOTORS, SIDES
    }

    public enum SyncAlgo {
        POSITION, SPEED
    }
}
