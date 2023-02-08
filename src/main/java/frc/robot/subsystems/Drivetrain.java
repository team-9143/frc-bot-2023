package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Drive;

public class Drivetrain extends SubsystemBase {
  public Drivetrain() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive(this));
  }

  @Override
  public void periodic() {
    // TODO: handle logic to schedule TurnToAngle here
  }
}