// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;

public final class Autos {
  // Auto to score a pre-loaded cube, then pick up and score a cube from in front of the community
  public static final SequentialCommandGroup SideAuto(Drivetrain drivetrain, TurnToAngle turnToAngle, Intake intake, Command outtake) {
    return new SequentialCommandGroup(

    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  public static final SequentialCommandGroup CenterAuto(Drivetrain drivetrain, TurnToAngle turnToAngle, Balance balance, Command outtake) {
    return new SequentialCommandGroup(

    );
  }
}