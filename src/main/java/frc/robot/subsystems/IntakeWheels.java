// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DeviceConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeWheels extends SubsystemBase {
  private final static CANSparkMax intake_motor = new CANSparkMax(DeviceConstants.kIntakeWheelsID, MotorType.kBrushless);

  public static RelativeEncoder getEncoder(){return intake_motor.getEncoder();}
  public void set(double speed) {intake_motor.set(speed);}

  // Stops all motors
  public void stop() {
    intake_motor.stopMotor();
  }

  // Outtake command
  public Command getOuttakeCommand() {
    return startEnd(
      () -> intake_motor.set(IntakeConstants.kOuttakeSpeed),
      this::stop
    );
  }
}