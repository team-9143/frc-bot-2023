// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends PIDCommand {
  private final Drivetrain drivetrain;
  private static double m_distance = 0; // In inches

  public DriveDistance(Drivetrain drivetrain) {
    super(
      new PIDController(DrivetrainConstants.kDistP, DrivetrainConstants.kDistI, DrivetrainConstants.kDistD),
      () -> drivetrain.getAvgPosition(),
      () -> m_distance,
      output -> drivetrain.moveStraight(Math.max(-DrivetrainConstants.kDistMaxSpeed, Math.min(output, DrivetrainConstants.kDistMaxSpeed)))
    );

    this.drivetrain = drivetrain;

    addRequirements(drivetrain);

    // Configure additional PID options
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
    // TODO: make sure that PIDController.atSetpoint() is comparing velocities in the same units (e.g. not RPM to degrees/s) and that RelativeEncoder.setVelocityConversionFactor() is working as expected
    return m_controller.atSetpoint();
  }

  /**
   * Sets target distance
   *
   * @param fdistance Target distance (in inches)
   */
  public static void setDistance(double fdistance) {
    m_distance = fdistance;
  }
}