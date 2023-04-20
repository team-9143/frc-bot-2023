package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.TurnToAngle;
import frc.robot.commands.Balance;

/** Contains auton endings. */
public class Endings {
  /** A command handling the end of an auton. Moves the drivetrain.
   *
   * @param end
   * @param body
   * @return the command
   */
  public static Command getEnding(AutoSelector.Ending end) {
    switch (end) {
      case TURN_AWAY:
        // Turn to face away from the drive station
        return new TurnToAngle(180);
      case TURN_CLOSE:
        // Turn to face the drive station
        return new TurnToAngle(0);
      case BALANCE:
        // Balance on the charge station
        return new Balance();
      default:
        return new InstantCommand();
    }
  }
}