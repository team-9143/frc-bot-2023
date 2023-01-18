package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {
    public final static Joystick m_stick = new Joystick(0);
    public final static JoystickButton m_button2 = new JoystickButton(m_stick, 2);
    public final static JoystickButton m_button1 = new JoystickButton(m_stick, 1);
}
