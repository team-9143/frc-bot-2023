package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Constants.DrivetrainConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

/** Adjusts drivetrain to balance on a charge station. */
public class Balance extends CommandBase {
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(drivetrain);
  
  private double previousPitch = -OI.pigeon.getPitch();

  /** Move forward while tilting backward and vice versa. */
  @Override
  public void execute() {
    // Pitch should increases to the back
    double pitch = -OI.pigeon.getPitch();

    if (Math.abs(pitch) > DrivetrainConstants.kBalanceTolerance && Math.abs(pitch - previousPitch) < 3) {
      drivetrain.moveStraight(Math.copySign(DrivetrainConstants.kSpeedMult * DrivetrainConstants.kBalanceSpeed, pitch));
    } else {
      // Stop movement on a large pitch change (usually denoting a fall) or when stabilized
      Drivetrain.stop();
    }

    previousPitch = pitch;
  }

  @Override
  public void end(boolean interrupted) {
    Drivetrain.stop();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }
}