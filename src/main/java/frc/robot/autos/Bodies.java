package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.OI;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeWheels;

/** Contains auton bodies. */
public class Bodies {
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  /** A command handling the main body of an auton. Moves the drivetrain. */
  public static Command getBody(AutoSelector.Body body) {
    switch (body) {
      case ESCAPE_LONG:
        return LongEscape();
      case ESCAPE_SHORT:
        return ShortEscape();
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

  /** Drive backwards out of the community's longer side, then turn around. */
  private static Command LongEscape() {
    return new SequentialCommandGroup(
      new DriveDistance(-150),
      new TurnToAngle(180)
    );
  }

  /** Drive backwards out of the community's shorter side, then turn around. */
  private static Command ShortEscape() {
    return new SequentialCommandGroup(
      new DriveDistance(-90),
      new TurnToAngle(180)
    );
  }

  /** Turn around and pickup a cone (inverts the intake wheels). */
  private static Command PickupCone() {
    return new SequentialCommandGroup(
      new DriveDistance(-165), // Move near cone
      new TurnToAngle(180),
      new InstantCommand(() -> {if (IntakeWheels.isInverted()) {IntakeWheels.invert();}}),

      new ParallelCommandGroup(
        new IntakeDown(),
        sIntakeWheels.getIntakeCommand(),
        new WaitCommand(2.5).andThen(new RunCommand(() -> sDrivetrain.moveStraight(0.1), sDrivetrain))
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