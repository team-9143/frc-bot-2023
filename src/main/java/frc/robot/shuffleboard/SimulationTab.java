package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardTabBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.networktables.GenericEntry;
import java.util.Map;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeTilt;
import frc.robot.subsystems.IntakeWheels;
import frc.robot.autos.AutoSelector;
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.DriveDistance;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import frc.robot.Constants.IntakeConstants;

// TODO: Copy data over to test tab, make 16x7
/** Contains auton selector and data for driver and operator. */
public class SimulationTab implements ShuffleboardTabBase {
  public static GenericEntry yaw_sim;
  public static GenericEntry drivetrainPos_sim;
  public static GenericEntry intakeAngle_sim;
  public static GenericEntry pitch_sim;

  protected SimulationTab() {}

  public void initialize() {
    final ShuffleboardTab sim_tab = Shuffleboard.getTab("Simulation");

    final Drivetrain sDrivetrain = Drivetrain.getInstance();
    final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();

    sim_tab.add("Auton Starter", AutoSelector.m_starterChooser)
      .withPosition(0, 0)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    sim_tab.add("Auton Body", AutoSelector.m_bodyChooser)
      .withPosition(3, 0)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    sim_tab.add("Auton Secondary", AutoSelector.m_secondaryChooser)
      .withPosition(6, 0)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    sim_tab.add("Auton Tertiary", AutoSelector.m_tertiaryChooser)
      .withPosition(9, 0)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    sim_tab.add("Auton Ending", AutoSelector.m_endingChooser)
      .withPosition(12, 0)
      .withSize(2, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    ShuffleboardLayout layout_1 = sim_tab.getLayout("TurnToAngle", BuiltInLayouts.kList)
      .withPosition(0, 2)
      .withSize(3, 8);
    layout_1.addDouble("Setpoint", TurnToAngle.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));
    yaw_sim = layout_1.add("Gyro", 0)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -180, "max", 180, "block increment", 1))
      .getEntry();
    layout_1.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(IntakeTilt::stop);
        builder.addDoubleProperty("Value",
          () -> (TurnToAngle.isRunning()) ? sDrivetrain.getLeft() : 0,
          null
        );
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
    layout_1.addBoolean("Running", TurnToAngle::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_2 = sim_tab.getLayout("DriveDistance", BuiltInLayouts.kList)
      .withPosition(3, 2)
      .withSize(3, 8);
    layout_2.addDouble("Setpoint", DriveDistance.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -225, "max", 225, "center", 0));
    drivetrainPos_sim = layout_2.add("Position", 0)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -225, "max", 225, "block increment", 2))
      .getEntry();
    layout_2.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(IntakeTilt::stop);
        builder.addDoubleProperty("Value",
          () -> (DriveDistance.isRunning()) ? sDrivetrain.getLeft() : 0,
          null
        );
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
    layout_2.addBoolean("Running", DriveDistance::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_3 = sim_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(6, 2)
      .withSize(3, 8);
    layout_3.addDouble("Setpoint", IntakeTilt::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));
    intakeAngle_sim = layout_3.add("Angle", IntakeConstants.kUpPos)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -110, "max", 110, "block increment", 1))
      .getEntry();
    layout_3.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(IntakeTilt::stop);
        builder.addDoubleProperty("Value", sIntakeTilt::get, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
    layout_3.addBoolean("Running", IntakeTilt::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_4 = sim_tab.getLayout("Intake Wheels", BuiltInLayouts.kList)
      .withPosition(9, 2)
      .withSize(3, 8);
    layout_4.addBoolean("Inverted", IntakeWheels::isInverted)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.addBoolean("Intaking", () -> (Math.signum(IntakeWheels.get()) == (IntakeWheels.isInverted() ? -1.0 : 1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.addBoolean("Shooting", () -> (Math.signum(IntakeWheels.get()) == (IntakeWheels.isInverted() ? 1.0 : -1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(IntakeWheels::stop);
        builder.addDoubleProperty("Value", IntakeWheels::get, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    pitch_sim = sim_tab.add("Docking Angle", 0)
      .withPosition(14, 0)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -45, "max", 45, "block increment", 5))
      .getEntry();

    sim_tab.add("Drivetrain", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.setActuator(true);
        builder.setSafeState(Drivetrain::stop);
        builder.addDoubleProperty("Left Motor Speed", sDrivetrain::getLeft, null);
        builder.addDoubleProperty("Right Motor Speed", () -> -sDrivetrain.getRight(), null);
      }
    }).withPosition(12, 2)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));
  }
}