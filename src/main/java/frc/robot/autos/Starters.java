// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

/** Contains auton starters */
public class Starters {
  /** A command to handle a preloaded game piece. Does not move the drivetrain. */
  public static Command getStarter(AutoSelector.Starter starter, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (starter) {
      case CUBE_SHOOT:
        // Shoot
        return sIntakeWheels.getShootCommand().withTimeout(0.5);

      case CUBE_SPIT:
        // Spit
        return sIntakeWheels.getSpitCommand().withTimeout(0.5);

      case CUBE_SHOOT_DOWN:
        // Aim down, shoot, then move intake up
        return new SequentialCommandGroup(
          new AimMid(sIntakeTilt).raceWith(
            new WaitCommand(IntakeConstants.kAimMidTimer).andThen(sIntakeWheels.getShootCommand().withTimeout(0.5))
          ),
          new IntakeUp(sIntakeTilt)
        );

      case CUBE_SPIT_DOWN:
        // Aim down, spit, then move intake up
        return new SequentialCommandGroup(
          new AimMid(sIntakeTilt).raceWith(
            new WaitCommand(IntakeConstants.kAimMidTimer).andThen(sIntakeWheels.getSpitCommand().withTimeout(0.5))
          ),
          new IntakeUp(sIntakeTilt)
        );

      default:
        return new InstantCommand();
    }
  }
}