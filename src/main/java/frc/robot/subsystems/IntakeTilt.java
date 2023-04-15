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
  private static boolean m_steadyEnabled = false;
  public static double m_setpoint = IntakeConstants.kUpPos;
  private static boolean isRunning = false;

  private static final CANSparkMax l_motor = new CANSparkMax(DeviceConstants.kIntakeTiltLeftID, MotorType.kBrushless);
  private static final CANSparkMax r_motor = new CANSparkMax(DeviceConstants.kIntakeTiltRightID, MotorType.kBrushless);

  private static final RelativeEncoder l_encoder = l_motor.getEncoder();
  private static final RelativeEncoder r_encoder = r_motor.getEncoder();

  private IntakeTilt() {
    // IMPORTANT: Ensure that motors have a consistent output
    r_motor.follow(l_motor, true);

    l_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox);
    l_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox);
    l_encoder.setMeasurementPeriod(20);
    l_encoder.setPosition(0);

    r_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox);
    r_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox);
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
    // return (l_encoder.getPosition() + r_encoder.getPosition())/2;

    // For simulation
    if (frc.robot.shuffleboard.SimulationTab.intakeAngle_sim == null) {
      return IntakeConstants.kUpPos * 360;
    } else {
      return frc.robot.shuffleboard.SimulationTab.intakeAngle_sim.getDouble(IntakeConstants.kUpPos * 360) / 360;
    }
  }

  public void resetEncoders() {
    l_encoder.setPosition(IntakeConstants.kUpPos);
    r_encoder.setPosition(IntakeConstants.kUpPos);
  }

  /** Enables and resets steady intake PID. */
  public static void enable() {
    m_steadyEnabled = true;
    m_controller.reset();
  }

  /** Disables steady intake PID and stops motors. */
  public static void disable() {
    m_steadyEnabled = false;
    stop();
  }

  public static boolean isSteadyEnabled() {return m_steadyEnabled;}

  public static void setRunning(boolean b) {isRunning = b;}
  public static boolean isRunning() {return isRunning;}

  public static void stop() {
    l_motor.stopMotor();
  }

  // TODO(low prio): Test and implement autoAlign
  public void getAutoAlignCommand() {
    new FunctionalCommand(
      () -> set(IntakeConstants.kAutoAlignSpeed),
      () -> {},
      interrupted -> {
        // TODO(autoAlign): Test and tune up position offset (currently 9 degrees back)
        l_encoder.setPosition(IntakeConstants.kUpPos - 0.025);
        r_encoder.setPosition(IntakeConstants.kUpPos - 0.025);
      },
      () -> (l_motor.getOutputCurrent() > IntakeConstants.kMaxCurrent) || (r_motor.getOutputCurrent() > IntakeConstants.kMaxCurrent),
      m_instance
    );
  }
}