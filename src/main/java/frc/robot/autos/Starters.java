package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.AimMid;
import frc.robot.commands.IntakeUp;
import frc.robot.subsystems.IntakeWheels;

import frc.robot.shuffleboard.ShuffleboardManager;

/** Contains auton starters. */
public class Starters {
  /** @return a command to handle a preloaded game piece. Does not move the drivetrain */
  public static Command getStarter(AutoSelector.Starter starter) {
    final Runnable invertForPiece = (ShuffleboardManager.getInstance().getCubePreloaded()) ? IntakeWheels::toCube : IntakeWheels::toCone;

    switch (starter) {
      case SHOOT:
        return TimedShoot().beforeStarting(invertForPiece);
      case SPIT:
        return TimedSpit().beforeStarting(invertForPiece);
      case SHOOT_DOWN:
        return TimedShootDown().beforeStarting(invertForPiece);
      case SPIT_DOWN:
        return TimedSpitDown().beforeStarting(invertForPiece);
      default:
        return new InstantCommand();
    }
  }

  /** Shoot a game piece. */
  public static Command TimedShoot() {
    return IntakeWheels.getInstance().getShootCommand().withTimeout(IntakeConstants.kShootTimer);
  }

  /** Spit a game piece. */
  public static Command TimedSpit() {
    return IntakeWheels.getInstance().getSpitCommand().withTimeout(IntakeConstants.kShootTimer);
  }

  /** Aim down, shoot, then move intake up. */
  public static Command TimedShootDown() {
    return new SequentialCommandGroup(
      new AimMid().raceWith(
        new WaitUntilCommand(() ->
          IntakeTilt.getInstance().getPosition() - IntakeConstants.kMidPos < IntakeConstants.kMidPosTolerance
        ).andThen(TimedShoot())
      ),
      new IntakeUp()
    );
  }

  /** Aim down, spit, then move intake up. */
  public static Command TimedSpitDown() {
    return new SequentialCommandGroup(
      new AimMid().raceWith(
        new WaitUntilCommand(() ->
          IntakeTilt.getInstance().getPosition() - IntakeConstants.kMidPos < IntakeConstants.kMidPosTolerance
        ).andThen(TimedSpit())
      ),
      new IntakeUp()
    );
  }
}