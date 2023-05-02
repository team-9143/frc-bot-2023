package frc.robot.util;

import edu.wpi.first.wpilibj.MotorSafety;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;;

/** Class for driving a differential drivetrain and ensuring motor safety with constant updates. */
public class DifferentialDrive extends MotorSafety {
  private final MotorController l_motor;
  private final MotorController r_motor;

  /**
   * Creates a new DifferentialDrive with motor safety enabled.
   *
   * @param l_motor the {@link MotorController} for the left side
   * @param r_motor the {@link MotorController} for the right side
   */
  public DifferentialDrive(MotorController l_motor, MotorController r_motor) {
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
    l_motor.set(speeds.left);
    r_motor.set(speeds.right);
    feed();
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