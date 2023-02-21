// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.DrivetrainConstants;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class Drive extends CommandBase {

  public Drive(Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double trigger = OI.driver_cntlr.getTriggerButtons();
    if ((trigger < -0.1) || (trigger > 0.1)) {
      // Turn in place, input from trigger
      Drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult*OI.driver_cntlr.getTriggerButtons(), 0, true);
    } else {
      // Regular drive, input from left stick
      Drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult*OI.driver_cntlr.getLeftStick()[0], -DrivetrainConstants.kSpeedMult*OI.driver_cntlr.getLeftStick()[1], true);
    }
  }
}