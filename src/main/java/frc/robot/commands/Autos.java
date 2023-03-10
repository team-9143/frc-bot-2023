// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
  // Auto to score a pre-loaded cube, then pick up a cube from in front of the community and return
  public static final SequentialCommandGroup SideAuto(DriveDistance driveDistance, TurnToAngle turnToAngle, Intake intake, Command outtake) {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(new WaitCommand(1), outtake),

      turnToAngle.beforeStarting(() -> turnToAngle.setHeading(180)),

      driveDistance.beforeStarting(() -> driveDistance.setDistance(48)),

      new ParallelDeadlineGroup(new WaitCommand(1), intake),

      turnToAngle.beforeStarting(() -> turnToAngle.setHeading(180)),

      driveDistance.beforeStarting(() -> driveDistance.setDistance(36))
    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  public static final SequentialCommandGroup CenterAuto(Balance balance, DriveDistance driveDistance, TurnToAngle turnToAngle, Command outtake) {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(new WaitCommand(1), outtake),

      turnToAngle.beforeStarting(() -> turnToAngle.setHeading(180)),

      driveDistance.beforeStarting(() -> driveDistance.setDistance(48)),

      turnToAngle.beforeStarting(() -> turnToAngle.setHeading(180)),

      driveDistance.beforeStarting(() -> driveDistance.setDistance(24)),

      balance
    );
  }
}