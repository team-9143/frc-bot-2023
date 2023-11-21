package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

/** Controls intake wheels. */
public class IntakeWheels extends SubsystemBase {
  private static IntakeWheels m_instance;

  /** @return the singleton instance */
  public static synchronized IntakeWheels getInstance() {
    if (m_instance == null) {
      m_instance = new IntakeWheels();
    }
    return m_instance;
  }

  /** {@code true} to invert wheel speeds for cones */
  private static boolean m_inverted = false;

  /** Used to apply tension if a game piece is being held in the intake. */
  private static boolean m_holding = false;

  private static final CANSparkMax m_motor = new CANSparkMax(DeviceConstants.kIntakeWheelsID, MotorType.kBrushless);

  private static final RelativeEncoder m_encoder = m_motor.getEncoder();

  private IntakeWheels() {
    m_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox); // UNIT: rotations
    m_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox); // UNIT: rpm
    m_encoder.setMeasurementPeriod(20);
    m_encoder.setPosition(0);

    // If inverted and has a game piece, apply tension to hold in a cone
    setDefaultCommand(startEnd(
      () -> {if (m_holding && m_inverted) {m_motor.set(IntakeConstants.kHoldSpeed);}},
      IntakeWheels::stop
    ));
  }

  public double getSpeed() {return m_motor.get();}

  /** Invert intake speeds for cones. */
  public static synchronized void invert() {
    getInstance().getDefaultCommand().cancel();
    if ((m_inverted ^ m_motor.get() > 0)) {
      // If intaking, invert the speed of the wheels.
      m_motor.set(-m_motor.get());
    }

    m_inverted ^= true;
  }

  /** @return {@code true} if the wheel speeds are inverted for cones */
  public static boolean isCone() {
    return m_inverted;
  }

  /** Invert to cubes. */
  public static void toCube() {if (isCone()) {invert();}}

  /** Invert to cones. */
  public static void toCone() {if (!isCone()) {invert();}}

  public static void stop() {
    m_motor.stopMotor();
  }

  /** @return a command to intake a game piece */
  public Command getIntakeCommand() {
    return startEnd(
      () -> {
        m_motor.set(m_inverted ? -IntakeConstants.kIntakeSpeed : IntakeConstants.kIntakeSpeed);
        m_holding = true;
      },
      IntakeWheels::stop
    );
  }

  /** @return a command to shoot a game piece at full speed */
  public Command getShootCommand() {
    return startEnd(
      () -> {
        m_motor.set(m_inverted ? -IntakeConstants.kShootSpeed : IntakeConstants.kShootSpeed);
        m_holding = false;
      },
      IntakeWheels::stop
    );
  }

  /** @return a command to spit a game piece at partial speed */
  public Command getSpitCommand() {
    return startEnd(
      () -> {
        m_motor.set(m_inverted ? -IntakeConstants.kSpitSpeed : IntakeConstants.kSpitSpeed);
        m_holding = false;
      },
      IntakeWheels::stop
    );
  }
}