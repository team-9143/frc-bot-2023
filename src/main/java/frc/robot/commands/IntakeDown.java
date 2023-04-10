package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

public class IntakeDown extends CommandBase {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  public static final PIDController m_controller = new PIDController(IntakeConstants.kDownP, IntakeConstants.kDownI, IntakeConstants.kDownD);

  @Override
  public void initialize() {
    intakeTilt.disable();
    m_controller.reset();
  }

  @Override
  public void execute() {
    intakeTilt.useOutput(m_controller.calculate(intakeTilt.getMeasurement()), IntakeConstants.kDownPos);
  }

  @Override
  public void end(boolean interrupted) {
    intakeTilt.stop();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return Set.of(intakeTilt);
  }
}