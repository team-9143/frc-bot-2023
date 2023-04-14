package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.DriveDistance;
import frc.robot.commands.TurnToAngle;

/** Contains auton secondaries. */
public class Secondaries {
  /** A command handling the secondary body of an auton. Moves the drivetrain.
   *
   * @param secondary
   * @param body
   * @return the command
   */
  public static Command getSecondary(AutoSelector.Secondary secondary, AutoSelector.Body body) {
    switch (secondary) {
      case RETURN_FROM_CONE:
        // If picking up a cone, turn and return to the grid
        if (body == AutoSelector.Body.PICKUP_CONE) {
          return new SequentialCommandGroup(
            new TurnToAngle(0),
            new DriveDistance(205)
          );
        };

      default:
        return new InstantCommand();
    }
  }
}