// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

public class IntakeTilt extends PIDSubsystem {
  private final CANSparkMax tilt_motor = new CANSparkMax(DeviceConstants.kIntakeTiltID, MotorType.kBrushless);

  private final RelativeEncoder tilt_encoder = tilt_motor.getEncoder();

  public IntakeTilt() {
    super(new PIDController(IntakeConstants.kP, IntakeConstants.kI, IntakeConstants.kD));
    tilt_encoder.setPositionConversionFactor(IntakeConstants.kTiltGearbox);
    tilt_encoder.setVelocityConversionFactor(IntakeConstants.kTiltGearbox);
    tilt_encoder.setMeasurementPeriod(20);
    tilt_encoder.setPosition(0);
    disable();

    // Set default target
    setSetpoint(IntakeConstants.kUpPos);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    tilt_motor.set(output);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return tilt_encoder.getPosition();
  }

  // Disables PID control, stopping calculation and motors, and resets to default target. Movement will only restart when re-enabled.
  public void stop() {
    disable();
    setSetpoint(IntakeConstants.kUpPos);
  }
}