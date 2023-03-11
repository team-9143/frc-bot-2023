// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;

public final class Autos {
  // Score a pre-loaded cube, then drive out of the community and back in
  public static final SequentialCommandGroup SideAuto(Drivetrain sDrivetrain, DriveDistance driveDistance, TurnToAngle turnToAngle, Intake intake, Command outtake) {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(new WaitCommand(1), outtake),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-48)),
      
      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(36))
    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  public static final SequentialCommandGroup CenterAuto(Drivetrain sDrivetrain, Balance balance, DriveDistance driveDistance, TurnToAngle turnToAngle, Command outtake) {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(new WaitCommand(1), outtake),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-48)),
      
      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(24)),

      balance
    );
  }
}