package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.IntakeTilt;

public class IntakeUp extends PIDCommand {
  public static final PIDController m_controller = new PIDController(IntakeConstants.kUpP, IntakeConstants.kUpI, IntakeConstants.kUpD);

  private final IntakeTilt intakeTilt;

  public IntakeUp(IntakeTilt intakeTilt) {
    super(
      m_controller,
      intakeTilt::getMeasurement,
      () -> IntakeConstants.kUpPos,
      output -> intakeTilt.useOutput(output, IntakeConstants.kUpPos)
    );

    this.intakeTilt = intakeTilt;

    addRequirements(intakeTilt);
  }

  @Override
  public void initialize() {
    intakeTilt.disable();
    m_controller.reset();
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    intakeTilt.enable();
  }
}