package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DeviceConstants;

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

  // Wheel speeds
  private static double
    intakeSpeed = 0.3,
    shootSpeed = -1,
    spitSpeed = -0.35,
    holdSpeed = 0.05;

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
      () -> {if (m_holding && isInverted()) {m_motor.set(holdSpeed);}},
      IntakeWheels::stop
    ));
  }

  public double getSpeed() {return m_motor.get();}

  /** Invert intake speeds for cones. */
  public static synchronized void invert() {
    getInstance().getDefaultCommand().cancel();
    if (m_motor.get() * intakeSpeed > 0) {
      // If intaking, invert the speed of the wheels.
      m_motor.set(-m_motor.get());
    }

    intakeSpeed *= -1;
    shootSpeed *= -1;
    spitSpeed *= -1;
    holdSpeed *= -1;
  }

  public static boolean isInverted() {
    return intakeSpeed < 0;
  }

  /** Invert to cubes. */
  public static void toCube() {if (isInverted()) {invert();}}

  /** Invert to cones. */
  public static void toCone() {if (!isInverted()) {invert();}}

  public static void stop() {
    m_motor.stopMotor();
  }

  /** @return a command to intake a game piece */
  public Command getIntakeCommand() {
    return startEnd(
      () -> {
        m_motor.set(intakeSpeed);
        m_holding = true;
      },
      IntakeWheels::stop
    );
  }

  /** @return a command to shoot a game piece at full speed */
  public Command getShootCommand() {
    return startEnd(
      () -> {
        m_motor.set(shootSpeed);
        m_holding = false;
      },
      IntakeWheels::stop
    );
  }

  /** @return a command to spit a game piece at partial speed */
  public Command getSpitCommand() {
    return startEnd(
      () -> {
        m_motor.set(spitSpeed);
        m_holding = false;
      },
      IntakeWheels::stop
    );
  }
}