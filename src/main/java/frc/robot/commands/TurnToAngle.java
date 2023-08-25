package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.OI;
import frc.robot.Constants.DrivetrainConstants;

import java.util.Set;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

/** Turns to a given angle. */
public class TurnToAngle extends CommandBase {
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final Set<Subsystem> m_requirements = Set.of(sDrivetrain);
  private static boolean isRunning = false;

  public static final PIDController m_controller = new PIDController(DrivetrainConstants.kTurnP, DrivetrainConstants.kTurnI, DrivetrainConstants.kTurnD);
  static {
    m_controller.setIntegratorRange(-DrivetrainConstants.kTurnMaxSpeed, DrivetrainConstants.kTurnMaxSpeed);
    m_controller.setTolerance(DrivetrainConstants.kTurnPosTolerance, DrivetrainConstants.kTurnVelTolerance);
    m_controller.setSetpoint(0);
    m_controller.enableContinuousInput(-180, 180);
  }

  /** If TurnToAngle can be used during teleop. */
  public static boolean m_enabled = false;
  private double heading;

  private TurnToAngle() {}
  // public TurnToAngle(double heading) {
  //   this.heading = heading;
  // }

  @Override
  public void initialize() {
    m_controller.reset();
    isRunning = true;
  }

  /** Calculate and clamp controller output to max speed. */
  @Override
  public void execute() {
    sDrivetrain.turnInPlace(Math.max(-DrivetrainConstants.kTurnMaxSpeed, Math.min(
      m_controller.calculate(-OI.pigeon.getYaw(), heading),
    DrivetrainConstants.kTurnMaxSpeed)));
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

  public void setHeading(double heading) {this.heading = heading;}

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }

  public static boolean isRunning() {return isRunning;}
}