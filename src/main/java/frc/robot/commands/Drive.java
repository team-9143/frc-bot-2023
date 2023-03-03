// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.DrivetrainConstants;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase {
  private Drivetrain drivetrain;

  public Drive(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double triggers = OI.driver_cntlr.getTriggerButtons();
    if ((triggers < -0.1) || (triggers > 0.1)) {
      // Turn in place, input from trigger
      // drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult*DrivetrainConstants.kTurnMult * triggers, 0, true);
      drivetrain.robotDrive.arcadeDrive(0, 0, true);
    } else {
      // Regular drive, input from left stick
      drivetrain.robotDrive.arcadeDrive(DrivetrainConstants.kSpeedMult*DrivetrainConstants.kTurnMult * OI.driver_cntlr.getLeftStick()[0], DrivetrainConstants.kSpeedMult*OI.driver_cntlr.getLeftStick()[1], true);
    }
  }
}