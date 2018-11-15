package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class SafeHardwareMap {
    private HardwareMap hardwareMap;
    public SafeHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public synchronized HardwareMap get() {
        return hardwareMap;
    }
}
