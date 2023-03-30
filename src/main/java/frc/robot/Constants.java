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
    public static final byte kOperatorCntlrPort = 1;
    // TOOD: Add new intake tilt motor
    public static final byte
      kFrontLeftID = 3,
      kBackLeftID = 4,
      kFrontRightID = 2,
      kBackRightID = 1,
      kPigeonID = 5,
      kIntakeWheelsID = 6,
      kIntakeTiltRightID = 7,
      kIntakeTiltLeftID = 8;
  }

  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 6; // In inches
    public static final double kGearboxRatio = 12.76 * 0.85; // TODO: Test and fix gearbox ratio

    // Teleop driving
    public static final double kSpeedMult = 1; // Applies to all manual drivetrain movement
    public static final double kTurnMult = 0.7;

    // TurnToAngle
    public static final double kTurnPosTolerance = 1.5; // Position tolerance (in degrees)
    public static final double kTurnVelTolerance = kTurnPosTolerance; // Velocity tolerance (in degrees/s)
    public static final double kTurnMaxSpeed = 0.75;
    public static final double
      kTurnP = 0.008,
      kTurnI = 0.004,
      kTurnD = 0.0025;

    // TODO: Fix DriveDistance overshooting
    // DriveDistance
    public static final double kDistPosTolerance = 2; // Position tolerance (in inches)
    public static final double kDistVelTolerance = kDistPosTolerance; // Velocity tolerance (in inches/s)
    public static final double kDistMaxSpeed = 0.4; // Time efficiency is unnecessary, high traction is priority
    public static final double
      kDistP = 0.01,
      kDistI = 0.00005,
      kDistD = 0.007;

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // In degrees
    public static final double kBalanceSpeed = 0.08;
  }

  public static class IntakeConstants {
    public static final double kTiltGearbox = (double) 1/35;
    public static final double kWheelGearbox = (double) 1/3;
    // TOOD: Tune
    public static final double kTiltMaxSpeed = 1; // TODO: Lower

    // Wheel speed
    public static double kIntakeSpeed = 0.3;
    public static double kOuttakeSpeed = -1;
    public static double kHoldingSpeed = 0.1;

    // Manual intake movement
    public static final double kUpSpeed = 0.1;
    public static final double kDownSpeed = 0.08;

    // TODO: Tune positions to new intake mount
    // Preset positions and tolerances (in rotations)
    public static final double kUpPos = 0.003;
    public static final double kDownPos = 0.29;
    public static final double kPosTolerance = 0.015;

    // Intake tilt gains
    public static final double
      kDownP = kTiltGearbox * 29,
      kDownI = kTiltGearbox * 22,
      kDownD = kTiltGearbox * 11;
    public static final double
      kUpP = kTiltGearbox * 31,
      kUpI = kTiltGearbox * 21,
      kUpD = kTiltGearbox * 10;
    public static final double
      kSteadyP = kTiltGearbox * 33,
      kSteadyI = kTiltGearbox * 23,
      kSteadyD = kTiltGearbox * 7;
  }
}