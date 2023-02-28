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
  
  private final TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
  private final Balance cBalance = new Balance(sDrivetrain);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // TODO: Configure Pigeon mount position and rotation error
    // Config pigeon
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
    // D-pad or right stick input will turn to the given angle
    new Trigger(() ->
      OI.driver_cntlr.getPOV() != -1
      || Math.abs(OI.driver_cntlr.getRightStick()[0]) > 0.3
      || Math.abs(OI.driver_cntlr.getRightStick()[1]) > 0.3
    )
      .whileTrue(new RunCommand(() ->
        cTurnToAngle.findHeading()
      ));
    
    // Button 'X' will reset gyro
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_X)
      .onTrue(new InstantCommand(() -> 
        // OI.gyro.reset()
        OI.pigeon.setYaw(0)
      ));
    
    // Button 'B' will stop robot turning
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_B)
      .onTrue(new InstantCommand(() -> 
        sDrivetrain.stop()
      ));
    
    // Right bumper (hold) will cause robot to balance on a charge station
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_RB)
      .whileTrue(cBalance);
    
    // Button 'A' (hold) will cause robot to target nearest retroreflective tape, if target is nearby
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_A)
      .and(() -> sLimelight.getArea() > 10)
      .whileTrue(new FunctionalCommand(
        () -> {},
        // () -> cTurnToAngle.setHeading(OI.gyro.getAngle() + sLimelight.getTx()),
        () -> cTurnToAngle.setHeading(OI.pigeon.getYaw() + sLimelight.getTx()),
        interrupted -> sDrivetrain.stop(),
        () -> false,
        sLimelight
      ));
    
    // Button 'Y' will toggle through limelight LED
    new JoystickButton(OI.driver_cntlr, LogitechController.BTN_Y)
      .onTrue(new InstantCommand(() -> 
        sLimelight.setLedMode((sLimelight.getLedMode() <= 1) ? 3 : sLimelight.getLedMode()-1)
      ));
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