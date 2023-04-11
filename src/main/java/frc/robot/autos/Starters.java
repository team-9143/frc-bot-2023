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
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  /** @return a command to handle a preloaded game piece. Does not move the drivetrain */
  public static Command getStarter(AutoSelector.Starter starter) {
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
          new AimMid().raceWith(
            new WaitCommand(IntakeConstants.kAimMidTimer).andThen(sIntakeWheels.getShootCommand().withTimeout(0.5))
          ),
          new IntakeUp()
        );

      case CUBE_SPIT_DOWN:
        // Aim down, spit, then move intake up
        return new SequentialCommandGroup(
          new AimMid().raceWith(
            new WaitCommand(IntakeConstants.kAimMidTimer).andThen(sIntakeWheels.getSpitCommand().withTimeout(0.5))
          ),
          new IntakeUp()
        );

      default:
        return new InstantCommand();
    }
  }
}