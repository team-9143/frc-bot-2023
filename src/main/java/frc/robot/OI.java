package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {
  public final static LogitechController m_controller = new LogitechController(0);
  public final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  private final static JoystickButton b_button = new JoystickButton(m_controller, LogitechController.BTN_B);

  public OI() {
    b_button.onTrue(new ResetGyro());
  }

  private class ResetGyro extends CommandBase {
    @Override
    public void initialize() {
      gyro.reset();
    }

    @Override
    public boolean isFinished() {
      return true;
    }
  }
}
