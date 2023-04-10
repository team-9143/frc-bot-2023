package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

public class AimMid extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeTilt.disable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeTilt.useOutput(
      (Math.abs(intakeTilt.getMeasurement() - IntakeConstants.kMidPos) < IntakeConstants.kMidPosTolerance) ? IntakeConstants.kSteadySpeed :
      (intakeTilt.getMeasurement() < IntakeConstants.kMidPos) ? IntakeConstants.kDownSpeed : IntakeConstants.kUpSpeed,
    IntakeConstants.kMidPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeTilt.disable();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return Set.of(intakeTilt);
  }
}