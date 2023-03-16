// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeWheels;

public final class Autos {
  public static enum Type {
    Long,
    Short,
    Center,
    None
  }

  public static Command getAuto(Type type, Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    switch(type) {
      case Long:
        return LongAuto(sDrivetrain, sIntakeWheels);
      case Short:
        return ShortAuto(sDrivetrain, sIntakeWheels);
      case Center:
        return CenterAuto(sDrivetrain, sIntakeWheels);
      default:
        return new InstantCommand();
    }
  }

  // Score a pre-loaded cube, then drive out of the community and back in
  private static Command LongAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(1),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-140))
    );
  }
  
  // Score a pre-loaded cube, then drive out of the community and back in
  private static Command ShortAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(1),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-80))
    );
  }

  // Auto to score a pre-loaded cube, drive over the charge station, then drive back and balance
  private static Command CenterAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(1),

      // Move back until pitch is greater than 10
      new FunctionalCommand(
        () -> sDrivetrain.moveStraight(-0.25),
        () -> {},
        interrupted -> sDrivetrain.stop(),
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      // Move back until pitch is less than -10
      new FunctionalCommand(
        () -> sDrivetrain.moveStraight(-0.25),
        () -> {},
        interrupted -> sDrivetrain.stop(),
        () -> OI.pigeon.getPitch() < -10,
        sDrivetrain
      ),

      // Move back until pitch is close to flat
      new FunctionalCommand(
        () -> sDrivetrain.moveStraight(-0.25),
        () -> {},
        interrupted -> sDrivetrain.stop(),
        () -> Math.abs(OI.pigeon.getPitch()) < DrivetrainConstants.kBalanceTolerance,
        sDrivetrain
      ),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-5)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(16)),

      new Balance(sDrivetrain)
    );
  }
}