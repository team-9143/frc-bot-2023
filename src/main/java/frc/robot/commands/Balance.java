// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class Balance extends CommandBase {
  private final Drivetrain drivetrain;
  private double previousPitch = 0;

  public Balance(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    previousPitch = -OI.pigeon.getPitch();

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Get pitch that increases to the back
    double pitch = -OI.pigeon.getPitch();

    if (Math.abs(pitch) > DrivetrainConstants.kBalanceTolerance) {
      if (Math.abs(pitch - previousPitch) > 3) {
        // Stop movement on a large pitch change (usually denoting a fall)
        drivetrain.stop();
      } else {
        // Move forward while tilting backward and vice versa
        drivetrain.robotDrive.arcadeDrive(0, Math.copySign(DrivetrainConstants.kSpeedMult * 0.075, pitch), false);
      }
    }

    previousPitch = pitch;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}