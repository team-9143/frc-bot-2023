// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.DrivetrainConstants;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Balance extends CommandBase {
  private final Drivetrain drivetrain;
  private double previousPitch;
  
  public Balance(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    previousPitch = OI.pigeon.getPitch();

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double pitch = OI.pigeon.getPitch();
    // TODO: move forward while pitch is positive, backward while pitch is negative; remember that pitch changes very quickly and the robot should stop at any small change
    if (Math.abs(pitch) > DrivetrainConstants.kPitchDeadspot) {
      if (Math.abs(pitch - previousPitch) > 1) {
        drivetrain.stop();
      } else {
        drivetrain.robotDrive.arcadeDrive(0, DrivetrainConstants.kSpeedMult * 0.2, false);
      }
    } else {
      cancel();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}
