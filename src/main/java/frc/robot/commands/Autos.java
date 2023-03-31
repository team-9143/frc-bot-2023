// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.*;

public final class Autos {
  public static enum Type {
    Long,
    LongSpit,
    Short,
    ShortSpit,
    Center,
    CenterSimple,
    Outtake,
    WPIAuto,
    None
  }

  public static Command getAuto(Type type, Drivetrain sDrivetrain, IntakeWheels sIntakeWheels, IntakeTilt sIntakeTilt) {
    switch(type) {
      case Long:
        return LongAuto(sDrivetrain, sIntakeWheels);
      case LongSpit:
        return LongAutoSpit(sDrivetrain, sIntakeWheels);
      case Short:
        return ShortAuto(sDrivetrain, sIntakeWheels);
      case ShortSpit:
      return ShortAutoSpit(sDrivetrain, sIntakeWheels);
      case Center:
        return CenterAuto(sDrivetrain, sIntakeWheels);
      case CenterSimple:
        return CenterSimpleAuto(sDrivetrain, sIntakeWheels);
      case Outtake:
        return OuttakeAuto(sDrivetrain, sIntakeWheels);
      case WPIAuto:
        return WIPAuto(sDrivetrain, sIntakeWheels, sIntakeTilt);
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

  private static Command LongAutoSpit(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getSpitCommand().withTimeout(0.5),

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

  private static Command ShortAutoSpit(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getSpitCommand().withTimeout(0.5),

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
        () -> sDrivetrain.moveStraight(-0.45),
        interrupted -> {},
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      // Move back until pitch is less than -10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-0.3),
        interrupted -> {},
        () -> OI.pigeon.getPitch() < -10,
        sDrivetrain
      ),

      // Move back until pitch is close to flat
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-0.3),
        interrupted -> {},
        () -> Math.abs(OI.pigeon.getPitch()) < 2,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(-0.45), sDrivetrain).withTimeout(0.1),

      new RunCommand(() -> sDrivetrain.moveStraight(0.45), sDrivetrain).withTimeout(1.5),

      new Balance(sDrivetrain)
    );
  }

  // Score a pre-loaded cube, then drive to the charge station and balance
  private static Command CenterSimpleAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      // Move back until pitch is greater than 10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-0.45),
        interrupted -> {},
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(0.4)).withTimeout(1),

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

  private static Command WIPAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels, IntakeTilt sIntakeTilt) {
    TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
    return new SequentialCommandGroup(
      sIntakeWheels.getOuttakeCommand().withTimeout(0.5),

      new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(180)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(224)),

      new IntakeDown(sIntakeTilt).alongWith(sIntakeWheels.getIntakeCommand()).withTimeout(2),

      new IntakeUp(sIntakeTilt),

      new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(90)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(48)),

      new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(0)),

      new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(48)),

      new Balance(sDrivetrain)
    );
  }
}