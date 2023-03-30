// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
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
    
    intake_encoder.setPositionConversionFactor(IntakeConstants.kTiltGearbox);
    intake_encoder.setVelocityConversionFactor(IntakeConstants.kTiltGearbox);
    intake_encoder.setMeasurementPeriod(20);
    intake_encoder.setPosition(0);
  }

  public void set(double speed) {intake_motor.set(speed);}

  public double getVelocity() {return intake_encoder.getVelocity();}

  // Stops all motors
  public void stop() {
    intake_motor.stopMotor();
  }

  // Outtake command
  public Command getOuttakeCommand() {
    return startEnd(
      () -> {
        intake_motor.set(IntakeConstants.kOuttakeSpeed);
        m_holding = false;
      },
      this::stop
    );
  }
}