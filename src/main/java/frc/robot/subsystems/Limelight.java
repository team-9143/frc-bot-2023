// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.commands.TargetTape;

public class Limelight extends SubsystemBase {
  private static final NetworkTableEntry
    tv = OI.limelight.getEntry("tv"),
    tx = OI.limelight.getEntry("tx"),
    ty = OI.limelight.getEntry("ty"),
    ta = OI.limelight.getEntry("ta"),
    ledMode = OI.limelight.getEntry("ledMode");

  public static boolean valid;
  public static double x, y, area;

  /** Creates a new Limelight. */
  public Limelight() {}

  @Override
  public void periodic() {
    // Update variables
    valid = (tv.getDouble(0) == 1) ? true : false;
    x = tx.getDouble(0);
    y = ty.getDouble(0);
    area = ta.getDouble(0);
    // TODO: Add AprilTag and 3D space related variables
  }

  /**
   * Sets the limelight's LED mode.
   * 
   * <pre>
   *0: Use the LED Mode set in the current pipeline
   *1: Force off
   *2: Force blink
   *3: Force on
   * </pre>
   * 
   * @param mode
   */
  public void setLedMode(int mode) {
    ledMode.setNumber(mode);
  }

  public int getLedMode() {
    return (int) ledMode.getInteger(0);
  }
}