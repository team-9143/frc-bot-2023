// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeWheels;

public final class Autos {
  public static enum Type {
    Long,
    Short,
    Center,
    CenterSimple,
    Outtake,
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
      case CenterSimple:
        return CenterSimpleAuto(sDrivetrain, sIntakeWheels);
      case Outtake:
        return OuttakeAuto(sDrivetrain, sIntakeWheels);
      default:
        return new InstantCommand();
    }
  }

  // Score a pre-loaded cube, then drive out of the community
  private static Command LongAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-140))
    );
  }

  // Score a pre-loaded cube, then drive out of the community
  private static Command ShortAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(-80))
    );
  }

  // Score a pre-loaded cube, drive over the charge station, then drive back and balance
  private static Command CenterAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      // Move back until pitch is greater than 10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-DrivetrainConstants.kAutonSpeed),
        interrupted -> {},
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      // Move back until pitch is less than -10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-DrivetrainConstants.kAutonSpeed),
        interrupted -> {},
        () -> OI.pigeon.getPitch() < -10,
        sDrivetrain
      ),

      // Move back until pitch is close to flat
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-DrivetrainConstants.kAutonSpeed),
        interrupted -> {},
        () -> Math.abs(OI.pigeon.getPitch()) < 2,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(-DrivetrainConstants.kAutonSpeed * 2), sDrivetrain).withTimeout(0.25),

      new RunCommand(() -> sDrivetrain.moveStraight(DrivetrainConstants.kAutonSpeed * 1.5), sDrivetrain).withTimeout(1.5),

      new Balance(sDrivetrain)
    );
  }

  // Score a pre-loaded cube, then drive to the charge station and balance
  private static Command CenterSimpleAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);

    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      cTurnToAngle.beforeStarting(() -> cTurnToAngle.setHeading(0)),

      // Move back until pitch is less than -10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(DrivetrainConstants.kAutonSpeed * 1.85),
        interrupted -> {},
        () -> OI.pigeon.getPitch() < -10,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(DrivetrainConstants.kAutonSpeed * 1.85)).withTimeout(1),

      new Balance(sDrivetrain)
    );
  }
  private static Command OuttakeAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels){
    TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);

    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),
      cTurnToAngle.beforeStarting(() -> cTurnToAngle.setHeading(0))
    );
  }
}