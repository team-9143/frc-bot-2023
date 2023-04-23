package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

/** Tilts the intake roughly parallel to the ground. Disables steady intake. */
public class AimMid extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(intakeTilt);

  @Override
  public void initialize() {
    IntakeTilt.disableSteady();
    IntakeTilt.m_setpoint = IntakeConstants.kMidPos;
    IntakeTilt.setRunning(true);
  }

  /** Moves toward mid position with a static speed, then holds upright. */
  @Override
  public void execute() {
    intakeTilt.set(
      (Math.abs(intakeTilt.getPosition() - IntakeConstants.kMidPos) < IntakeConstants.kMidPosTolerance) ?
        IntakeConstants.kSteadySpeed :
      (intakeTilt.getPosition() < IntakeConstants.kMidPos) ?
        IntakeConstants.kDownSpeed : IntakeConstants.kUpSpeed
    );
  }

  @Override
  public void end(boolean interrupted) {
    IntakeTilt.disableSteady();
    IntakeTilt.setRunning(false);
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }
}