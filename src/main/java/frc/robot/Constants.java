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
    public static final byte kDriverCntlrPort = 1;
    public static final byte kOperatorCntlrPort = 0;
    public static final byte
      kFrontLeftID = 3,
      kBackLeftID = 4,
      kFrontRightID = 2,
      kBackRightID = 1,
      kPigeonID = 5,
      kIntakeWheelsID = 6,
      kIntakeTiltID = 17;
  }

  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 6; // In inches
    public static final double kGearboxRatio = 12.761;

    // Teleop driving
    public static final double kSpeedMult = 1; // Applies to all manual drivetrain movement
    public static final double kTurnMult = 0.7;

    // TurnToAngle
    public static final double kTurnPosTolerance = 1.5; // Position tolerance (in degrees)
    public static final double kTurnVelTolerance = kTurnPosTolerance*100; // Velocity tolerance (in degrees/s)
    public static final double kTurnMaxSpeed = 1;
    public static final double
      kTurnP = 0.005,
      kTurnI = 0.0035,
      kTurnD = 0.0025;

    // DriveDistance
    public static final double kDistPosTolerance = 2; // Position tolerance (in inches)
    public static final double kDistVelTolerance = kDistPosTolerance*100; // Velocity tolerance (in inches/s)
    public static final double kDistMaxSpeed = 1;
    public static final double
      kDistP = 0.005,
      kDistI = 0.003,
      kDistD = 0.003;

    // Charge station balancing
    public static final double kBalanceTolerance = 2; // In degrees
    public static final double kBalanceSpeed = 0.075;

    // Speed for center auton
    public static final double kAutonSpeed = 0.2;
  }

  public static class IntakeConstants {
    public static final double kTiltGearbox = (double) 1/28;

    // Cube intake/outtake wheel speed
    public static final double kIntakeSpeed = 0.3;
    public static final double kOuttakeSpeed = -1;

    // TODO: Test and tune speed as necessary
    // Manual intake movement
    public static final double kUpSpeed = 0.1;
    public static final double kDownSpeed = -0.08;

    // Preset positions in rotations
    public static final double kUpPos = -0.02;
    public static final double kDownPos = (double) -75/360;

    // Intake tilt gains
    public static final double
      kDownP = 0.36,
      kDownI = 0.4,
      kDownD = 0.3;
    public static final double
      kUpP = 0.36,
      kUpI = 0.4,
      kUpD = 0.3;
  }
}