// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class DeviceConstants {
    public static final byte kDriverCntlrPort = 0;
    public static final byte
      kFrontLeftCANid = 3,
      kBackLeftCANid = 4,
      kFrontRightCANid = 2,
      kBackRightCANid = 1,
      kPigeonCANid = 5,
      kIntakeWheelsCANid = 6,
      kIntakePositionalCANid = 17;
  }

  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 0.5; // In feet
    public static final double kGearboxRatio = 12.761;

    // General driving
    public static final double kSpeedMult = 1; // Applies to all drivetrain movement
    public static final double kTurnMult = 0.7; // For controller-based turning

    // TurnToAngle
    public static final double kTurnDeadspot = 1.5; // Deadspot for non-PID TurnToAngle
    public static final double kTurnPower = 0.2; // Base power for non-PID TurnToAngle

    // Charge station balancing
    public static final double kPitchDeadspot = 2;
  }

  public static class IntakeConstants {
    public static final double kPositionalGearbox = (double) 1/27;

    // Cube intake/outtake wheel speed
    public static final double kIntakeSpeed = 0.25;
    public static final double kOuttakeSpeed = -1;

    // In rotations, multiplied by gearbox ratio
    public static final double kUpPos = -0.02;
    public static final double kDownPos = -0.18 + kUpPos;

    public static final double
      kP = 0.3,
      kI = 0.4,
      kD = 0.2;
  }
}