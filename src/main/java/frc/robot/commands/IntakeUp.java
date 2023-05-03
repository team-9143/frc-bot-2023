package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

/** Tilts the intake fully up to store and shoot game pieces. Enables steady intake on finish. */
public class IntakeUp extends CommandBase {
  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(sIntakeTilt);
  public static final PIDController m_controller = new PIDController(IntakeConstants.kUpP, IntakeConstants.kUpI, IntakeConstants.kUpD);

  /** Reset controller. */
  @Override
  public void initialize() {
    m_controller.reset();
    IntakeTilt.setSetpoint(IntakeConstants.kUpPos);
    IntakeTilt.setRunning(true);
  }

  @Override
  public void execute() {
    sIntakeTilt.set(m_controller.calculate(sIntakeTilt.getPosition()));
  }

  /** Finish when upright, and swap to steady intake. */
  @Override
  public boolean isFinished() {
    return m_controller.getPositionError() > IntakeConstants.kUpPosTolerance;
  }

  @Override
  public void end(boolean interrupted) {
    IntakeTilt.enableSteady();
    IntakeTilt.setRunning(false);
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }
}