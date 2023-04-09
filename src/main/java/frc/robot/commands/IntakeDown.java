// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.IntakeTilt;

public class IntakeDown extends PIDCommand {
  private final IntakeTilt intakeTilt;
  private static final PIDController m_controller = new PIDController(IntakeConstants.kDownP, IntakeConstants.kDownI, IntakeConstants.kDownD);

  public IntakeDown(IntakeTilt intakeTilt) {
    super(
      m_controller,
      intakeTilt::getMeasurement,
      () -> IntakeConstants.kDownPos,
      output -> intakeTilt.useOutput(output, IntakeConstants.kDownPos)
    );

    this.intakeTilt = intakeTilt;

    m_controller.setIntegratorRange(-IntakeConstants.kTiltMaxSpeed, IntakeConstants.kTiltMaxSpeed);

    addRequirements(intakeTilt);
  }

  @Override
  public void initialize() {
    intakeTilt.disable();
    m_controller.reset();
  }
}