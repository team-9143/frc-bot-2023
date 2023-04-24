package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.FunctionalCommand;

/** Controls intake tilt motors. */
public class IntakeTilt extends SubsystemBase {
  private static IntakeTilt m_instance;

  /** @return the singleton instance */
  public static synchronized IntakeTilt getInstance() {
    if (m_instance == null) {
      m_instance = new IntakeTilt();
    }
    return m_instance;
  }

  public static final PIDController m_controller = new PIDController(IntakeConstants.kSteadyP, IntakeConstants.kSteadyI, IntakeConstants.kSteadyD);
  private static double m_setpoint = IntakeConstants.kUpPos;
  private static boolean m_steadyEnabled = false;
  private static boolean m_running = false;

  private static final CANSparkMax l_motor = new CANSparkMax(DeviceConstants.kIntakeTiltLeftID, MotorType.kBrushless);
  private static final CANSparkMax r_motor = new CANSparkMax(DeviceConstants.kIntakeTiltRightID, MotorType.kBrushless);

  private static final RelativeEncoder l_encoder = l_motor.getEncoder();
  private static final RelativeEncoder r_encoder = r_motor.getEncoder();

  private IntakeTilt() {
    // IMPORTANT: Ensure that motors have an equivalent setpoint
    r_motor.follow(l_motor, true);

    l_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox * 360); // UNIT: degrees
    l_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox * (360 / 60)); // UNIT: degrees/s
    l_encoder.setMeasurementPeriod(20);
    l_encoder.setPosition(0);

    r_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox * 360); // UNIT: degrees
    r_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox * (360 / 60)); // UNIT: degrees/s
    r_encoder.setMeasurementPeriod(20);
    r_encoder.setPosition(0);

    // If enabled, keep intake in a steady state at up position
    setDefaultCommand(new FunctionalCommand(
      () -> {
        m_controller.reset();
        m_setpoint = IntakeConstants.kUpPos;
      },
      () -> {if (m_steadyEnabled) {set(m_controller.calculate(getPosition()));}},
      interrupted -> {},
      () -> false,
      this
    ));
  }

  /** Clamps input to max speed. */
  public void set(double speed) {
    l_motor.set(Math.max(-IntakeConstants.kTiltMaxSpeed, Math.min(speed, IntakeConstants.kTiltMaxSpeed)));
  }
  public double get() {return l_motor.get();}

  /** @return the average position of the tilt encoders */
  public double getPosition() {
    return (l_encoder.getPosition() + r_encoder.getPosition())/2;

    // For simulation
    // if (frc.robot.shuffleboard.SimulationTab.intakeAngle_sim == null) {
    //   return IntakeConstants.kUpPos * 360;
    // } else {
    //   return frc.robot.shuffleboard.SimulationTab.intakeAngle_sim.getDouble(IntakeConstants.kUpPos * 360) / 360;
    // }
  }

  public void resetEncoders() {
    l_encoder.setPosition(IntakeConstants.kUpPos);
    r_encoder.setPosition(IntakeConstants.kUpPos);
  }

  /** Enables and resets steady intake PID. */
  public static void enableSteady() {
    m_steadyEnabled = true;
    m_controller.reset();
  }

  /** Disables steady intake PID and stops motors. */
  public static void disableSteady() {
    m_steadyEnabled = false;
    stop();
  }

  /** @return {@code true} if currently trying to stay upright */
  public static boolean isSteadyEnabled() {return m_steadyEnabled;}

  /** @param b {@code true} if tilt motor is being moved by a command */
  public static void setRunning(boolean b) {m_running = b;}
  /** @return {@code true} if tilt motor is being moved by a command */
  public static boolean isRunning() {return m_running;}

  /**
   * Sets the setpoint of the intake for the dashboard.
   * 
   * @param setpoint new setpoint (UNIT: degrees)
   */
  public static void setSetpoint(double setpoint) {m_setpoint = setpoint;}
  public static double getSetpoint() {return m_setpoint;}

  public static void stop() {
    l_motor.stopMotor();
  }

  // TODO(auto align): Test and implement autoAlign
  public void getAutoAlignCommand() {
    new FunctionalCommand(
      () -> set(IntakeConstants.kAutoAlignSpeed),
      () -> {},
      interrupted -> {
        // TODO(auto align): Test and tune up position offset (currently 9 degrees back)
        l_encoder.setPosition(IntakeConstants.kUpPos - 0.025);
        r_encoder.setPosition(IntakeConstants.kUpPos - 0.025);
      },
      () -> (l_motor.getOutputCurrent() > IntakeConstants.kMaxCurrent) || (r_motor.getOutputCurrent() > IntakeConstants.kMaxCurrent),
      m_instance
    );
  }
}