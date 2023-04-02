// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.util.sendable.SendableRegistry;
import frc.robot.OI;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class TurnToAngle extends PIDCommand {
  public static boolean m_enabled = false;
  private static double m_heading = 0;

  public TurnToAngle(Drivetrain drivetrain) {
    super(
      new PIDController(DrivetrainConstants.kTurnP, DrivetrainConstants.kTurnI, DrivetrainConstants.kTurnD),
      () -> -OI.pigeon.getYaw(),
      () -> m_heading,
      output -> drivetrain.turnInPlace(Math.max(-DrivetrainConstants.kTurnMaxSpeed, Math.min(output, DrivetrainConstants.kTurnMaxSpeed)))
    );

    addRequirements(drivetrain);
    SendableRegistry.setSubsystem(m_controller, drivetrain.getSubsystem());

    // Configure additional PID options
    m_controller.setIntegratorRange(-DrivetrainConstants.kTurnMaxSpeed, DrivetrainConstants.kTurnMaxSpeed);
    m_controller.setTolerance(DrivetrainConstants.kTurnPosTolerance, DrivetrainConstants.kTurnVelTolerance);
    m_controller.enableContinuousInput(-180, 180);
    m_controller.setSetpoint(0);
  }

  public TurnToAngle(Drivetrain drivetrain, double fheading) {
    this(drivetrain);
    setHeading(fheading);
  }

  @Override public void execute() {
    super.execute();
    if (
      Math.abs(OI.driver_cntlr.getLeftX()) > 0.05
      || Math.abs(OI.driver_cntlr.getLeftY()) > 0.05
      || Math.abs(OI.driver_cntlr.getTriggers()) > 0.05
    ) {
      cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  /**
   * Sets target heading and resets PID controller
   *
   * @param fheading Target heading (in degrees)
   */
  public void setHeading(double fheading) {
    if (Math.abs(fheading - m_heading) > 50) {
      m_controller.reset();
    }
    m_heading = fheading;
  }

  public static double getHeading() {return m_heading;}
}