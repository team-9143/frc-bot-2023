// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class Drive extends CommandBase {
  private static int speed = 1;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Drive() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(new Drivetrain());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double trigger = OI.m_controller.getTriggerButtons();
    if ((trigger < -0.1) || (trigger > 0.1)) {
      // Turn in place, input from trigger
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getTriggerButtons(), 0, true);
    } else {
      // Regular drive, input from left stick
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getLeftStick()[0], speed*OI.m_controller.getLeftStick()[1], true);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
