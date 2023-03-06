// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DeviceConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeWheels extends SubsystemBase {
  public final CANSparkMax intakeMotor = new CANSparkMax(DeviceConstants.kIntakeCANid, MotorType.kBrushless);

  // Stops all motors
  public void stop() {
    intakeMotor.stopMotor();
  }
}