// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.DrivetrainConstants;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnToAngle extends CommandBase {
  private Drivetrain drivetrain;
  private static double heading;
  
  public TurnToAngle(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // double turnAngle = (heading - OI.gyro.getAngle()) % 360;
    double turnAngle = (heading - OI.pigeon.getYaw()) % 360;
    turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
    double turnAngleMult = turnAngle / 180;
    
    if (Math.abs(turnAngle) > DrivetrainConstants.kTurnDeadspot) {
      drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult * Math.copySign((turnAngleMult*turnAngleMult * (1-DrivetrainConstants.kDirectedTurnPower)) + DrivetrainConstants.kDirectedTurnPower, turnAngleMult), 0, false);
    } else {
      // Stop when within turning deadspot
      drivetrain.stop();
    }
  }

  // Main way to schedule command
  public void setHeading(double fheading) {
    heading = fheading;
    schedule();
  }

  // Called when a trigger shows sufficient input
  public void findHeading() {
    if (OI.driver_cntlr.getPOV() != -1) {
      // Round heading to 45 degrees, input from d-pad
      setHeading(45 * Math.round((float) OI.driver_cntlr.getPOV() / 45));
    } else {
      // Find specific angle, input from right stick
      setHeading(Math.toDegrees(Math.atan2(
        OI.driver_cntlr.getRightStick()[0],
        OI.driver_cntlr.getRightStick()[1]
      )));
    }
  }
}