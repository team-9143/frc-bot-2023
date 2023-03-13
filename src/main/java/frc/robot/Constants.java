// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// TODO: DriveDistance, TurnToAngle, and Intake gains should be tuned
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
      kIntakeTiltCANid = 17;
  }

  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 6; // In inches
    public static final double kGearboxRatio = 12.761;

    // General driving
    public static final double kSpeedMult = 1; // Applies to all drivetrain movement
    public static final double kTurnMult = 0.7; // For controller-based turning

    // TurnToAngle
    public static final double kTurnPosTolerance = 1.5; // Position tolerance (in degrees)
    public static final double kTurnVelTolerance = kTurnPosTolerance*100; // Velocity tolerance (in degrees/s)
    public static final double
      kTurnP = 0.005,
      kTurnI = 0.0035,
      kTurnD = 0.0025;

    // DriveDistance
    public static final double kDistPosTolerance = 2; // Position tolerance (in inches)
    public static final double kDistVelTolerance = kDistPosTolerance*100; // Velocity tolerance (in inches/s)
    public static final double
      kDistP = 0,
      kDistI = 0,
      kDistD = 0;

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // In degrees
  }

  public static class IntakeConstants {
    public static final double kTiltGearbox = (double) 1/28;

    // Cube intake/outtake wheel speed
    public static final double kIntakeSpeed = 0.05;
    public static final double kOuttakeSpeed = -0.25;

    // In rotations, multiplied by gearbox ratio
    public static final double kUpPos = -0.02;
    public static final double kDownPos = (double) -75/360;

    // Intake tilt gains
    public static final double
      kP = 0.013 / kTiltGearbox,
      kI = 0.015 / kTiltGearbox,
      kD = 0.01 / kTiltGearbox;
  }
}