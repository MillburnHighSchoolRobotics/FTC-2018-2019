package com.sensors.ftc;

import java.sql.SQLException;

/**
 * Created by ethachu19 on 11/2/2016.
 */

public class SRF08Protocol {
    public static byte CHANGEI2C1 = (byte) 0xA0;
    public static byte CHANGEI2C2 = (byte) 0xAA;
    public static byte CHANGEI2C3 = (byte) 0xA5;

    public static byte DISTINCHES = 0x50;
    public static byte DISTCM = 0x51;
    public static byte DISTMICROSECONDS = 0x52;
}
