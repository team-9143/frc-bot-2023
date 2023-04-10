package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.IntakeConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.IntakeTilt;

public class IntakeDown extends PIDCommand {
  private static final IntakeTilt intakeTilt = IntakeTilt.getInstance();
  public static final PIDController m_controller = new PIDController(IntakeConstants.kDownP, IntakeConstants.kDownI, IntakeConstants.kDownD);

  public IntakeDown() {
    super(
      m_controller,
      intakeTilt::getMeasurement,
      () -> IntakeConstants.kDownPos,
      output -> intakeTilt.useOutput(output, IntakeConstants.kDownPos)
    );
  }

  @Override
  public void initialize() {
    intakeTilt.disable();
    m_controller.reset();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return Set.of(intakeTilt);
  }
}