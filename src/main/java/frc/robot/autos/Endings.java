package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.*;

/** Contains auton endings */
public class Endings {
  /** A command handling the end of an auton. Moves the drivetrain. */
  public static Command getEnding(AutoSelector.Ending end, AutoSelector.Body body) {
    switch (end) {
      case TURN_AWAY:
        // Turn to face away from the drive station
        return new TurnToAngle(180);

      case TURN_CLOSE:
        // Turn to face the drive station
        return new TurnToAngle(0);

      case RETURN_FROM_CONE:
        // If picking up a cone, turn and return to the grid
        if (body == AutoSelector.Body.PICKUP_CONE) {
          return new SequentialCommandGroup(
            new TurnToAngle(0),
            new DriveDistance(205)
          );
        }

      default:
        return new InstantCommand();
    }
  }
}