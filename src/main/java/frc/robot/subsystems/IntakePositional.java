// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

public class IntakePositional extends PIDSubsystem {
  public final CANSparkMax positionalMotor = new CANSparkMax(DeviceConstants.kIntakePositionCANid, MotorType.kBrushless);

  public final RelativeEncoder positionalEncoder = positionalMotor.getEncoder();
  
  public IntakePositional() {
    super(new PIDController(IntakeConstants.kP, IntakeConstants.kI, IntakeConstants.kD));
    disable();
    positionalEncoder.setPositionConversionFactor(IntakeConstants.kPositionalGearbox);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    System.out.println(getMeasurement());
    positionalMotor.set(output);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return positionalEncoder.getPosition();
  }

  public void stop() {
    positionalMotor.stopMotor();
  }
}