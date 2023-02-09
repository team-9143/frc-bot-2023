// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnToAngle extends CommandBase {
  private final static double turn_deadspot = 1.5;
  private static double heading;
  
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurnToAngle(Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    double turnAngle = heading - (OI.gyro.getAngle() % 360);
    turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
    double turnAngleMult = (double) turnAngle / 180;
    
    System.out.println("gyro angle: " + OI.gyro.getAngle()%360 + " turn angle: " + turnAngleMult);
    
    if (Math.abs(turnAngle) > turn_deadspot) {
      RobotContainer.m_robotDrive.arcadeDrive((Math.copySign((turnAngleMult*turnAngleMult*0.5) + 0.5, turnAngleMult)), 0, false);
    } else {
      // Stop motors and cancel command when within turning deadspot
      RobotContainer.m_robotDrive.stopMotor();
      this.cancel();
    }
  }

  public void setHeading(double fHeading) {
    heading = fHeading;
  }
}