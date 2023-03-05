// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants.DeviceConstants;

public class IntakePositional extends SubsystemBase {
  public final CANSparkMax positionMotor = new CANSparkMax(DeviceConstants.kIntakePositionCANid, MotorType.kBrushless);

  public final RelativeEncoder positionEncoder = positionMotor.getEncoder();

  // Stops all motors
  public void stop() {
    positionMotor.stopMotor();
  }
}