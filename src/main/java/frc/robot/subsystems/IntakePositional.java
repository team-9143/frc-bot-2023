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

public class IntakePositional extends PIDSubsystem {
  private final CANSparkMax positional_motor = new CANSparkMax(DeviceConstants.kIntakePositionalCANid, MotorType.kBrushless);

  private final RelativeEncoder positional_encoder = positional_motor.getEncoder();

  public IntakePositional() {
    super(new PIDController(IntakeConstants.kP, IntakeConstants.kI, IntakeConstants.kD));
    positional_encoder.setPositionConversionFactor(IntakeConstants.kPositionalGearbox);
    positional_encoder.setVelocityConversionFactor(IntakeConstants.kPositionalGearbox);
    disable();
    
    // Set default target
    setSetpoint(IntakeConstants.kUpPos);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    positional_motor.set(output);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return positional_encoder.getPosition();
  }

  // Disables PID control, stopping calculation and motors, and resets to default target
  public void stop() {
    disable();
    setSetpoint(IntakeConstants.kUpPos);
  }
}