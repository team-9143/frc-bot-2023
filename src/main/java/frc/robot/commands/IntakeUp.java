// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.util.sendable.SendableRegistry;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.IntakeTilt;

public class IntakeUp extends PIDCommand {
  private final IntakeTilt intakeTilt;
  private static final PIDController m_controller = new PIDController(IntakeConstants.kUpP, IntakeConstants.kUpI, IntakeConstants.kUpD);

  public IntakeUp(IntakeTilt intakeTilt) {
    super(
      m_controller,
      intakeTilt::getMeasurement,
      () -> IntakeConstants.kUpPos,
      output -> intakeTilt.useOutput(output, IntakeConstants.kUpPos)
    );

    this.intakeTilt = intakeTilt;

    m_controller.setIntegratorRange(-IntakeConstants.kTiltMaxSpeed, IntakeConstants.kTiltMaxSpeed);
    m_controller.setTolerance(IntakeConstants.kPosTolerance);
    m_controller.setSetpoint(IntakeConstants.kUpPos);

    addRequirements(intakeTilt);
    SendableRegistry.setSubsystem(m_controller, intakeTilt.getSubsystem());
  }

  @Override
  public void initialize() {
    intakeTilt.disable();
    m_controller.reset();
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    intakeTilt.enable();
  }
}