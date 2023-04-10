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

public class IntakeWheels extends SubsystemBase {
  public static boolean m_holding;

  private static final CANSparkMax intake_motor = new CANSparkMax(DeviceConstants.kIntakeWheelsID, MotorType.kBrushless);

  private static final RelativeEncoder intake_encoder = intake_motor.getEncoder();

  public IntakeWheels() {
    setDefaultCommand(new StartEndCommand(
      () -> {if (m_holding && IntakeConstants.kIntakeSpeed < 0) {intake_motor.set(IntakeConstants.kHoldingSpeed);}},
      this::stop,
      this
    ));

    intake_encoder.setPositionConversionFactor(PhysConstants.kTiltGearbox);
    intake_encoder.setVelocityConversionFactor(PhysConstants.kTiltGearbox);
    intake_encoder.setMeasurementPeriod(20);
    intake_encoder.setPosition(0);
  }

  public void set(double speed) {intake_motor.set(speed);}
  public double get() {return intake_motor.get();}

  public double getVelocity() {return intake_encoder.getVelocity();}

  public void invert() {
    IntakeConstants.kIntakeSpeed *= -1;
    IntakeConstants.kOuttakeSpeed *= -1;
    IntakeConstants.kSpitSpeed *= -1;
    IntakeConstants.kHoldingSpeed *= -1;
  }

  // Stops all motors
  public void stop() {
    intake_motor.stopMotor();
  }

  public Command getIntakeCommand() {
    return startEnd(
      () -> {
        intake_motor.set(IntakeConstants.kIntakeSpeed);
        m_holding = true;
      },
      this::stop
    );
  }

  public Command getShootCommand() {
    return startEnd(
      () -> {
        intake_motor.set(IntakeConstants.kOuttakeSpeed);
        m_holding = false;
      },
      this::stop
    );
  }

  public Command getSpitCommand() {
    return startEnd(
      () -> {
        intake_motor.set(IntakeConstants.kSpitSpeed);
        m_holding = false;
      },
      this::stop
    );
  }
}