package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

/** Tilts the intake fully down to pick up game pieces. Disables steady intake. */
public class IntakeDown extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(intakeTilt);
  public static final PIDController m_controller = new PIDController(IntakeConstants.kDownP, IntakeConstants.kDownI, IntakeConstants.kDownD);

  /** Reset controller. */
  @Override
  public void initialize() {
    IntakeTilt.disableSteady();
    m_controller.reset();
    IntakeTilt.setSetpoint(IntakeConstants.kDownPos);
    IntakeTilt.setRunning(true);
  }

  @Override
  public void execute() {
    intakeTilt.set(m_controller.calculate(intakeTilt.getPosition()));
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