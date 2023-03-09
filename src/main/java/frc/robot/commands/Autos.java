// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public final class Autos {
  // Auto to score a pre-loaded cube, then pick up and score a cube from in front of the community
  public static final SequentialCommandGroup SideAuto(DriveDistance driveDistance, TurnToAngle turnToAngle, Intake intake, Command outtake) {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(new WaitCommand(1), outtake),
      new InstantCommand(() -> driveDistance.setDistance(24)),
      driveDistance
    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  public static final SequentialCommandGroup CenterAuto(Balance balance, DriveDistance driveDistance, TurnToAngle turnToAngle, Command ottake) {
    return new SequentialCommandGroup(

    );
  }
}