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
    public static final byte kDriverControllerPort = 0;
    public static final byte
      kFrontLeftCANid = 1,
      kBackLeftCANid = 2,
      kFrontRightCANid = 3,
      kBackRightCANid = 4,
      kPigeonCANid = 5,
      kIntakeCANid = 6,
      kIntakePositionCANid = 17;
  }
  
  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 0.5; // In feet
    public static final double kGearboxRatio = 12.761;
    
    // General driving
    public static final double kSpeedMult = 1;
    public static final double kTurnMult = 0.7; // For normal turning
    
    // TurnToAngle
    public static final double kTurnDeadspot = 1.5;
    public static final double kTurnPower = 0.2;

    // Charge station balancing
    public static final double kPitchDeadspot = 2;
  }

  public static class IntakeConstants {
    public static final double kIntakeSpeed = 1;
    public static final double kOuttakeSpeed = -1;
    public static final double kPositionalSpeed = 0.25;

    public static final double kUpPos = 0;
    public static final double kDownPos = 0.25;
    public static final double kPositionDeadspot = 0.05;
  }
}