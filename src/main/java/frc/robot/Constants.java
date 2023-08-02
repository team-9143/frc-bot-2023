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
      kFrontLeftID = 1,
      kFrontRightID = 2,
      kBackLeftID = 3,
      kBackRightID = 4,
      kPigeonID = 5,
      kIntakeWheelsID = 6,
      kIntakeTiltLeftID = 7,
      kIntakeTiltRightID = 8;
  }

  public static class DrivetrainConstants {
    // Applies to all teleop driving (exponential)
    public static final double kSpeedMult = 1; // Change this value to change driving speed [0..1]
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
    public static final double kTiltMaxSpeed = 0.45;

    // Delay to shoot/spit a game piece
    public static final double kShootTimer = 0.5;

    // Non-PID intake movement
    public static final double
      kUpSpeed = PhysConstants.kTiltGearbox * -3.5,
      kDownSpeed = PhysConstants.kTiltGearbox * 2.8,
      kSteadySpeed = PhysConstants.kTiltGearbox * -0.35;

    // Preset positions and tolerances (UNIT: degrees)
    public static final double
      kUpPos = 1.08,
      kMidPos = 36,
      kDownPos = 104.4;
    public static final double
      kUpPosTolerance = -9, // Check as error > tolerance
      kMidPosTolerance = 1.5, // Check as abs(error) > tolerance
      kDownPosTolerance = 2; // Check as error < tolerance

    // Intake tilt PID gains
    public static final double
      kDownP = PhysConstants.kTiltGearbox * 0.0806,
      kDownI = PhysConstants.kTiltGearbox * 0.0612,
      kDownD = PhysConstants.kTiltGearbox * 0.0305;
    public static final double
      kUpP = PhysConstants.kTiltGearbox * 0.0862,
      kUpI = PhysConstants.kTiltGearbox * 0.0584,
      kUpD = PhysConstants.kTiltGearbox * 0.0277;
    public static final double
      kSteadyP = PhysConstants.kTiltGearbox * 0.0917,
      kSteadyI = PhysConstants.kTiltGearbox * 0.0639,
      kSteadyD = PhysConstants.kTiltGearbox * 0.0194;
  }
}