// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.commands.TurnToAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeWheels;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
  private Drivetrain sDrivetrain;
  ShuffleboardTab driveTab;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    driveTab = Shuffleboard.getTab("Drive");
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    driveTab.getLayout("Encoders", BuiltInLayouts.kGrid)
      .withPosition(2, 1);
    driveTab.getLayout("Encoders")
      .addNumber("Left Encoder", sDrivetrain.getEncoder()[0]::getPosition);
    driveTab.getLayout("Encoders")
      .addNumber("Right Encoder", sDrivetrain.getEncoder()[1]::getPosition);

    driveTab.getLayout("", BuiltInLayouts.kList)
      .withPosition(1, 0);
    driveTab.getLayout("")
      .addBoolean("TurnToAngle", new TurnToAngle(sDrivetrain)::isScheduled); 
    driveTab.getLayout("")
      .addNumber("Gyro Rotation", OI.pigeon::getYaw)
        .withWidget(BuiltInWidgets.kGyro);
    driveTab.getLayout("")
      .addDouble("Heading", TurnToAngle::getHeading);

    driveTab.addDouble("Docking", OI.pigeon::getPitch)
      .withWidget(BuiltInWidgets.kGyro)
        .withPosition(2, 0);

    driveTab.getLayout("Limelight", BuiltInLayouts.kList)
      .withPosition(2, 2);
    driveTab.getLayout("Limelight")
      .addNumber("TA", new Limelight()::getArea);
    driveTab.getLayout("Limelight")
      .addNumber("TX", new Limelight()::getTx);
    driveTab.getLayout("Limelight")
      .addNumber("TY", new Limelight()::getTy);
    driveTab.getLayout("Limelight")
      .addBoolean("Is Valid", new Limelight()::getValid);
    
    driveTab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(3, 0);
    driveTab.getLayout("Intake")
      .addBoolean("Intake On", () -> IntakeWheels.getEncoder().getVelocity() > 0);
    driveTab.getLayout("Intake")
      .addDouble("Intake RPM", IntakeWheels.getEncoder()::getVelocity);

    driveTab.getLayout("Motor Rpm", BuiltInLayouts.kList)
      .withPosition(4, 0);
    driveTab.getLayout("Motor RPM")
      .addDouble("Left Motor", sDrivetrain.getEncoder()[0]::getVelocity);
    driveTab.getLayout("Motor RPM")
      .addDouble("Right Motor", sDrivetrain.getEncoder()[1]::getVelocity);

  }


  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    m_robotContainer.stop();
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}


  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    TurnToAngle.m_enabled = false;
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}