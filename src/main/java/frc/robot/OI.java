package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class OI {
    public final static Joystick m_stick = new Joystick(0);
    public final static LogitechController logitechController = new LogitechController(m_stick);
    double leftJoyX = logitechController.getLeftStick().x;
    double leftJoyY = logitechController.getLeftStick().y;
    double rightJoyX = logitechController.getRightStick().x;
    double rightJoyY = logitechController.getRightStick().y;
    public static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public final double joystickAngle = Math.atan2(rightJoyY,rightJoyX) * 180 / Math.PI;
    

}
