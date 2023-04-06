// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import frc.robot.OI;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.DeviceConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import edu.wpi.first.wpilibj2.command.RunCommand;

public class Drivetrain extends SubsystemBase {
  // Initialize motors, encoders, and differential drive
  private static final CANSparkMax
    fl_motor = new CANSparkMax(DeviceConstants.kFrontLeftID, MotorType.kBrushless),
    bl_motor = new CANSparkMax(DeviceConstants.kBackLeftID, MotorType.kBrushless),
    fr_motor = new CANSparkMax(DeviceConstants.kFrontRightID, MotorType.kBrushless),
    br_motor = new CANSparkMax(DeviceConstants.kBackRightID, MotorType.kBrushless);

  private static final RelativeEncoder l_encoder = fl_motor.getEncoder();
  private static final RelativeEncoder r_encoder = fr_motor.getEncoder(); // Position must be inverted when called

  private static final MotorControllerGroup l_motors = new MotorControllerGroup(fl_motor, bl_motor);
  private static final MotorControllerGroup r_motors = new MotorControllerGroup(fr_motor, br_motor);

  // Inverts the right motors for properly functioning sendable
  public static final DifferentialDrive robotDrive = new DifferentialDrive(l_motors, r_motors) {
    @Override
    public void initSendable(SendableBuilder builder) {
      builder.setSmartDashboardType("DifferentialDrive");
      builder.setActuator(true);
      builder.setSafeState(this::stopMotor);
      builder.addDoubleProperty("Left Motor Speed", l_motors::get, l_motors::set);
      builder.addDoubleProperty("Right Motor Speed", () -> -r_motors.get(), x -> r_motors.set(-x));
    }
  };

  public Drivetrain() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new RunCommand(
      () -> {
        if (Math.abs(OI.driver_cntlr.getTriggers()) > 0.05) {
          // Turn in place, input from trigger
          turnInPlace(DrivetrainConstants.kTurnMult * MathUtil.applyDeadband(OI.driver_cntlr.getTriggers(), 0.05));
        } else {
          // Regular drive, input from left stick
          robotDrive.arcadeDrive(DrivetrainConstants.kTurnMult * OI.driver_cntlr.getLeftX(), DrivetrainConstants.kSpeedMult * OI.driver_cntlr.getLeftY(), true);
        }
      },
      this
    ));

    SendableRegistry.setSubsystem(robotDrive, getSubsystem());

    // Sets encoders to measure in inches
    l_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox);
    l_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60);
    r_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox);
    r_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60);

    // Sets encoder measurement period in time with default loop
    l_encoder.setMeasurementPeriod(20);
    r_encoder.setMeasurementPeriod(20);

    l_encoder.setPosition(0);
    r_encoder.setPosition(0);
  }

  public void turnInPlace(double rotationSpeed) {
    robotDrive.tankDrive(rotationSpeed, rotationSpeed, false);
  }

  public void moveStraight(double speed) {
    robotDrive.tankDrive(speed, -speed, false);
  }

  // Returns the average of the position of the encoders (in inches)
  public double getAvgPosition() {
    return (l_encoder.getPosition() - r_encoder.getPosition())/2;
  }

  public void resetEncoders() {
    l_encoder.setPosition(0);
    r_encoder.setPosition(0);
  }

  // Stops drivetrain motors
  public void stop() {
    robotDrive.stopMotor();
  }
}