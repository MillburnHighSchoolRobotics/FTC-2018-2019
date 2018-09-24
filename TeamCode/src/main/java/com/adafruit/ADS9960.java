package com.adafruit;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.sensors.I2CSensor;

import static com.adafruit.ADS9960.Register.*;

/**
 * Created by Ethan Mak on 11/22/2017.
 */

public class ADS9960 extends I2CSensor {

    enum Register implements I2CSensor.Register {
        APDS9960_RAM(0x00),
        APDS9960_ENABLE(0x80),
        APDS9960_ATIME(0x81),
        APDS9960_WTIME(0x83),
        APDS9960_AILTIL(0x84),
        APDS9960_AILTH(0x85),
        APDS9960_AIHTL(0x86),
        APDS9960_AIHTH(0x87),
        APDS9960_PILT(0x89),
        APDS9960_PIHT(0x8B),
        APDS9960_PERS(0x8C),
        APDS9960_CONFIG1(0x8D),
        APDS9960_PPULSE(0x8E),
        APDS9960_CONTROL(0x8F),
        APDS9960_CONFIG2(0x90),
        APDS9960_ID(0x92),
        APDS9960_STATUS(0x93),
        APDS9960_CDATAL(0x94),
        APDS9960_CDATAH(0x95),
        APDS9960_RDATAL(0x96),
        APDS9960_RDATAH(0x97),
        APDS9960_GDATAL(0x98),
        APDS9960_GDATAH(0x99),
        APDS9960_BDATAL(0x9A),
        APDS9960_BDATAH(0x9B),
        APDS9960_PDATA(0x9C),
        APDS9960_POFFSET_UR(0x9D),
        APDS9960_POFFSET_DL(0x9E),
        APDS9960_CONFIG3(0x9F),
        APDS9960_GPENTH(0xA0),
        APDS9960_GEXTH(0xA1),
        APDS9960_GCONF1(0xA2),
        APDS9960_GCONF2(0xA3),
        APDS9960_GOFFSET_U(0xA4),
        APDS9960_GOFFSET_D(0xA5),
        APDS9960_GOFFSET_L(0xA7),
        APDS9960_GOFFSET_R(0xA9),
        APDS9960_GPULSE(0xA6),
        APDS9960_GCONF3(0xAA),
        APDS9960_GCONF4(0xAB),
        APDS9960_GFLVL(0xAE),
        APDS9960_GSTATUS(0xAF),
        APDS9960_IFORCE(0xE4),
        APDS9960_PICLEAR(0xE5),
        APDS9960_CICLEAR(0xE6),
        APDS9960_AICLEAR(0xE7),
        APDS9960_GFIFO_U(0xFC),
        APDS9960_GFIFO_D(0xFD),
        APDS9960_GFIFO_L(0xFE),
        APDS9960_GFIFO_R(0xFF);

        Register(int val) {
            this.val = val;
        }

        int val;

        @Override
        public int getVal() {
            return val;
        }
    }

    protected ADS9960(I2cDeviceSynch i2cDeviceSynch) {
        super(i2cDeviceSynch, true);
    }

    @Override
    protected boolean doInitialize() {
        byte x = read8(APDS9960_ID);
        if (x != 0xAB) {
            return false;
        }

  /* Set default integration time and gain */
//        setADCIntegrationTime(iTimeMS);
//        setADCGain(aGain);
//
//        // disable everything to start
//        enableGesture(false);
//        enableProximity(false);
//        enableColor(false);
//
//        disableColorInterrupt();
//        disableProximityInterrupt();
//        clearInterrupt();
//
//  /* Note: by default, the device is in power down mode on bootup */
//        enable(false);
//        delay(10);
//        enable(true);
//        delay(10);
//
//        //default to all gesture dimensions
//        setGestureDimensions(APDS9960_DIMENSIONS_ALL);
//        setGestureFIFOThreshold(APDS9960_GFIFO_4);
//        setGestureGain(APDS9960_GGAIN_4);
//        setGestureProximityThreshold(50);
//        resetCounts();
//
//        _gpulse.GPLEN = APDS9960_GPULSE_32US;
//        _gpulse.GPULSE = 9; //10 pulses
//        write8(APDS9960_GPULSE, _gpulse.get());

        return true;
    }

