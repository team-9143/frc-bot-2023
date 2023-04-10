package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.DrivetrainConstants;

import frc.robot.subsystems.Drivetrain;

public class TurnToAngle extends CommandBase {
  public static final PIDController m_controller = new PIDController(DrivetrainConstants.kTurnP, DrivetrainConstants.kTurnI, DrivetrainConstants.kTurnD);

  private final Drivetrain drivetrain;
  public static boolean m_enabled = false;
  private double heading;

  public TurnToAngle(Drivetrain drivetrain, double heading) {
    this.drivetrain = drivetrain;
    this.heading = heading;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    m_controller.reset();
  }

  @Override
  public void execute() {
    drivetrain.turnInPlace(Math.max(-DrivetrainConstants.kTurnMaxSpeed, Math.min(
      m_controller.calculate(-OI.pigeon.getYaw(), heading),
    DrivetrainConstants.kTurnMaxSpeed)));
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  public void setHeading(double heading) {this.heading = heading;}
}