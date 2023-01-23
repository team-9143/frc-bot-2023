package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {
    public final static Joystick m_stick = new Joystick(0);
    public final static LogitechController logitechController = new LogitechController(m_stick);
}
