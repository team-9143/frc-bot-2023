package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Logitech game controller wrapper class
 * 
 * @author Shuhao
 * @author Siddharth Thomas '2025
 */
public class LogitechController extends Joystick {
  public static final byte
    BTN_A = 1, 
    BTN_B = 2,
    BTN_X = 3,
    BTN_Y = 4,
    BTN_LB = 5,
    BTN_RB = 6,
    BTN_BACK = 7,
    BTN_START = 8,
    BTN_LEFT_JOYSTICK = 9,
    BTN_RIGHT_JOYSTICK = 10;

  public LogitechController(int port) {
    super(port);
  }

  /**
   * Gets the trigger, which is left and right trigger. Left trigger is
   * negative and right trigger is positive. They range from 0 - 1 (absolute
   * values) and additive
   * 
   * @return The trigger value.
   */
  public double getTriggerButtons() {
    return getRawAxis(3) - getRawAxis(2);
  }

  public double[] getLeftStick() {
    return new double[] {getRawAxis(0), -getRawAxis(1)};
  }

  public double[] getRightStick() {
    return new double[] {getRawAxis(4), -getRawAxis(5)};
  }
}