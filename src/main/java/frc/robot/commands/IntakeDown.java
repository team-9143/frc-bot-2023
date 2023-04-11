package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

public class IntakeDown extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(intakeTilt);
  public static final PIDController m_controller = new PIDController(IntakeConstants.kDownP, IntakeConstants.kDownI, IntakeConstants.kDownD);

  @Override
  public void initialize() {
    IntakeTilt.disable();
    m_controller.reset();
    IntakeTilt.m_setpoint = IntakeConstants.kDownPos;
  }

  @Override
  public void execute() {
    intakeTilt.set(m_controller.calculate(intakeTilt.getPosition()));
  }

  @Override
  public void end(boolean interrupted) {
    IntakeTilt.disable();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }
}