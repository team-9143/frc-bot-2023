// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import java.util.Map;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain sDrivetrain = new Drivetrain();
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

  // Dashboard declarations
  public final ShuffleboardTab m_driveTab = Shuffleboard.getTab("Drive");
  private final SendableChooser<Autos.Type> m_autonChooser = new SendableChooser<Autos.Type>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.1978197, -179.08374);
    OI.pigeon.setYaw(0);

    // Configure autonomous choices
    m_autonChooser.addOption("Long Auto", Autos.Type.Long);
    m_autonChooser.addOption("Long Shoot Auto", Autos.Type.LongShoot);
    m_autonChooser.addOption("Short Auto", Autos.Type.Short);
    m_autonChooser.addOption("Short Shoot Auto", Autos.Type.ShortShoot);
    m_autonChooser.addOption("Center Auto", Autos.Type.Center);
    m_autonChooser.addOption("Simple Center Auto", Autos.Type.CenterSimple);
    m_autonChooser.addOption("Just Outtake", Autos.Type.Outtake);
    m_autonChooser.setDefaultOption("None", Autos.Type.None);

    // Configure the trigger bindings
    configureBindings();

    // Initialize Shuffleboard
    configureShuffleboard();
  }

  private void configureShuffleboard() {
    m_driveTab.add("Auton chooser", m_autonChooser)
      .withPosition(6, 5)
      .withSize(5, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    m_driveTab.add("Drivetrain", Drivetrain.robotDrive)
      .withPosition(7, 0)
      .withSize(6, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 80, "show velocity vectors", true));

    m_driveTab.addDouble("Docking Angle", () -> -OI.pigeon.getPitch())
      .withPosition(4, 0)
      .withSize(3, 3)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -45, "max", 45, "show value", true));

    ShuffleboardLayout layout_1 = m_driveTab.getLayout("Rotation", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 6);
    layout_1.add("Gyro", new OI.PigeonSendable(OI.pigeon))
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
    layout_1.addBoolean("TurnToAngle Enabled", () -> TurnToAngle.m_enabled)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_2 = m_driveTab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(13, 0)
      .withSize(4, 5);
    layout_2.addBoolean("Intaking", cIntake::isScheduled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Outtaking", cOuttake::isScheduled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addDouble("Wheel Speed", IntakeWheels.intake_encoder::getVelocity)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -300, "max", 300, "center", 0));

    Shuffleboard.selectTab("Drive");
    Shuffleboard.disableActuatorWidgets();
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
        cTurnToAngle.setHeading(Math.round((float) OI.driver_cntlr.getPOV() / 45) * 45);
        cTurnToAngle.schedule();
      }));

    new Trigger(() ->
      TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() == -1 &&
      (Math.abs(OI.driver_cntlr.getRightX()) > 0.2 || Math.abs(OI.driver_cntlr.getRightY()) > 0.2)
    )
    .whileTrue(new RunCommand(() -> {
      cTurnToAngle.setHeading(Math.toDegrees(Math.atan2(OI.driver_cntlr.getRightY(), OI.driver_cntlr.getRightX()) + 90));
      cTurnToAngle.schedule();
    }));

    // Button 'A' (hold) will cause robot to balance on a charge station
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.A.val)
      .whileTrue(cBalance);

    // Button 'X' will reset gyro
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.X.val)
      .onTrue(new InstantCommand(() ->
        OI.pigeon.setYaw(0)
      ));

    // Button 'Y' will toggle TurnToAngle
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() ->
        TurnToAngle.m_enabled ^= true
      ));
  }

  private void configureOperator() {
    // Button 'A' (hold) will swap intake and outtake (for cones)
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.A.val)
      .whileTrue(new StartEndCommand(
        () -> {
          Constants.IntakeConstants.kIntakeSpeed *= -1;
          Constants.IntakeConstants.kOuttakeSpeed *= -1;
        }, () -> {
          Constants.IntakeConstants.kIntakeSpeed *= -1;
          Constants.IntakeConstants.kOuttakeSpeed *= -1;
        }
      ));

    // TODO: Un-comment
    // Button 'X' will reset tilt encoder
    // new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
    //   .onTrue(new InstantCommand(() -> {
    //     sIntakeTilt.resetEncoder();
    //   }));

    // TODO: Not working? Test
    // Button 'Y' will toggle automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        if (sIntakeTilt.isEnabled()) {sIntakeTilt.disable();} else {sIntakeTilt.enable();}
      }));

    // Button 'LB' (hold) will spit cubes
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.LB.val)
      .whileTrue(cOuttake);

    // Button 'RB' (hold) will lower and activate intake
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.RB.val)
      .whileTrue(cIntake);

    // TODO: Test, ensure working
    // Triggers will disable intake and manually move up (LT) and down (RT)
    new Trigger(() -> Math.abs(OI.operator_cntlr.getTriggers()) > 0.05)
      .whileTrue(new FunctionalCommand(
        sIntakeTilt::disable,
        () -> sIntakeTilt.useOutput(-OI.operator_cntlr.getTriggers() * ((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed), 0),
        interrupted -> {},
        () -> false,
        sIntakeTilt
      ));

    // TODO: Style
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
      .onTrue(sDrivetrain.getShootCommand(sIntakeWheels));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Autos start backwards, so robot yaw should be backward
    return Autos.getAuto(m_autonChooser.getSelected(), sDrivetrain, sIntakeWheels, sIntakeTilt)
      .beforeStarting(() -> OI.pigeon.setYaw(180));
  }

  /** Stops all motors and disables PID controllers */
  public void stop() {
    cStop.execute();
  }
}