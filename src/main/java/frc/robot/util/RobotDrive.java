package frc.robot.util;

import edu.wpi.first.wpilibj.MotorSafety;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/** Class for controlling the robot drivetrain and ensuring motor safety with constant updates. */
public class RobotDrive extends MotorSafety {
  private final MotorController l_motor;
  private final MotorController r_motor;

  public static class WheelSpeeds {
    public final double left;
    public final double right;

    public WheelSpeeds(double left, double right) {
      this.left = left;
      this.right = right;
    }
  }

  /**
   * Creates a new RobotDrive with motor safety enabled.
   *
   * @param l_motor the {@link MotorController} for the left side
   * @param r_motor the {@link MotorController} for the right side
   */
  public RobotDrive(MotorController l_motor, MotorController r_motor) {
    this.l_motor = l_motor;
    this.r_motor = r_motor;
    setSafetyEnabled(true);
  }

  /**
   * Sets the drivetrain's speeds according to the passed {@link WheelSpeeds}.
   *
   * @param speeds the speeds to be set inside a {@link WheelSpeeds}
   */
  public void drive(WheelSpeeds speeds) {
    speeds = MathUtil.desaturateWheelSpeeds(speeds);
    l_motor.set(speeds.left);
    r_motor.set(speeds.right);
    feed();
  }

  /**
   * Classic arcade drive, squaring inputs to increase sensitivity.
   *
   * @param drive forward speed [-1.0..1.0]
   * @param rotation clockwise rotation [-1.0..1.0]
   */
  public void arcadeDrive(double drive, double rotation) {
    drive(MathUtil.arcadeDriveIK(drive, rotation));
  }

  /** @return the speed of the left motor */
  public double getLeft() {return l_motor.get();}
  /** @return the speed of the right motor */
  public double getRight() {return r_motor.get();}

  /** Stops all motors. */
  @Override
  public void stopMotor() {
    l_motor.stopMotor();
    r_motor.stopMotor();
    feed();
  }

  @Override
  public String getDescription() {return "Drivetrain";}
}