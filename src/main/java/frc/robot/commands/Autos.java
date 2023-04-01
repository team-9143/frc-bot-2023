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
  public static enum Starter {
    Shoot,
    Spit,
    ShootDown,
    SpitDown,
    None
  }
  public static enum Body {
    LongEscape,
    ShortEscape,
    CenterOver,
    CenterSimple,
    None
  }

  public static Command getAuto(Starter starter, Body body, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels, Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(getStarter(starter, sIntakeTilt, sIntakeWheels), getBody(body, sDrivetrain));
  }

  private static Command getStarter(Starter starter, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (starter) {
      case Shoot:
        // Shoot
        return sIntakeWheels.getShootCommand().withTimeout(0.5);
      case Spit:
        // Spit
        return sIntakeWheels.getSpitCommand().withTimeout(0.5);
      case ShootDown:
        // Aim down, shoot, then start moving intake up
        return new SequentialCommandGroup(
          sIntakeTilt.getAimDownCommand(),
          sIntakeWheels.getShootCommand().withTimeout(0.5),
          new InstantCommand(new IntakeUp(sIntakeTilt)::schedule)
        );
      case SpitDown:
        // Aim down, spit, then start moving intake up
        return new SequentialCommandGroup(
          sIntakeTilt.getAimDownCommand(),
          sIntakeWheels.getSpitCommand().withTimeout(0.5),
          new InstantCommand(new IntakeUp(sIntakeTilt)::schedule)
        );
      default:
        return new InstantCommand();
    }
  }

  private static Command getBody(Body body, Drivetrain sDrivetrain) {
    switch (body) {
      case LongEscape:
        return LongEscapeBody(sDrivetrain);
      case ShortEscape:
        return ShortEscapeBody(sDrivetrain);
      case CenterOver:
        return CenterOverBody(sDrivetrain);
      case CenterSimple:
        return CenterSimpleBody(sDrivetrain);
      default:
        return new InstantCommand();
    }
  }

  /** Drive backwards out of the community's longer side */
  private static Command LongEscapeBody(Drivetrain sDrivetrain) {
    return new DriveDistance(sDrivetrain, -140);
  }

  /** Drive backwards out of the community's shorter side */
  private static Command ShortEscapeBody(Drivetrain sDrivetrain) {
    return new DriveDistance(sDrivetrain, -80);
  }

  /** Drive backwards over the charge station, then drive back and balance */
  private static Command CenterOverBody(Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
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

  /** Drive backwards to the charge station and balance */
  private static Command CenterSimpleBody(Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
      // Move back until pitch is greater than 10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-0.45),
        interrupted -> {},
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(-0.35)).withTimeout(1),

      new Balance(sDrivetrain)
    );
  }

  // private static Command WIPAuto(Drivetrain sDrivetrain, IntakeWheels sIntakeWheels, IntakeTilt sIntakeTilt) {
  //   TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
  //   return new SequentialCommandGroup(
  //     sIntakeWheels.getShootCommand().withTimeout(0.5),

  //     new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(180)),

  //     new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(224)),

  //     new IntakeDown(sIntakeTilt).alongWith(sIntakeWheels.getIntakeCommand()).withTimeout(2),

  //     new IntakeUp(sIntakeTilt),

  //     new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(90)),

  //     new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(48)),

  //     new TurnToAngle(sDrivetrain).beforeStarting(() -> cTurnToAngle.setHeading(0)),

  //     new DriveDistance(sDrivetrain).beforeStarting(() -> DriveDistance.setDistance(48)),

  //     new Balance(sDrivetrain)
  //   );
  // }
}