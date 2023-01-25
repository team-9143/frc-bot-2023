package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class OI {
    public final static LogitechController logitechController = new LogitechController(0);
    double leftJoyX = logitechController.getLeftStick()[0];
    double leftJoyY = logitechController.getLeftStick()[1];
    double rightJoyX = logitechController.getRightStick()[4];
    double rightJoyY = logitechController.getRightStick()[5];
    public static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public final double joystickAngle = Math.atan2(rightJoyY,rightJoyX) * 180 / Math.PI;
    

}
