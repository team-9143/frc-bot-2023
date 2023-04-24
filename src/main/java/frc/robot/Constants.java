// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// TODO(HIGH prio): DriveDistance and TurnToAngle gains must be tuned
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
    public static final double kTiltGearbox = 1/35.0;
    public static final double kWheelGearbox = 1/3.0;

    public static final double kWheelCircumference = 6 * Math.PI; // UNIT: inches
  }

  public static class DeviceConstants {
    public static final byte kDriverCntlrPort = 0;
    public static final byte kOperatorCntlrPort = 1;
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
    // Applies to all teleop driving
    public static final double kSpeedMult = 1;
    public static final double kTurnMult = 0.7;

    // TurnToAngle
    public static final double kTurnPosTolerance = 0.75; // UNIT: degrees
    public static final double kTurnVelTolerance = kTurnPosTolerance; // UNIT: degrees/s
    public static final double kTurnMaxSpeed = 0.3;
    public static final double
      kTurnP = 0.018,
      kTurnI = 0.0006,
      kTurnD = 0.0045;

    // DriveDistance
    public static final double kDistPosTolerance = 2; // UNIT: inches
    public static final double kDistVelTolerance = kDistPosTolerance; // UNIT: inches/s
    public static final double kDistMaxSpeed = 0.4; // Traction is priority
    public static final double
      kDistP = 0.04,
      kDistI = 0.00003,
      kDistD = 0.007;

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // UNIT: degrees
    public static final double kBalanceSpeed = 0.08;
  }

  public static class IntakeConstants {
    public static final double kTiltMaxSpeed = 0.7; // TODO(low prio): Lower max tilt motor speed

    // Delay to shoot/spit a game piece
    public static final double kShootTimer = 0.5;

    // Non-PID intake movement
    public static final double
      kUpSpeed = PhysConstants.kTiltGearbox * -3.5,
      kDownSpeed = PhysConstants.kTiltGearbox * 2.8,
      kSteadySpeed = PhysConstants.kTiltGearbox * -0.35;

    public static final double kAutoAlignSpeed = PhysConstants.kTiltGearbox * -2.5;
    // TODO(autoAlign): Test and tune maximum current against NEO specs
    public static final double kMaxCurrent = 80; // For autoAlign

    // TODO: Change all the following into degrees
    // Preset positions and tolerances (UNIT: rotations)
    public static final double
      kUpPos = 0.003,
      kMidPos = 0.1,
      kDownPos = 0.29;
    public static final double
      kUpPosTolerance = -0.025, // Should only be checked one way
      kMidPosTolerance = 0.004,
      kDownPosTolerance = 0.0012; // Should only be checked one way

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