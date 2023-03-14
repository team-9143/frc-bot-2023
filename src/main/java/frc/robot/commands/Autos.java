// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeWheels;
import edu.wpi.first.wpilibj2.command.Command;

public final class Autos {
  public static enum Type {
    Side,
    Center,
    None
  }

  public static Command getAuto(Type type, Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    switch(type) {
      case Side:
        return SideAuto(sDrivetrain, sIntakeWheels);
      case Center:
        return CenterAuto(sDrivetrain, sIntakeWheels);
      default:
        return new InstantCommand();
    }
  }

  // Score a pre-loaded cube, then drive out of the community and back in
  private static Command SideAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(1),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-48)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(36))
    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  private static Command CenterAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(1),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-48)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(24)),

      new Balance(sDrivetrain)
    );
  }
}