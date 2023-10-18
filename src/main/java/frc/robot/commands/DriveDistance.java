package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.DrivetrainConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

/** Drives a given distance for autons. Realistically moves about 10% less due to slip. */
public class DriveDistance extends CommandBase {
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(sDrivetrain);
  private static boolean isRunning = false;

  public static final PIDController m_controller = new PIDController(DrivetrainConstants.kDistP.getAsDouble(), DrivetrainConstants.kDistI.getAsDouble(), DrivetrainConstants.kDistD.getAsDouble());
  static {
    DrivetrainConstants.kDistP.bindTo(m_controller::setP);
    DrivetrainConstants.kDistI.bindTo(m_controller::setI);
    DrivetrainConstants.kDistD.bindTo(m_controller::setD);
    m_controller.setIntegratorRange(-DrivetrainConstants.kDistMaxSpeed, DrivetrainConstants.kDistMaxSpeed);
    m_controller.setTolerance(DrivetrainConstants.kDistPosTolerance, DrivetrainConstants.kDistVelTolerance);
    m_controller.setSetpoint(0);
  }

  private final double distance; // UNIT: inches

  public DriveDistance(double distance) {
    this.distance = distance;
  }

  /** Reset controller and encoders. */
  @Override
  public void initialize() {
    m_controller.reset();
    sDrivetrain.resetEncoders();
    isRunning = true;
  }

  /** Calculate and clamp controller output to max speed. */
  @Override
  public void execute() {
    sDrivetrain.moveStraight(Math.max(-DrivetrainConstants.kDistMaxSpeed, Math.min(
      m_controller.calculate(sDrivetrain.getPosition(), distance),
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