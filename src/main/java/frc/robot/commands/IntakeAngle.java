// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakePositional;
import frc.robot.LogitechController;
import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;

public class IntakeAngle extends CommandBase {
  private final IntakePositional intakePositional;

  public IntakeAngle(IntakePositional intakePositional) {
    this.intakePositional = intakePositional;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakePositional);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Sets setpoint: up position if RB is pressed, otherwise up
    intakePositional.setSetpoint((OI.driver_cntlr.getRawButton(LogitechController.BTN_RB)) ? IntakeConstants.kUpPos : IntakeConstants.kDownPos);
    intakePositional.enable();
    
    OI.driver_cntlr.getRawButtonPressed(LogitechController.BTN_RB);
    OI.driver_cntlr.getRawButtonPressed(LogitechController.BTN_LB);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (OI.driver_cntlr.getRawButtonPressed(LogitechController.BTN_RB)) {
      intakePositional.setSetpoint(IntakeConstants.kUpPos);
    } else if (OI.driver_cntlr.getRawButtonPressed(LogitechController.BTN_LB)) {
      intakePositional.setSetpoint(IntakeConstants.kDownPos);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {intakePositional.stop();}
}