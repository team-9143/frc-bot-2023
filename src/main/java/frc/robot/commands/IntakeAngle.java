// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakePositional;

public class IntakeAngle extends CommandBase {
  private final IntakePositional intakePositional;

  public IntakeAngle(IntakePositional intakePositional) {
    this.intakePositional = intakePositional;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakePositional);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // TODO: Find target position (up or down) and run until position reached by checking encoders
    intakePositional.positionMotor.set(Math.copySign(OI.driver_cntlr.getTriggerButtons() * IntakeConstants.kPositionalSpeed, OI.driver_cntlr.getTriggerButtons()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakePositional.stop();
  }
}