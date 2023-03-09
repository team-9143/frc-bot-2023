// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends PIDCommand {
  private static double m_distance = 0;

  public DriveDistance(Drivetrain drivetrain) {
    super(
      new PIDController(DrivetrainConstants.kDistP, DrivetrainConstants.kDistI, DrivetrainConstants.kDistD),
      () -> drivetrain.getAvgPosition(),
      () -> m_distance,
      output -> drivetrain.robotDrive.arcadeDrive(0, output)
    );

    addRequirements(drivetrain);

    // Configure additional PID options
    getController().setTolerance(DrivetrainConstants.kDistPosTolerance, DrivetrainConstants.kDistVelTolerance);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  /**
   * Sets target distance
   *
   * @param fdistance Target distance (in inches)
   */
  public void setDistance(double fdistance) {
    m_distance = fdistance;
  }
}