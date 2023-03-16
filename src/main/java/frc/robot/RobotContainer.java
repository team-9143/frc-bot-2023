// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain sDrivetrain = new Drivetrain();
  private final Limelight sLimelight = new Limelight();
  private final IntakeWheels sIntakeWheels = new IntakeWheels();
  private final IntakeTilt sIntakeTilt = new IntakeTilt();

  private final Balance cBalance = new Balance(sDrivetrain);
  private final TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
  private final Intake cIntake = new Intake(sIntakeTilt, sIntakeWheels);
  private final Command cOuttake = sIntakeWheels.getOuttakeCommand();
  private final Command cStop = new RunCommand(() -> {
    sDrivetrain.stop();
    sIntakeWheels.stop();
    sIntakeTilt.disable();
    cTurnToAngle.cancel();
  });

  // Autonomous chooser declaration
  public final SendableChooser<Autos.Type> m_autonChooser = new SendableChooser<Autos.Type>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.1978197, -179.08374);
    OI.pigeon.setYaw(0);

    // Configure autonomous choices
    m_autonChooser.addOption("Long Auto", Autos.Type.Long);
    m_autonChooser.addOption("Short Auto", Autos.Type.Short);
    m_autonChooser.addOption("Center Auto", Autos.Type.Center);
    m_autonChooser.addOption("Simple Center Auto", Autos.Type.CenterSimple);
    m_autonChooser.setDefaultOption("None", Autos.Type.None);

    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Drive Controller:
    // D-pad and right stick will turn to the specified angle
    new Trigger(() -> OI.driver_cntlr.getPOV() != -1)
      .whileTrue(new RunCommand(() -> {
        cTurnToAngle.setHeading(Math.round((float) OI.driver_cntlr.getPOV() / 45) * 45);
        cTurnToAngle.schedule();
      }));

    new Trigger(() ->
      OI.driver_cntlr.getPOV() == -1 &&
      (Math.abs(OI.driver_cntlr.getRightX()) > 0.2 || Math.abs(OI.driver_cntlr.getRightY()) > 0.2)
    )
    .whileTrue(new RunCommand(() -> {
      cTurnToAngle.setHeading(Math.toDegrees(Math.atan2(OI.driver_cntlr.getRightY() + 90, OI.driver_cntlr.getRightX())));
      cTurnToAngle.schedule();
    }));

    // Button 'X' will reset gyro
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.X.val)
      .onTrue(new InstantCommand(() ->
        OI.pigeon.setYaw(0)
      ));

    // Button 'B' (hold) will continuously stop all movement
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.B.val)
      .whileTrue(cStop);

    // Button 'A' (hold) will cause robot to balance on a charge station
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.A.val)
      .whileTrue(cBalance);

    // Operator Controller:
    // Button 'B' (hold) will continuously stop all movement
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.B.val)
      .whileTrue(cStop);

    // Button 'A' will disable automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.A.val)
      .onTrue(new InstantCommand(() -> {
        sIntakeTilt.disable();
      }));

    // Button 'Y' will enable automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        sIntakeTilt.enable();
      }));

    // Button 'X' will reset tilt encoder
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
      .onTrue(new InstantCommand(() -> {
        sIntakeTilt.resetEncoder();
      }));

    // Triggers will disable intake and manually move up (LT) and down (RT)
    new Trigger(() -> Math.abs(OI.operator_cntlr.getTriggers()) > 0.05)
      .whileTrue(new FunctionalCommand(
        () -> sIntakeTilt.disable(),
        () -> sIntakeTilt.useOutput(((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed), 0),
        interrupted -> sIntakeTilt.disable(),
        () -> false,
        sIntakeTilt
      ));

    // Button 'LB' (hold) will spit cubes
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.LB.val)
      .whileTrue(cOuttake);

    // Button 'RB' (hold) will lower and activate intake
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.RB.val)
      .whileTrue(cIntake);
  }

  /** Stops all motors and disables PID controllers */
  public void stop() {
    cStop.execute();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Autos.getAuto(m_autonChooser.getSelected(), sDrivetrain, sIntakeWheels);
  }
}