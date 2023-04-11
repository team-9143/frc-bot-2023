// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.shuffleboard.ShuffleboardManager;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

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

  // Initialize subsystems and commands
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();
  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();

  private final Balance cBalance = new Balance();
  private final TurnToAngle cTurnToAngle = new TurnToAngle(0);
  private final IntakeDown cIntakeDown = new IntakeDown();
  private final IntakeUp cIntakeUp = new IntakeUp();
  private final Command cIntake = sIntakeWheels.getIntakeCommand();
  private final Command cShoot = sIntakeWheels.getShootCommand();
  private final Command cSpit = sIntakeWheels.getSpitCommand();
  private final Command cAimMid = new AimMid();
  private final Command cManualHold = new StartEndCommand(
    () -> sIntakeWheels.set(Constants.IntakeConstants.kHoldingSpeed),
    IntakeWheels::stop,
    sIntakeWheels
  );
  private static final Command cStop = new RunCommand(() -> {
    Drivetrain.stop();
    IntakeWheels.stop();
    IntakeTilt.stop();
  }, sDrivetrain, sIntakeWheels, sIntakeTilt)
    .withInterruptBehavior(InterruptionBehavior.kCancelIncoming);

  /** The container for the robot. Intializes subsystems, teleop commands, and OI devices. */
  private RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.24665457, -179.574783);
    OI.pigeon.setYaw(0);

    configurePIDControllers();

    configureBindings();

    // Initialize Shuffleboard
    ShuffleboardManager.getInstance();
    Shuffleboard.disableActuatorWidgets();
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
    IntakeUp.m_controller.setTolerance(Constants.IntakeConstants.kUpPosTolerance);
    IntakeUp.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);
  }

  /** Initialize button bindings. */
  private void configureBindings() {
    // Universal:
    // Button 'B' (hold) will continuously stop all movement
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.B.val)
    .or(new JoystickButton(OI.operator_cntlr, OI.Controller.btn.B.val))
      .whileTrue(cStop);

    configureDriver();
    configureOperator();
  }

  private void configureDriver() {
    // D-pad and right stick will turn to the specified angle
    new Trigger(() -> TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() != -1)
      .whileTrue(new RunCommand(() -> {
        cTurnToAngle.setHeading(OI.driver_cntlr.getPOV());
        cTurnToAngle.schedule();
      }));

    new Trigger(() ->
      TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() == -1 &&
      (Math.abs(OI.driver_cntlr.getRightX()) > 0.2 || Math.abs(OI.driver_cntlr.getRightY()) > 0.2)
    )
      .whileTrue(new RunCommand(() -> {
        cTurnToAngle.setHeading(Math.toDegrees(Math.atan2(OI.driver_cntlr.getRightY(), OI.driver_cntlr.getRightX())) + 90);
        cTurnToAngle.schedule();
      }));

    // Button 'A' (hold) will run auto-balance code
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.A.val)
      .whileTrue(cBalance);

    // Button 'X' (debounced 1s) will reset gyro
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.X.val)
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        OI.pigeon.setYaw(0)
      ));

    // Button 'Y' will toggle TurnToAngle
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        cTurnToAngle.cancel();
        TurnToAngle.m_enabled ^= true;
      }));
  }

  private void configureOperator() {
    // Button 'A' will swap intake and outtake (for cones)
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.A.val)
      .onTrue(new InstantCommand(() -> {
        IntakeWheels.invert();

        if (cIntake.isScheduled() || cManualHold.isScheduled()) {
          sIntakeWheels.set(-sIntakeWheels.get());
        } else if (!(cShoot.isScheduled() || cSpit.isScheduled())) {
          IntakeWheels.stop();
        }
      }));

    // Button 'X' (debounced 1s) will reset tilt encoder
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        sIntakeTilt.resetEncoders()
      ));

    // Button 'Y' will toggle automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        if (IntakeTilt.isEnabled()) {IntakeTilt.disable();} else {IntakeTilt.enable();}
      }));

    // Button 'LB' (hold) will shoot cubes
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.LB.val)
      .whileTrue(cShoot);

    // Button 'RB' (hold) will lower and activate intake, then raise on release
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.RB.val)
      .whileTrue(cIntakeDown)
      .whileTrue(cIntake)
      .onFalse(cIntakeUp);

    // Triggers will disable intake and manually move up (LT) and down (RT)
    new Trigger(() -> Math.abs(OI.operator_cntlr.getTriggers()) > 0.05)
      .whileTrue(new FunctionalCommand(
        IntakeTilt::disable,
        () -> sIntakeTilt.set(
          Math.pow(OI.operator_cntlr.getTriggers(), 2) *
          ((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed)
        ),
        interrupted -> IntakeTilt.disable(),
        () -> false,
        sIntakeTilt
      ));

    // D-pad up will angle down, then shoot
    new Trigger(() -> OI.operator_cntlr.getPOV() == 0)
      .whileTrue(cAimMid)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimMidTimer)
      .whileTrue(cShoot);

    // D-pad right will spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 90)
      .whileTrue(cSpit);

    // D-pad down will angle down, then spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 180)
      .whileTrue(cAimMid)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimMidTimer)
      .whileTrue(cSpit);

    // D-pad left will hold pieces
    new Trigger(() -> OI.operator_cntlr.getPOV() == 270)
      .whileTrue(cManualHold);
  }

  /** Stops all motors, disables PID controllers, and cancels commands requiring actuator subsystems. */
  public static void stop() {
    cStop.execute();
  }
}