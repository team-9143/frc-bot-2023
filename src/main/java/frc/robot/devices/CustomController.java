package frc.robot.devices;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.hal.DriverStationJNI;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.event.EventLoop;

/**
 * Class to communicate with a controller through the drive station.
 *
 * @see edu.wpi.first.wpilibj.GenericHID
 */
public class CustomController {
  public static enum btn {
    A(1),
    B(2),
    X(3),
    Y(4),
    LB(5),
    RB(6),
    LStick(9),
    RStick(10),
    Back(7),
    Start(8);

    public final byte val;
    btn (int val) {this.val = (byte) val;}
  }

  public static enum axis {
    leftX(0),
    leftY(1),
    rightX(4),
    rightY(5),
    leftTrigger(2),
    rightTrigger(3);

    public final byte val;
    axis (int val) {this.val = (byte) val;}
  }

  private final byte m_port;

  private int m_outputs;
  private short m_leftRumble;
  private short m_rightRumble;

  public CustomController(byte port) {
    m_port = port;
  }

  /** Applies a 0.02 to 1.00 deadband to the passed value. */
  public static double deadband(double value) {
    if (Math.abs(value) > 0.02) {
      return (value - Math.copySign(0.02, value)) / 0.98;
    }
    return 0;
  }

  public boolean getButton(btn btn) {
    return DriverStation.getStickButton(m_port, btn.val);
  }

  public double getAxis(axis axis) {
    return DriverStation.getStickAxis(m_port, axis.val);
  }

  /** POV indexes start at 0. */
  public int getPOV(int pov) {
    return DriverStation.getStickPOV(m_port, pov);
  }

  /** @return left trigger subtractive and right trigger additive [-1.0..1.0] */
  public double getTriggers() {
    return deadband(getAxis(axis.rightTrigger)) - deadband(getAxis(axis.leftTrigger));
  }

  public double getLeftX() {return deadband(getAxis(axis.leftX));}
  public double getLeftY() {return deadband(getAxis(axis.leftY));}
  public double getRightX() {return deadband(getAxis(axis.rightX));}
  public double getRightY() {return deadband(getAxis(axis.rightY));}

  /**
   * Set the outputs for the HID.
   *
   * @param outputs outputs to be set as a 32-bit value
   */
  public void setOutputs(int outputs) {
    m_outputs = outputs;
    DriverStationJNI.setJoystickOutputs(m_port, m_outputs, m_leftRumble, m_rightRumble);
  }

  /**
   * Set the rumble for the HID.
   *
   * @param left left rumble [0.0..1.0]
   * @param right right rumble [0.0..1.0]
   */
  public void setRumble(double left, double right) {
    short m_leftRumble = (short) (Math.max(0, Math.min(left, 1)) * 65535);
    short m_rightRumble = (short) (Math.max(0, Math.min(right, 1)) * 65535);
    DriverStationJNI.setJoystickOutputs(m_port, m_outputs, m_leftRumble, m_rightRumble);
  }

  public void onTrue(btn btn, Runnable run) {
    onTrue(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void onTrue(btn btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (DriverStation.getStickButtonPressed(m_port, btn.val)) {run.run();}
    });
  }

  public void onFalse(btn btn, Runnable run) {
    onFalse(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void onFalse(btn btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (DriverStation.getStickButtonReleased(m_port, btn.val)) {run.run();}
    });
  }

  public void whileTrue(btn btn, Runnable run) {
    whileTrue(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void whileTrue(btn btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (getButton(btn)) {run.run();}
    });
  }
}