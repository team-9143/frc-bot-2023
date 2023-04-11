package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

public class IntakeUp extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(intakeTilt);
  public static final PIDController m_controller = new PIDController(IntakeConstants.kUpP, IntakeConstants.kUpI, IntakeConstants.kUpD);

  @Override
  public void initialize() {
    m_controller.reset();
    IntakeTilt.m_setpoint = IntakeConstants.kUpPos;
  }

  @Override
  public void execute() {
    intakeTilt.set(m_controller.calculate(intakeTilt.getPosition()));
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    IntakeTilt.enable();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }
}