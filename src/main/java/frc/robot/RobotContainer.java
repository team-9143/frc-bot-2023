// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

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
  private final IntakePositional sIntakePosition = new IntakePositional();

  private final TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
  private final Balance cBalance = new Balance(sDrivetrain);
  private final Intake cIntake = new Intake(sIntakePosition, sIntakeWheels);
  private final Command cOuttake = new FunctionalCommand(
    () -> sIntakeWheels.intakeMotor.set(Constants.IntakeConstants.kOuttakeSpeed),
    () -> {},
    (interrupted) -> sIntakeWheels.stop(),
    () -> false,
    sIntakeWheels
  );

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure Pigeon - make sure to continuously update pitch and roll offsets
    OI.pigeon.configMountPose(0, 0, 0);
    OI.pigeon.setYaw(0);

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
    /*
    // D-pad or right stick input will turn to the given angle
    new Trigger(() ->
      OI.driver_cntlr.getPOV() != -1
      || Math.abs(OI.driver_cntlr.getRightStick()[0]) > 0.3
      || Math.abs(OI.driver_cntlr.getRightStick()[1]) > 0.3
    )
      .whileTrue(new RunCommand(() ->
        cTurnToAngle.findHeading()
      ));
    */

    // Button 'X' will reset gyro
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_X)
      .onTrue(new InstantCommand(() ->
        // OI.gyro.reset()
        OI.pigeon.setYaw(0)
      ));

    // Button 'B' (hold) will continuously stop all movement
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_B)
      .whileTrue(new RunCommand(() -> {
        sDrivetrain.stop();
        sIntakePosition.stop();
        sIntakeWheels.stop();
      }));

    // Button 'A' (hold) will cause robot to balance on a charge station
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_A)
      .whileTrue(cBalance);

    // Button 'Y' will toggle through limelight LED
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_Y)
      .onTrue(new InstantCommand(() ->
        sLimelight.setLedMode((sLimelight.getLedMode() <= 1) ? 3 : sLimelight.getLedMode()-1)
      ));

    // Button 'RB' (hold) will spit cubes
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_RB)
      .whileTrue(cOuttake);

    // Button 'LB' (hold) will turn on intake
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_LB)
      .whileTrue(cIntake);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // TODO: Autonomous command chooser and commands in Autos class
    return new InstantCommand();
  }
}