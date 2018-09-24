package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by shant on 2/18/2016.
 */
@Disabled
public class N60Test extends OpMode {

    DcMotor m1;
    DcMotor m2;

    int m1Init;
    int m2Init;

    @Override
    public void init() {
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");

        m1Init = m1.getCurrentPosition();
        m2Init = m2.getCurrentPosition();
    }

    @Override
    public void loop() {
        int m1Cur = m1.getCurrentPosition() - m1Init;
        int m2Cur = m2.getCurrentPosition() - m2Init;

        if (m1Cur < 15000) {
            m1.setPower(.8);
        } else {
            m1.setPower(0);
        }

        if (m2Cur < 15000) {
            m2.setPower(.8);
        } else {
            m2.setPower(0);
        }
    }
}
