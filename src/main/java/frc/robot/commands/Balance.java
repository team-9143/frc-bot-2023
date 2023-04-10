package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Constants.DrivetrainConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

public class Balance extends CommandBase {
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private double previousPitch = -OI.pigeon.getPitch();

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Get pitch that increases to the back
    double pitch = -OI.pigeon.getPitch();

    if (Math.abs(pitch) > DrivetrainConstants.kBalanceTolerance) {
      if (Math.abs(pitch - previousPitch) < 3) {
        // Move forward while tilting backward and vice versa
        drivetrain.moveStraight(Math.copySign(DrivetrainConstants.kSpeedMult * DrivetrainConstants.kBalanceSpeed, pitch));
      }
    } else {
      // Stop movement on a large pitch change (usually denoting a fall) or when stabilized
      Drivetrain.stop();
    }

    previousPitch = pitch;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Drivetrain.stop();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return Set.of(drivetrain);
  }
}