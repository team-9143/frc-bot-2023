// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.DrivetrainConstants;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnToAngle extends CommandBase {
  private static double heading;
  
  /** Creates a new Drive. */
  public TurnToAngle(Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    double turnAngle = (heading - OI.gyro.getAngle()) % 360;
    turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
    double turnAngleMult = (double) turnAngle / 180;
    
    if (Math.abs(turnAngle) > DrivetrainConstants.kTurnDeadspot) {
      Drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult * Math.copySign((turnAngleMult*turnAngleMult * (1-DrivetrainConstants.kTurnPower)) + DrivetrainConstants.kTurnPower, turnAngleMult), 0, false);
    } else {
      // Stop command when within turning deadspot
      Drivetrain.robotDrive.stopMotor();
      cancel();
    }
  }

  public void setHeading(double fheading) {
    heading = fheading;
    schedule();
  }

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