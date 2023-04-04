// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// TODO: DriveDistance and TurnToAngle gains must be tuned
/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class PhysConstants {
    public static final double kDrivetrainGearbox = 1/8.45;
    public static final double kTiltGearbox = (double) 1/35;
    public static final double kWheelGearbox = (double) 1/3;

    public static final double kWheelCircumference = 6 * Math.PI; // In inches
  }

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
    // Manual driving
    public static final double kSpeedMult = 1;
    public static final double kTurnMult = 0.7;

    // TurnToAngle
    public static final double kTurnPosTolerance = 1.5; // Position tolerance (in degrees)
    public static final double kTurnVelTolerance = kTurnPosTolerance; // Velocity tolerance (in degrees/s)
    public static final double kTurnMaxSpeed = 0.75;
    public static final double
      kTurnP = 0.008,
      kTurnI = 0.003,
      kTurnD = 0.003;

    // DriveDistance
    public static final double kDistPosTolerance = 2; // Position tolerance (in inches)
    public static final double kDistVelTolerance = kDistPosTolerance; // Velocity tolerance (in inches/s)
    public static final double kDistMaxSpeed = 0.4; // Time efficiency is unnecessary, high traction is priority
    public static final double
      kDistP = 0.04,
      kDistI = 0.00003,
      kDistD = 0.007;

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // In degrees
    public static final double kBalanceSpeed = 0.08;
  }

  public static class IntakeConstants {
    public static final double kTiltMaxSpeed = 0.7; // TODO: Lower max tilt motor speed

    // Wheel speed
    public static double kIntakeSpeed = 0.3;
    public static double kOuttakeSpeed = -1;
    public static double kSpitSpeed = -0.5;
    public static double kHoldingSpeed = 0.05;

    // Non-PID intake movement
    public static final double kUpSpeed = -0.1;
    public static final double kDownSpeed = 0.08;
    public static final double kAimDownTimer = 0.4; // Seconds to move down for aiming straight
    public static final double kAutoAlignSpeed = -0.15;
    // TODO: Test and tune maximum current against NEO specs
    public static final double kMaxCurrent = 80; // To check against getOutputCurrent() for autoAlign

    // Preset positions and tolerances (in rotations)
    public static final double kUpPos = 0.003;
    public static final double kMidPos = 0.15; // TODO: Test mid position
    public static final double kDownPos = 0.29;
    public static final double kPosTolerance = 0.015;

    // Maximum threshold to be considered at a position (in rotations)
    public static final double kUpPosThreshold = 0.025; // Large to account for play in the up position
    public static final double kMidPosThreshold = 0.004;

    // Intake tilt PID gains
    public static final double
      kDownP = PhysConstants.kTiltGearbox * 29,
      kDownI = PhysConstants.kTiltGearbox * 22,
      kDownD = PhysConstants.kTiltGearbox * 11;
    public static final double
      kUpP = PhysConstants.kTiltGearbox * 31,
      kUpI = PhysConstants.kTiltGearbox * 21,
      kUpD = PhysConstants.kTiltGearbox * 10;
    public static final double
      kSteadyP = PhysConstants.kTiltGearbox * 33,
      kSteadyI = PhysConstants.kTiltGearbox * 23,
      kSteadyD = PhysConstants.kTiltGearbox * 7;
  }
}