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
  private final static int turn_deadspot = 2;
  private static double heading;
  private static boolean is_turning = false;
  
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
    // Adjust and round right stick input
    double rs_X = OI.m_controller.getRightStick()[0], rs_Y = OI.m_controller.getRightStick()[1];
    if (Math.abs(rs_X) < 0.3 && Math.abs(rs_Y) < 0.3) {
      rs_X = 0;
      rs_Y = 0;
    }

    if (OI.m_controller.getPOV() != -1) {
      // Snap heading to 45 degrees, input from d-pad
      heading = 45 * Math.round((float) OI.m_controller.getPOV() / 45);
      heading = (heading == 360) ? 0 : heading;
      is_turning = true;  
    } else if (rs_X != 0 || rs_Y != 0){
      // Find specific angle, input from right stick
      heading = Math.toDegrees(Math.atan2(rs_X, rs_Y));
      heading += (heading < 0) ? 360 : 0;
      is_turning = true;
    }

    // Turning to a heading
    if (is_turning) {
      double turnAngle = heading - (OI.gyro.getAngle() % 360);
      turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
      double turnAngleMult = (double) turnAngle / 180;
      
      System.out.println("gyro angle: " + OI.gyro.getAngle()%360 + " turn angle: " + turnAngleMult);
      
      if (Math.abs(turnAngle) > turn_deadspot) {
        RobotContainer.m_robotDrive.arcadeDrive((Math.copySign((0.8*turnAngleMult*turnAngleMult) + 0.2, turnAngleMult)), 0, false);
      } else {
        is_turning = false;
        RobotContainer.m_robotDrive.arcadeDrive(0, 0);
      } 
    } 
  }

  public void setHeading(double fHeading) {
    heading = fHeading;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}