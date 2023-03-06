// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.DeviceConstants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Drive;

public class Drivetrain extends SubsystemBase {
  // Initialize motors, encoders, and differential drive
  private static final CANSparkMax
    fl_motor = new CANSparkMax(DeviceConstants.kFrontLeftCANid, MotorType.kBrushless),
    bl_motor = new CANSparkMax(DeviceConstants.kBackLeftCANid, MotorType.kBrushless),
    fr_motor = new CANSparkMax(DeviceConstants.kFrontRightCANid, MotorType.kBrushless),
    br_motor = new CANSparkMax(DeviceConstants.kBackRightCANid, MotorType.kBrushless);

  public final RelativeEncoder[] encoders = {
    fl_motor.getEncoder(),
    bl_motor.getEncoder(),
    fr_motor.getEncoder(),
    br_motor.getEncoder()
  };

  public final DifferentialDrive robotDrive = new DifferentialDrive(
    new MotorControllerGroup(fl_motor, bl_motor),
    new MotorControllerGroup(fr_motor, br_motor)
  );

  public Drivetrain() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive(this));

    for (RelativeEncoder encoder : encoders) {
      // Sets encoders to measure position in feet
      encoder.setPositionConversionFactor(Math.PI * DrivetrainConstants.kWheelDiameter / DrivetrainConstants.kGearboxRatio);
    }
  }

  // Stops drivetrain motors and cancels current command
  public void stop() {
    robotDrive.stopMotor();
    getCurrentCommand().cancel();
  }
}