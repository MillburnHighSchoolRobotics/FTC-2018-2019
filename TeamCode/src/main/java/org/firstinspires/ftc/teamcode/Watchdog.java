package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Watchdog extends Thread {
    protected final Thread parentThread;
    protected final HardwareMap hardwareMap;
    public Watchdog(Thread parentThread, HardwareMap hardwareMap) {
        this.parentThread = parentThread;
        this.hardwareMap = hardwareMap;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !parentThread.isInterrupted()) {
            loop();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Log.e("Watchdog", e.getMessage(), e);
                break;
            }
        }
    }
    protected abstract void loop();
}
