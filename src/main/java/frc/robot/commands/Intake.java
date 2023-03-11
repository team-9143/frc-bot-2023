// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.IntakeTilt;
import frc.robot.subsystems.IntakeWheels;

public class Intake extends CommandBase {
  private final IntakeTilt intakeTilt;
  private final IntakeWheels intakeWheels;

  public Intake(IntakeTilt intakeTilt, IntakeWheels intakeWheels) {
    this.intakeTilt = intakeTilt;
    this.intakeWheels = intakeWheels;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeTilt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Sets setpoint down and starts intake
    intakeTilt.setSetpoint(IntakeConstants.kDownPos);
    intakeTilt.enable();
    intakeWheels.intake_motor.set(IntakeConstants.kIntakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Sets default setpoint and stops intake
    intakeTilt.setSetpoint(IntakeConstants.kUpPos);
    intakeWheels.stop();
  }
}