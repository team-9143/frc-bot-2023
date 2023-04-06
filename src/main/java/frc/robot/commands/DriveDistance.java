// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.util.sendable.SendableRegistry;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends PIDCommand {
  private final Drivetrain drivetrain;
  private static final PIDController m_controller = new PIDController(DrivetrainConstants.kDistP, DrivetrainConstants.kDistI, DrivetrainConstants.kDistD);

  public DriveDistance(Drivetrain drivetrain, double distance) {
    super(
      m_controller,
      drivetrain::getAvgPosition,
      () -> distance,
      output -> drivetrain.moveStraight(Math.max(-DrivetrainConstants.kDistMaxSpeed, Math.min(output, DrivetrainConstants.kDistMaxSpeed)))
    );

    this.drivetrain = drivetrain;

    addRequirements(drivetrain);
    SendableRegistry.setSubsystem(m_controller, drivetrain.getSubsystem());

    // Configure additional PID options
    m_controller.setIntegratorRange(-DrivetrainConstants.kDistMaxSpeed, DrivetrainConstants.kDistMaxSpeed);
    m_controller.setTolerance(DrivetrainConstants.kDistPosTolerance, DrivetrainConstants.kDistVelTolerance);
    m_controller.setSetpoint(0);
  }

  @Override
  public void initialize() {
    super.initialize();
    drivetrain.resetEncoders();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }
}