    /**************************************************************************/
/*!
    Sets the integration time for the ADC of the APDS9960, in millis
*/

    /**************************************************************************/
    void setADCIntegrationTime(int iTimeMS) {
        double temp;

        // convert ms into 2.78ms increments
        temp = iTimeMS;
        temp /= 2.78;
        temp = 256 - temp;
        if (temp > 255) temp = 255;
        if (temp < 0) temp = 0;

  /* Update the timing register */
        write8(APDS9960_ATIME, (byte) temp);
    }

    double getADCIntegrationTime() {
        double temp;

        temp = read8(APDS9960_ATIME);

        // convert to units of 2.78 ms
        temp = 256 - temp;
        temp *= 2.78;
        return temp;
    }

/**************************************************************************/
/*!
    Adjusts the color/ALS gain on the APDS9960 (adjusts the sensitivity to light)
*/

    /**************************************************************************/
//    void setADCGain(apds9960AGain_t aGain) {
//        _control.AGAIN = aGain;
//
//  /* Update the timing register */
//        write8(APDS9960_CONTROL, _control.get());
//    }
//
//    apds9960AGain_t getADCGain(void) {
//        return (apds9960AGain_t) (read8(APDS9960_CONTROL) & 0x03);
//    }
//
//
///**************************************************************************/
///*!
//    Adjusts the Proximity gain on the APDS9960
//*/
//
//    /**************************************************************************/
//    void setProxGain(apds9960PGain_t pGain) {
//        _control.PGAIN = pGain;
//
//  /* Update the timing register */
//        write8(APDS9960_CONTROL, _control.get());
//    }
//
//    apds9960PGain_t getProxGain(void) {
//        return (apds9960PGain_t) (read8(APDS9960_CONTROL) & 0x0C);
//    }
//
//    void setProxPulse(apds9960PPulseLen_t pLen, byte pulses) {
//        if (pulses < 1) pulses = 1;
//        if (pulses > 64) pulses = 64;
//        pulses--;
//
//        _ppulse.PPLEN = pLen;
//        _ppulse.PPULSE = pulses;
//
//        write8(APDS9960_PPULSE, _ppulse.get());
//    }
//
///**************************************************************************/
///*!
//    Enable proximity readings on APDS9960
//*/
//
//    /**************************************************************************/
//    void enableProximity(boolean en) {
//        _enable.PEN = en;
//
//        write8(APDS9960_ENABLE, _enable.get());
//    }
//
//    void enableProximityInterrupt() {
//        _enable.PIEN = 1;
//        write8(APDS9960_ENABLE, _enable.get());
//        clearInterrupt();
//    }
//
//    void disableProximityInterrupt() {
//        _enable.PIEN = 0;
//        write8(APDS9960_ENABLE, _enable.get());
//    }
//
//    void setProximityInterruptThreshold(byte low, byte high, byte persistance) {
//        write8(APDS9960_PILT, low);
//        write8(APDS9960_PIHT, high);
//
//        if (persistance > 7) persistance = 7;
//        _pers.PPERS = persistance;
//        write8(APDS9960_PERS, _pers.get());
//    }
//
//    bool getProximityInterrupt() {
//        _status.set(this->read8(APDS9960_STATUS));
//        return _status.PINT;
//    }
//
//    ;
//
///**************************************************************************/
///*!
//    Read proximity data
//*/
//
//    /**************************************************************************/
//    byte readProximity(void) {
//        return read8(APDS9960_PDATA);
//    }
//
//
//    bool gestureValid() {
//        _gstatus.set(this->read8(APDS9960_GSTATUS));
//        return _gstatus.GVALID;
//    }
//
//    void setGestureDimensions(byte dims) {
//        _gconf3.GDIMS = dims;
//        this->write8(APDS9960_GCONF3, _gconf3.get());
//    }
//
//    void setGestureFIFOThreshold(byte thresh) {
//        _gconf1.GFIFOTH = thresh;
//        this->write8(APDS9960_GCONF1, _gconf1.get());
//    }
//
//    void setGestureGain(byte gain) {
//        _gconf2.GGAIN = gain;
//        this->write8(APDS9960_GCONF2, _gconf2.get());
//    }
//
//    void setGestureProximityThreshold(byte thresh) {
//        this->write8(APDS9960_GPENTH, thresh);
//    }
//
//    void setGestureOffset(byte offset_up, byte offset_down, byte offset_left, byte offset_right) {
//        this->write8(APDS9960_GOFFSET_U, offset_up);
//        this->write8(APDS9960_GOFFSET_D, offset_down);
//        this->write8(APDS9960_GOFFSET_L, offset_left);
//        this->write8(APDS9960_GOFFSET_R, offset_right);
//    }
//
///**************************************************************************/
///*!
//    Enable gesture readings on APDS9960
//*/
//
//    /**************************************************************************/
//    void enableGesture(boolean en) {
//        if (!en) {
//            _gconf4.GMODE = 0;
//            write8(APDS9960_GCONF4, _gconf4.get());
//        }
//        _enable.GEN = en;
//        write8(APDS9960_ENABLE, _enable.get());
//        resetCounts();
//    }
//
//    void resetCounts() {
//        gestCnt = 0;
//        UCount = 0;
//        DCount = 0;
//        LCount = 0;
//        RCount = 0;
//    }
//
//    byte readGesture(void) {
//        byte toRead, bytesRead;
//        byte buf[ 256];
//        unsigned long t;
//        byte gestureReceived;
//        while (1) {
//            int up_down_diff = 0;
//            int left_right_diff = 0;
//            gestureReceived = 0;
//            if (!gestureValid()) return 0;
//
//            delay(30);
//            toRead = this->read8(APDS9960_GFLVL);
//
//            bytesRead = this->read(APDS9960_GFIFO_U, buf, toRead);
//
//            if (Math.abs((int) buf[0] - (int) buf[1]) > 13)
//                up_down_diff += (int) buf[0] - (int) buf[1];
//
//            if (Math.abs((int) buf[2] - (int) buf[3]) > 13)
//                left_right_diff += (int) buf[2] - (int) buf[3];
//
//            if (up_down_diff != 0) {
//                if (up_down_diff < 0) {
//                    if (DCount > 0) {
//                        gestureReceived = APDS9960_UP;
//                    } else UCount++;
//                } else if (up_down_diff > 0) {
//                    if (UCount > 0) {
//                        gestureReceived = APDS9960_DOWN;
//                    } else DCount++;
//                }
//            }
//
//            if (left_right_diff != 0) {
//                if (left_right_diff < 0) {
//                    if (RCount > 0) {
//                        gestureReceived = APDS9960_LEFT;
//                    } else LCount++;
//                } else if (left_right_diff > 0) {
//                    if (LCount > 0) {
//                        gestureReceived = APDS9960_RIGHT;
//                    } else RCount++;
//                }
//            }
//
//            if (up_down_diff != 0 || left_right_diff != 0) t = millis();
//
//            if (gestureReceived || millis() - t > 300) {
//                resetCounts();
//                return gestureReceived;
//            }
//        }
//    }
//
///**************************************************************************/
///*!
//    Set LED brightness for proximity/gesture
//*/
//
//    /**************************************************************************/
//    void setLED(apds9960LedDrive_t drive, apds9960LedBoost_t boost) {
//        // set BOOST
//        _config2.LED_BOOST = boost;
//        write8(APDS9960_CONFIG2, _config2.get());
//
//        _control.LDRIVE = drive;
//        write8(APDS9960_CONTROL, _control.get());
//    }
//
///**************************************************************************/
///*!
//    Enable proximity readings on APDS9960
//*/
//
//    /**************************************************************************/
//    void enableColor(boolean en) {
//        _enable.AEN = en;
//        write8(APDS9960_ENABLE, _enable.get());
//    }
//
//    bool colorDataReady() {
//        _status.set(this->read8(APDS9960_STATUS));
//        return _status.AVALID;
//    }
//
///**************************************************************************/
///*!
//    @brief  Reads the raw red, green, blue and clear channel values
//*/
//
//    /**************************************************************************/
//    void getColorData(int r, int g, int b, int c) {
//
//  *c = read16(APDS9960_CDATAL);
//  *r = read16(APDS9960_RDATAL);
//  *g = read16(APDS9960_GDATAL);
//  *b = read16(APDS9960_BDATAL);
//
//    }
//
///**************************************************************************/
///*!
//    @brief  Converts the raw R/G/B values to color temperature in degrees
//            Kelvin
//*/
//
//    /**************************************************************************/
//    int calculateColorTemperature(int r, int g, int b) {
//        double X, Y, Z;      /* RGB to XYZ correlation      */
//        double xc, yc;       /* Chromaticity co-ordinates   */
//        double n;            /* McCamy's formula            */
//        double cct;
//
//  /* 1. Map RGB values to their XYZ counterparts.    */
//  /* Based on 6500K fluorescent, 3000K fluorescent   */
//  /* and 60W incandescent values for a wide range.   */
//  /* Note: Y = Illuminance or lux                    */
//        X = (-0.14282F * r) + (1.54924F * g) + (-0.95641F * b);
//        Y = (-0.32466F * r) + (1.57837F * g) + (-0.73191F * b);
//        Z = (-0.68202F * r) + (0.77073F * g) + (0.56332F * b);
//
//  /* 2. Calculate the chromaticity co-ordinates      */
//        xc = (X) / (X + Y + Z);
//        yc = (Y) / (X + Y + Z);
//
//  /* 3. Use McCamy's formula to determine the CCT    */
//        n = (xc - 0.3320F) / (0.1858F - yc);
//
//  /* Calculate the final CCT */
//        cct = (449.0F * Math.pow(n, 3)) + (3525.0F * Math.pow(n, 2)) + (6823.3F * n) + 5520.33F;
//
//  /* Return the results in degrees Kelvin */
//        return (int) cct;
//    }
//
///**************************************************************************/
///*!
//    @brief  Calculate ambient light values
//*/
//
//    /**************************************************************************/
//
//    int calculateLux(int r, int g, int b) {
//        double illuminance;
//
//  /* This only uses RGB ... how can we integrate clear or calculate lux */
//  /* based exclusively on clear since this might be more reliable?      */
//        illuminance = (-0.32466F * r) + (1.57837F * g) + (-0.73191F * b);
//
//        return (int) illuminance;
//    }
//
//    void enableColorInterrupt() {
//        _enable.AIEN = 1;
//        write8(APDS9960_ENABLE, _enable.get());
//    }
//
//    void disableColorInterrupt() {
//        _enable.AIEN = 0;
//        write8(APDS9960_ENABLE, _enable.get());
//    }
//
//    void clearInterrupt(void) {
//        this->write(APDS9960_AICLEAR, NULL, 0);
//    }
//
//
//    void setIntLimits(int low, int high) {
//        write8(APDS9960_AILTIL, low & 0xFF);
//        write8(APDS9960_AILTH, low >> 8);
//        write8(APDS9960_AIHTL, high & 0xFF);
//        write8(APDS9960_AIHTH, high >> 8);
//    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Adafruit;
    }

    @Override
    public String getDeviceName() {
        return getManufacturer().name() + " ADS9960";
    }
}
