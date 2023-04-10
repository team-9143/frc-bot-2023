package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

/** Contains auton bodies */
public class Bodies {
  /** A command handling the main body of an auton. Moves the drivetrain. */
  public static Command getBody(AutoSelector.Body body, Drivetrain sDrivetrain, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    switch (body) {
      case ESCAPE_LONG:
        return LongEscape(sDrivetrain);
      case ESCAPE_SHORT:
        return ShortEscape(sDrivetrain);
      case PICKUP_CONE:
        return PickupCone(sDrivetrain, sIntakeTilt, sIntakeWheels);
      case CENTER_OVER:
        return CenterOver(sDrivetrain);
      case CENTER_SIMPLE:
        return CenterSimple(sDrivetrain);
      default:
        return new InstantCommand();
    }
  }

  /** Drive backwards out of the community's longer side, then turn around */
  private static Command LongEscape(Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
      new DriveDistance(sDrivetrain, -150),
      new TurnToAngle(sDrivetrain, 180)
    );
  }

  /** Drive backwards out of the community's shorter side, then turn around */
  private static Command ShortEscape(Drivetrain sDrivetrain) {
    return new SequentialCommandGroup(
      new DriveDistance(sDrivetrain, -90),
      new TurnToAngle(sDrivetrain, 180)
    );
  }

  /** Turn around and pickup a cone (inverts the intake wheels) */
  private static Command PickupCone(Drivetrain sDrivetrain, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels) {
    return new SequentialCommandGroup(
      new DriveDistance(sDrivetrain, -165), // Move near cone
      new TurnToAngle(sDrivetrain, 180),
      new InstantCommand(() -> {if (IntakeConstants.kIntakeSpeed > 0) {sIntakeWheels.invert();}}),

      new ParallelCommandGroup(
        new IntakeDown(sIntakeTilt),
        sIntakeWheels.getIntakeCommand(),
        new WaitCommand(2.5).andThen(new RunCommand(() -> sDrivetrain.moveStraight(0.1), sDrivetrain))
      ).until(() -> sDrivetrain.getAvgPosition() >= -125),

      new IntakeUp(sIntakeTilt)
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
}