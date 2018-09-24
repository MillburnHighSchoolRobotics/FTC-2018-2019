package virtualRobot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by david on 1/12/18.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateMotor {
    String name();
    boolean enabled() default true;
    DcMotor.RunMode mode() default DcMotor.RunMode.RUN_WITHOUT_ENCODER;
    DcMotor.ZeroPowerBehavior zero() default DcMotor.ZeroPowerBehavior.BRAKE;
    DcMotor.Direction direction() default DcMotorSimple.Direction.FORWARD;
    boolean encoderReversed() default false;
}
