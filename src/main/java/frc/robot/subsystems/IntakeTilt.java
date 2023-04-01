// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.FunctionalCommand;

public class IntakeTilt extends PIDSubsystem {
  private static final CANSparkMax l_motor = new CANSparkMax(DeviceConstants.kIntakeTiltLeftID, MotorType.kBrushless);
  private static final CANSparkMax r_motor = new CANSparkMax(DeviceConstants.kIntakeTiltRightID, MotorType.kBrushless);

  private static final RelativeEncoder l_encoder = l_motor.getEncoder();
  private static final RelativeEncoder r_encoder = r_motor.getEncoder();

  public IntakeTilt() {
    super(new PIDController(IntakeConstants.kSteadyP, IntakeConstants.kSteadyI, IntakeConstants.kSteadyD));

    r_motor.follow(l_motor, true);

    l_encoder.setPositionConversionFactor(IntakeConstants.kTiltGearbox);
    l_encoder.setVelocityConversionFactor(IntakeConstants.kTiltGearbox);
    l_encoder.setMeasurementPeriod(20);
    l_encoder.setPosition(0);

    r_encoder.setPositionConversionFactor(IntakeConstants.kTiltGearbox);
    r_encoder.setVelocityConversionFactor(IntakeConstants.kTiltGearbox);
    r_encoder.setMeasurementPeriod(20);
    r_encoder.setPosition(0);

    setSetpoint(IntakeConstants.kUpPos);

    enable();
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    l_motor.set(Math.max(-IntakeConstants.kTiltMaxSpeed, Math.min(output, IntakeConstants.kTiltMaxSpeed)));
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return (l_encoder.getPosition() + r_encoder.getPosition())/2;
  }

  public void resetEncoder() {
    l_encoder.setPosition(IntakeConstants.kUpPos);
    r_encoder.setPosition(IntakeConstants.kUpPos);
  }

  public void autoAlign() {
    new FunctionalCommand(
      () -> {},
      () -> useOutput(-0.25, IntakeConstants.kUpPos),
      interrupted -> {},
      () -> l_motor.getBusVoltage() > IntakeConstants.kMaxVoltage || r_motor.getBusVoltage() > IntakeConstants.kMaxVoltage,
      this
    )
    .schedule();
  }

  public Command getAimDownCommand() {
    return startEnd(
      () -> {
        disable();
        useOutput(IntakeConstants.kDownSpeed, 0);
      },
      this::disable
    )
    .withTimeout(IntakeConstants.kAimDownTimer);
  }
}