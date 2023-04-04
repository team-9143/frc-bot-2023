// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
    PickupCone,
    CenterOver,
    CenterSimple,
    None
  }
  public static enum Ending {
    TurnAround,
    None
  }

  public static Command getAuto(Starter starter, Body body, Ending end, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels, Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
      getStarter(starter, sIntakeTilt, sIntakeWheels).raceWith(new RunCommand(sDrivetrain::stop, sDrivetrain)),
      getBody(body, sDrivetrain, sIntakeTilt, sIntakeWheels),
      getEnd(end, sDrivetrain, sIntakeTilt, sIntakeWheels)
    );
  }

  /** A command to handle the preloaded game piece. Does not move the drivetrain. */
  private static Command getStarter(Starter starter, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (starter) {
      case Shoot:
        // Shoot
        return sIntakeWheels.getShootCommand().withTimeout(0.5);
      case Spit:
        // Spit
        return sIntakeWheels.getSpitCommand().withTimeout(0.5);
      case ShootDown:
        // Aim down, shoot, then move intake up
        return new SequentialCommandGroup(
          sIntakeTilt.getAimMidCommand(),
          sIntakeWheels.getShootCommand().withTimeout(0.5),
          new IntakeUp(sIntakeTilt).until(sIntakeTilt::atUpPos)
        );
      case SpitDown:
        // Aim down, spit, then move intake up
        return new SequentialCommandGroup(
          sIntakeTilt.getAimMidCommand(),
          sIntakeWheels.getSpitCommand().withTimeout(0.5),
          new IntakeUp(sIntakeTilt).until(sIntakeTilt::atUpPos)
        );
      default:
        return new InstantCommand();
    }
  }

  /** A command contining the main body of the auton. Moves the drivetrain. */
  private static Command getBody(Body body, Drivetrain sDrivetrain, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (body) {
      case LongEscape:
        return LongEscape(sDrivetrain);
      case ShortEscape:
        return ShortEscape(sDrivetrain);
      case PickupCone:
        return PickupCone(sDrivetrain, sIntakeTilt, sIntakeWheels);
      case CenterOver:
        return CenterOver(sDrivetrain);
      case CenterSimple:
        return CenterSimple(sDrivetrain);
      default:
        return new InstantCommand();
    }
  }

  /** A command for the end of the auton. Moves the drivetrain. */
  private static Command getEnd(Ending end, Drivetrain sDrivetrain, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (end) {
      case TurnAround:
        // Turn 180 degrees
        return new TurnToAngle(sDrivetrain, 180);
      default:
        return new InstantCommand();
    }
  }

  /** Drive backwards out of the community's longer side */
  private static Command LongEscape(Drivetrain sDrivetrain) {
    return new DriveDistance(sDrivetrain, -150);
  }

  /** Drive backwards out of the community's shorter side */
  private static Command ShortEscape(Drivetrain sDrivetrain) {
    return new DriveDistance(sDrivetrain, -90);
  }

  /** Turn around and pickup a cone (inverts the intake wheels) */
  private static Command PickupCone(Drivetrain sDrivetrain, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      new TurnToAngle(sDrivetrain, 180),
      new DriveDistance(sDrivetrain, 165), // Move near cone
      new InstantCommand(sIntakeWheels::invert),

      new ParallelCommandGroup(
        new IntakeDown(sIntakeTilt),
        sIntakeWheels.getIntakeCommand(),
        new SequentialCommandGroup(
          new WaitCommand(2.5),
          new RunCommand(() -> sDrivetrain.moveStraight(0.1), sDrivetrain)
        )
      ).until(() -> sDrivetrain.getAvgPosition() >= 204),

      new IntakeUp(sIntakeTilt).until(sIntakeTilt::atUpPos)
    );
  }

  /** Drive backwards over the charge station, then drive back and balance */
  private static Command CenterOver(Drivetrain sDrivetrain) {
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
  private static Command CenterSimple(Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
      // Move back until pitch is greater than 10
      new FunctionalCommand(
        () -> {},
        () -> sDrivetrain.moveStraight(-0.45),
        interrupted -> {},
        () -> OI.pigeon.getPitch() > 10,
        sDrivetrain
      ),

      new RunCommand(() -> sDrivetrain.moveStraight(-0.35), sDrivetrain).withTimeout(1),

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