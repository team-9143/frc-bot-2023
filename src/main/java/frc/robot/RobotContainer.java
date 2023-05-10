// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.devices.CustomController.btn;

// TODO: Combine triggers so that commands cannot be independently scheduled from different triggers
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private static RobotContainer m_instance;

  /** @return the singleton instance */
  public static RobotContainer getInstance() {
    if (m_instance == null) {
      m_instance = new RobotContainer();
    }
    return m_instance;
  }

  // Initialize commands
  private static final Command cStop = new RunCommand(() -> {
    Drivetrain.stop();
    IntakeWheels.stop();
    IntakeTilt.disableSteady();
  }, Drivetrain.getInstance(), IntakeWheels.getInstance(), IntakeTilt.getInstance())
    .withInterruptBehavior(InterruptionBehavior.kCancelIncoming);

  /** The container for the robot. Intializes subsystems, teleop command bindings, and OI devices. */
  private RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.24665457, -179.574783);
    OI.pigeon.setYaw(0);

    configurePIDControllers();

    configureBindings();
  }

  /** Configure settings for PID controllers. */
  private void configurePIDControllers() {
    TurnToAngle.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kTurnMaxSpeed, Constants.DrivetrainConstants.kTurnMaxSpeed);
    TurnToAngle.m_controller.setTolerance(Constants.DrivetrainConstants.kTurnPosTolerance, Constants.DrivetrainConstants.kTurnVelTolerance);
    TurnToAngle.m_controller.enableContinuousInput(-180, 180);
    TurnToAngle.m_controller.setSetpoint(0);

    DriveDistance.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kDistMaxSpeed, Constants.DrivetrainConstants.kDistMaxSpeed);
    DriveDistance.m_controller.setTolerance(Constants.DrivetrainConstants.kDistPosTolerance, Constants.DrivetrainConstants.kDistVelTolerance);
    DriveDistance.m_controller.setSetpoint(0);

    IntakeTilt.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeTilt.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);

    IntakeDown.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeDown.m_controller.setSetpoint(Constants.IntakeConstants.kDownPos);

    IntakeUp.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeUp.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);
  }

  /** Initialize button bindings. */
  private void configureBindings() {
    // Universal:
    // Button 'B' (hold) will continuously stop all movement
    new Trigger(() -> OI.driver_cntlr.getRawButton(btn.B.val) || OI.operator_cntlr.getRawButton(btn.B.val))
      .whileTrue(cStop);

    configureDriver();
    configureOperator();
  }

  private void configureDriver() {
    final TurnToAngle cTurnToAngle = new TurnToAngle(0);

    // D-pad will turn to the specified angle
    CommandScheduler.getInstance().getDefaultButtonLoop().bind(() -> {
      if (TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() != -1) {
        cTurnToAngle.setHeading(OI.driver_cntlr.getPOV());
        cTurnToAngle.schedule();
      }
    });

    // Button 'A' (hold) will auto-balance
    new Trigger(() -> OI.driver_cntlr.getRawButton(btn.A.val))
      .whileTrue(Drivetrain.getInstance().getBalanceCommand());

    // Button 'X' (debounced 1s) will reset gyro
    new Trigger(() -> OI.driver_cntlr.getRawButton(btn.X.val))
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        OI.pigeon.setYaw(0)
      ));

    // Button 'Y' will toggle TurnToAngle
    CommandScheduler.getInstance().getDefaultButtonLoop().bind(() -> {
      if (OI.driver_cntlr.getRawButtonPressed(btn.Y.val)) {
        cTurnToAngle.cancel();
        TurnToAngle.m_enabled ^= true;
      }
    });
  }

  private void configureOperator() {
    final IntakeUp cIntakeUp = new IntakeUp();

    // Button 'A' will invert intake wheels (for cones)
    CommandScheduler.getInstance().getDefaultButtonLoop().bind(() -> {
      if (OI.operator_cntlr.getRawButtonPressed(btn.A.val)) {
        IntakeWheels.invert();
      }
    });

    // Button 'X' (debounced 1s) will reset intake tilt encoders
    new Trigger(() -> OI.operator_cntlr.getRawButton(btn.X.val))
    .debounce(1)
      .onTrue(new InstantCommand(
        IntakeTilt.getInstance()::resetEncoders
      ));

    // Button 'Y' will toggle automatic intake control
    CommandScheduler.getInstance().getDefaultButtonLoop().bind(() -> {
      if (OI.operator_cntlr.getRawButtonPressed(btn.Y.val)) {
        if (IntakeTilt.isSteadyEnabled()) {IntakeTilt.disableSteady();} else {IntakeTilt.enableSteady();}
      }
    });

    // Button 'LB' (hold) will shoot cubes
    new Trigger(() -> OI.operator_cntlr.getRawButton(btn.LB.val))
      .whileTrue(IntakeWheels.getInstance().getShootCommand());

    // Button 'RB' (hold) will lower and activate intake, then raise on release
    new Trigger(() -> OI.operator_cntlr.getRawButton(btn.RB.val))
      .whileTrue(new IntakeDown().alongWith(IntakeWheels.getInstance().getIntakeCommand()))
      .onFalse(cIntakeUp);

    // Triggers will disable intake and manually move up (LT) and down (RT)
    new Trigger(() -> Math.abs(OI.operator_cntlr.getTriggers()) > 0.05)
      .whileTrue(new FunctionalCommand(
        IntakeTilt::disableSteady,
        () -> IntakeTilt.getInstance().set(
          Math.pow(OI.operator_cntlr.getTriggers(), 2) *
          ((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed)
        ),
        interrupted -> IntakeTilt.disableSteady(),
        () -> false,
        IntakeTilt.getInstance()
      ));

    // D-pad up will angle down, then shoot
    new Trigger(() -> OI.operator_cntlr.getPOV() == 0)
      .whileTrue(new AimMid())
      .onFalse(cIntakeUp)
    .debounce(0.5)
      .whileTrue(IntakeWheels.getInstance().getShootCommand());

    // D-pad right will spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 90)
      .whileTrue(IntakeWheels.getInstance().getSpitCommand());

    // D-pad down will angle down, then spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 180)
      .whileTrue(new AimMid())
      .onFalse(cIntakeUp)
    .debounce(0.5)
      .whileTrue(IntakeWheels.getInstance().getSpitCommand());

    // D-pad left will intake
    new Trigger(() -> OI.operator_cntlr.getPOV() == 270)
      .whileTrue(IntakeWheels.getInstance().getIntakeCommand());
  }

  /** Stops all motors and disables PID controllers. */
  public static void stop() {
    cStop.execute();
  }
}