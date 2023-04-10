package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends CommandBase {
  public static final PIDController m_controller = new PIDController(DrivetrainConstants.kDistP, DrivetrainConstants.kDistI, DrivetrainConstants.kDistD);

  private final Drivetrain drivetrain;
  private double distance; // UNIT: inches

  public DriveDistance(Drivetrain drivetrain, double distance) {
    this.drivetrain = drivetrain;
    this.distance = distance;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    m_controller.reset();
    drivetrain.resetEncoders();
  }

  @Override
  public void execute() {
    drivetrain.moveStraight(Math.max(-DrivetrainConstants.kDistMaxSpeed, Math.min(
      m_controller.calculate(drivetrain.getAvgPosition(), distance),
    DrivetrainConstants.kDistMaxSpeed)));
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}