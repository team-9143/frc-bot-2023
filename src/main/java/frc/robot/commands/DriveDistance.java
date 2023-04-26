package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.DrivetrainConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

/** Drives a given distance for autons. Realistically moves about 10% less due to slip. */
public class DriveDistance extends CommandBase {
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(drivetrain);
  private static boolean isRunning = false;
  public static final PIDController m_controller = new PIDController(DrivetrainConstants.kDistP, DrivetrainConstants.kDistI, DrivetrainConstants.kDistD);

  private final double distance; // UNIT: inches

  public DriveDistance(double distance) {
    this.distance = distance;
  }

  /** Reset controller and encoders. */
  @Override
  public void initialize() {
    m_controller.reset();
    drivetrain.resetEncoders();
    isRunning = true;
  }

  /** Calculate and clamp controller output to max speed. */
  @Override
  public void execute() {
    drivetrain.moveStraight(Math.max(-DrivetrainConstants.kDistMaxSpeed, Math.min(
      m_controller.calculate(drivetrain.getPosition(), distance),
    DrivetrainConstants.kDistMaxSpeed)));
  }

  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    Drivetrain.stop();
    m_controller.setSetpoint(0);
    isRunning = false;
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }

  public static boolean isRunning() {return isRunning;}
}