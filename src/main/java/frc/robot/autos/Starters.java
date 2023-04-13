package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.*;
import frc.robot.subsystems.IntakeWheels;

/** Contains auton starters. */
public class Starters {
  /** @return a command to handle a preloaded game piece. Does not move the drivetrain */
  public static Command getStarter(AutoSelector.Starter starter) {
    switch (starter) {
      case SHOOT:
        return Shoot().beforeStarting(IntakeWheels::toCube);
      case SPIT:
        return Spit().beforeStarting(IntakeWheels::toCube);
      case SHOOT_DOWN:
        return ShootDown().beforeStarting(IntakeWheels::toCube);
      case SPIT_DOWN:
        return SpitDown().beforeStarting(IntakeWheels::toCube);
      default:
        return new InstantCommand();
    }
  }

  /** Shoot a game piece. */
  public static Command Shoot() {
    return IntakeWheels.getInstance().getShootCommand().withTimeout(IntakeConstants.kShootTimer);
  }

  /** Spit a game piece. */
  public static Command Spit() {
    return IntakeWheels.getInstance().getSpitCommand().withTimeout(IntakeConstants.kShootTimer);
  }

  /** Aim down, shoot, then move intake up. */
  public static Command ShootDown() {
    return new SequentialCommandGroup(
      new AimMid().raceWith(
        new WaitCommand(IntakeConstants.kAimMidTimer).andThen(Shoot())
      ),
      new IntakeUp()
    );
  }

  /** Aim down, spit, then move intake up. */
  public static Command SpitDown() {
    return new SequentialCommandGroup(
      new AimMid().raceWith(
        new WaitCommand(IntakeConstants.kAimMidTimer).andThen(Spit())
      ),
      new IntakeUp()
    );
  }
}