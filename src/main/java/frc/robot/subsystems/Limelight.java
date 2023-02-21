// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Limelight extends SubsystemBase {
  private final NetworkTableEntry
    tv = OI.limelight.getEntry("tv"),
    tx = OI.limelight.getEntry("tx"),
    ty = OI.limelight.getEntry("ty"),
    ta = OI.limelight.getEntry("ta"),
    
    // Visual testing purposes
    ledMode = OI.limelight.getEntry("ledMode");

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
  public void setLedMode(int mode) {ledMode.setNumber(mode);}

  public double getTx() {return tx.getDouble(0);}
  public double getTy() {return ty.getDouble(0);}
  public double getArea() {return ta.getDouble(0);}
  public boolean getValid() {return (tv.getInteger(0) == 1) ? true : false;}
  // TODO: Add AprilTag and 3D space related variables
  
  // Visual testing purposes
  public int getLedMode() {return (int) ledMode.getInteger(0);}
}