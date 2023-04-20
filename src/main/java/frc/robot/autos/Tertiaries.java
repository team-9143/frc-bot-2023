package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.IntakeWheels;

/** Contains auton tertiaries. */
public class Tertiaries {
  /** A command handling the tertiary body of an auton. Does not move the drivetrain.
   *
   * @param tertiary
   * @param body
   * @return the command
   */
  public static Command getTertiary(AutoSelector.Tertiary tertiary) {
    switch (tertiary) {
      case CONE_SHOOT:
        return Starters.TimedShoot().beforeStarting(IntakeWheels::toCone);
      case CONE_SPIT:
        return Starters.TimedSpit().beforeStarting(IntakeWheels::toCone);
      case CONE_SHOOT_DOWN:
        return Starters.TimedShootDown().beforeStarting(IntakeWheels::toCone);
      case CONE_SPIT_DOWN:
        return Starters.TimedShootDown().beforeStarting(IntakeWheels::toCone);
      default:
        return new InstantCommand();
    }
  }
}