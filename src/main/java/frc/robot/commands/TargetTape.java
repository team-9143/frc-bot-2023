// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TargetTape extends CommandBase {
  private final Limelight limelight;
  private final Drivetrain drivetrain;
  private final TurnToAngle turnToAngle;
  
  public TargetTape(Limelight limelight, Drivetrain drivetrain, TurnToAngle turnToAngle) {
    this.limelight = limelight;
    this.drivetrain = drivetrain;
    this.turnToAngle = turnToAngle;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(limelight);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Turns to calibration angle
    turnToAngle.setHeading(OI.gyro.getAngle() + limelight.getTx());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}