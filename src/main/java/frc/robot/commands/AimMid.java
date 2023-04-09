// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.IntakeTilt;

public class AimMid extends CommandBase {
  private final IntakeTilt intakeTilt;

  public AimMid(IntakeTilt intakeTilt) {
    this.intakeTilt = intakeTilt;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeTilt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeTilt.disable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeTilt.useOutput(
      (Math.abs(intakeTilt.getMeasurement() - IntakeConstants.kMidPos) < IntakeConstants.kMidPosTolerance) ? IntakeConstants.kSteadySpeed :
      (intakeTilt.getMeasurement() < IntakeConstants.kMidPos) ? IntakeConstants.kDownSpeed : IntakeConstants.kUpSpeed,
    IntakeConstants.kMidPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeTilt.disable();
  }
}