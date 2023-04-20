package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.StartEndCommand;

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

  /** Used to apply tension if a game piece is being held in the intake. */
  public static boolean m_holding;

  private static final CANSparkMax intake_motor = new CANSparkMax(DeviceConstants.kIntakeWheelsID, MotorType.kBrushless);

  private static final RelativeEncoder intake_encoder = intake_motor.getEncoder();

  private IntakeWheels() {
    intake_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox);
    intake_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox);
    intake_encoder.setMeasurementPeriod(20);
    intake_encoder.setPosition(0);

    // If inverted and has a game piece, apply tension to hold in a cone
    setDefaultCommand(startEnd(
      () -> {if (m_holding && isInverted()) {set(IntakeConstants.kHoldingSpeed);}},
      IntakeWheels::stop
    ));
  }

  public void set(double speed) {intake_motor.set(speed);}
  public double get() {return intake_motor.get();}

  public double getVelocity() {return intake_encoder.getVelocity();}

  /** Invert intake speeds for cones. */
  public static synchronized void invert() {
    IntakeConstants.kIntakeSpeed *= -1;
    IntakeConstants.kShootSpeed *= -1;
    IntakeConstants.kSpitSpeed *= -1;
    IntakeConstants.kHoldingSpeed *= -1;
  }

  public static boolean isInverted() {
    return IntakeConstants.kIntakeSpeed < 0;
  }

  /** Invert to cubes. */
  public static void toCube() {if (isInverted()) {invert();}}

  /** Invert to cones. */
  public static void toCone() {if (!isInverted()) {invert();}}

  public static void stop() {
    intake_motor.stopMotor();
  }

  /** @return a command to intake a game piece */
  public Command getIntakeCommand() {
    return new StartEndCommand(
      () -> {
        set(IntakeConstants.kIntakeSpeed);
        m_holding = true;
      },
      IntakeWheels::stop,
      m_instance
    );
  }

  /** @return a command to shoot a game piece at full speed */
  public Command getShootCommand() {
    return new StartEndCommand(
      () -> {
        set(IntakeConstants.kShootSpeed);
        m_holding = false;
      },
      IntakeWheels::stop,
      m_instance
    );
  }

  /** @return a command to spit a game piece at partial speed */
  public Command getSpitCommand() {
    return new StartEndCommand(
      () -> {
        set(IntakeConstants.kSpitSpeed);
        m_holding = false;
      },
      IntakeWheels::stop,
      m_instance
    );
  }
}