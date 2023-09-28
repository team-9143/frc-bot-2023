package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

/** Tilts the intake fully down to pick up game pieces. Disables steady intake. */
public class IntakeDown extends CommandBase {
  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(sIntakeTilt);

  public static final PIDController m_controller = new PIDController(IntakeConstants.kDownP.getAsDouble(), IntakeConstants.kDownI.getAsDouble(), IntakeConstants.kDownD.getAsDouble());
  static {
    IntakeConstants.kDownP.bindTo(m_controller::setP);
    IntakeConstants.kDownI.bindTo(m_controller::setI);
    IntakeConstants.kDownD.bindTo(m_controller::setD);
    m_controller.setIntegratorRange(-IntakeConstants.kTiltMaxSpeed, IntakeConstants.kTiltMaxSpeed);
    m_controller.setSetpoint(IntakeConstants.kDownPos);
  }

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
    sIntakeTilt.set(m_controller.calculate(sIntakeTilt.getPosition()));
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