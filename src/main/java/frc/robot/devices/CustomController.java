package frc.robot.devices;

import edu.wpi.first.wpilibj.GenericHID;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.event.EventLoop;

// TODO: Apply deadbands to axes
public class CustomController extends GenericHID {
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

    public final int val;
    btn (int val) {this.val = val;}
  }

  public static enum axis {
    leftX(0),
    leftY(1),
    rightX(4),
    rightY(5),
    leftTrigger(2),
    rightTrigger(3);

    public final int val;
    axis (int val) {this.val = val;}
  }

  public CustomController(int port) {
    super(port);
  }

  /** @return left trigger subtractive and right trigger additive [-1.0..1.0] */
  public double getTriggers() {
    return getRawAxis(axis.rightTrigger.val) - getRawAxis(axis.leftTrigger.val);
  }

  public double getLeftX() {return getRawAxis(axis.leftX.val);}
  public double getLeftY() {return getRawAxis(axis.leftY.val);}
  public double getRightX() {return getRawAxis(axis.rightX.val);}
  public double getRightY() {return getRawAxis(axis.rightY.val);}

  public void onTrue(int btn, Runnable run) {
    onTrue(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void onTrue(int btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (getRawButtonPressed(btn)) {run.run();}
    });
  }

  public void onFalse(int btn, Runnable run) {
    onFalse(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void onFalse(int btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (getRawButtonReleased(btn)) {run.run();}
    });
  }

  public void whileTrue(int btn, Runnable run) {
    whileTrue(btn, run, CommandScheduler.getInstance().getDefaultButtonLoop());
  }

  public void whileTrue(int btn, Runnable run, EventLoop loop) {
    loop.bind(() -> {
      if (getRawButton(btn)) {run.run();}
    });
  }
}