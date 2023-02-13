// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TargetTape extends CommandBase {
  private final Drivetrain drivetrain;
  private final Limelight limelight;
  
  /** Creates a new TargetTape. */
  public TargetTape(Limelight limelight, Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    this.limelight = limelight;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(limelight, drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Turns to calibration angle
    drivetrain.turnDegrees(limelight.getTx());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.m_robotDrive.stopMotor();
  }
}