package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.DriveDistance;
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.Balance;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeTilt;
import frc.robot.subsystems.IntakeWheels;

/** Contains auton bodies. */
public class Bodies {
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  /** A command handling the main body of an auton. Moves the drivetrain.
   *
   * @param body
   * @return the command
   */
  public static Command getBody(AutoSelector.Body body) {
    switch (body) {
      case ESCAPE_LONG:
        // Drive backwards out of the community's longer side
        return new DriveDistance(-150);
      case ESCAPE_SHORT:
        // Drive backwards out of the community's shorter side
        return new DriveDistance(-90);
      case PICKUP_CONE:
        return PickupCone();
      case CENTER_OVER:
        return CenterOver();
      case CENTER_SIMPLE:
        return CenterSimple();
      default:
        return new InstantCommand();
    }
  }

  /** Turn around and pickup a cone (inverts the intake wheels). */
  private static Command PickupCone() {
    return new SequentialCommandGroup(
      new DriveDistance(-165), // Move near cone
      new TurnToAngle(180),
      new InstantCommand(IntakeWheels::toCone),

      new ParallelCommandGroup(
        new IntakeDown(),
        sIntakeWheels.getIntakeCommand(),
        new WaitUntilCommand(() ->
          IntakeTilt.getInstance().getPosition() < IntakeConstants.kDownPosTolerance
        ).andThen(new RunCommand(() -> sDrivetrain.moveStraight(0.1), sDrivetrain))
      ).until(() -> sDrivetrain.getPosition() >= -125),

      new IntakeUp()
    );
  }

  /** Drive backwards over the charge station, then drive back and balance. */
  private static Command CenterOver() {
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

      new Balance()
    );
  }

  /** Drive backwards to the charge station and balance. */
  private static Command CenterSimple() {
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

      new Balance()
    );
  }
}