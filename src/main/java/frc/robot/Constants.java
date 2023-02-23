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
  public static class OperatorConstants {
    public static final byte kDriverControllerPort = 0;
  }
  
  public static class DrivetrainConstants {
    public static final double kWheelDiameter = 0.5; // In feet
    public static final double kGearboxRatio = 12.761;
    
    public static final double kTurnDeadspot = 1.5;
    public static final double kTurnPower = 0.3;
    public static final double kSpeedMult = 1;

    public static final byte
      kFrontLeftDeviceID = 1,
      kBackLeftDeviceID = 2,
      kFrontRightDeviceID = 3,
      kBackRightDeviceID = 4;
  }
}