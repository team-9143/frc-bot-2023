// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.util.TunableNumber;

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
    public static final double kDrivetrainGearbox = 1/7.31;
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
    // Applies to all teleop driving
    public static final double kSpeedMult = 1; // Change driving speed [0..1] (squared before application)
    public static final double kTurnMult = Math.sqrt(0.85); // Change turning speed [0..1] (squared before application)

    // TurnToAngle
    public static final double kTurnPosTolerance = 0.75; // UNIT: degrees
    public static final double kTurnVelTolerance = kTurnPosTolerance; // UNIT: degrees/s
    public static final double kTurnMaxSpeed = 0.3;
    public static final TunableNumber
      kTurnP = new TunableNumber("P", 0.018, "TurnToAngle"),
      kTurnI = new TunableNumber("I", 0.0006, "TurnToAngle"),
      kTurnD = new TunableNumber("D", 0.0045, "TurnToAngle");

    // DriveDistance
    public static final double kDistPosTolerance = 2; // UNIT: inches
    public static final double kDistVelTolerance = kDistPosTolerance; // UNIT: inches/s
    public static final double kDistMaxSpeed = 0.4; // Traction is priority
    public static final TunableNumber
      kDistP = new TunableNumber("P", 0.04, "DriveDistance"),
      kDistI = new TunableNumber("I", 0.00003, "DriveDistance"),
      kDistD = new TunableNumber("D", 0.007, "DriveDistance");

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // UNIT: degrees
    public static final TunableNumber kBalanceSpeed = new TunableNumber("BalanceSpeed", 0.08);
  }

  // TODO: Move wheel speeds here and use boolean conditional instead of `*= -1` for inversion
  public static class IntakeConstants {
    public static final double kTiltMaxSpeed = 0.45;

    // Delay to shoot/spit a game piece
    public static final double kShootTimer = 0.5;

    // Non-PID intake movement
    public static final TunableNumber
      kUpSpeed = new TunableNumber("Up", PhysConstants.kTiltGearbox * -3.5, "IntakeAim"),
      kDownSpeed = new TunableNumber("Down", PhysConstants.kTiltGearbox * 2.8, "IntakeAim"),
      kSteadySpeed = new TunableNumber("Steady", PhysConstants.kTiltGearbox * -0.35, "IntakeAim");

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
    public static final TunableNumber
      kDownP = new TunableNumber("P", PhysConstants.kTiltGearbox * 0.0806, "IntakeDown"),
      kDownI = new TunableNumber("I", PhysConstants.kTiltGearbox * 0.0612, "IntakeDown"),
      kDownD = new TunableNumber("D", PhysConstants.kTiltGearbox * 0.0305, "IntakeDown");
    public static final TunableNumber
      kUpP = new TunableNumber("P", PhysConstants.kTiltGearbox * 0.0862, "IntakeUp"),
      kUpI = new TunableNumber("I", PhysConstants.kTiltGearbox * 0.0584, "IntakeUp"),
      kUpD = new TunableNumber("D", PhysConstants.kTiltGearbox * 0.0277, "IntakeUp");
    public static final TunableNumber
      kSteadyP = new TunableNumber("P", PhysConstants.kTiltGearbox * 0.0917, "IntakeSteady"),
      kSteadyI = new TunableNumber("I", PhysConstants.kTiltGearbox * 0.0639, "IntakeSteady"),
      kSteadyD = new TunableNumber("D", PhysConstants.kTiltGearbox * 0.0194, "IntakeSteady");
  }
}