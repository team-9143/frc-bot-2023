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
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.DriveDistance;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import frc.robot.Constants.IntakeConstants;

// TODO: Copy data over to test tab, make 16x7
/** Contains auton selector and data for driver and operator. */
public class SimulationTab implements ShuffleboardTabBase {
  final ShuffleboardTab sim_tab = Shuffleboard.getTab("Simulation");

  public static GenericEntry yaw_sim;
  public static GenericEntry drivetrainPos_sim;
  public static GenericEntry intakeAngle_sim;
  public static GenericEntry pitch_sim;

  protected SimulationTab() {}

  public void initialize() {
    initLayout1();
    initLayout2();
    initLayout3();
    initLayout4();

    sim_tab.add("Drivetrain", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.setActuator(true);
        builder.setSafeState(Drivetrain::stop);
        builder.addDoubleProperty("Left Motor Speed", Drivetrain.getInstance()::getLeft, null);
        builder.addDoubleProperty("Right Motor Speed", () -> -Drivetrain.getInstance().getRight(), null);
      }
    }).withPosition(12, 0)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));

    pitch_sim = sim_tab.add("Docking Angle", 0)
      .withPosition(12, 4)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -45, "max", 45, "block increment", 2))
      .getEntry();
  }

  private void initLayout1() {
    ShuffleboardLayout layout_1 = sim_tab.getLayout("TurnToAngle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(3, 7);

    layout_1.addDouble("Setpoint", TurnToAngle.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));

    yaw_sim = layout_1.add("Gyro", 0)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -180, "max", 180, "block increment", 2))
      .getEntry();

    layout_1.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", () -> (TurnToAngle.isRunning()) ? Drivetrain.getInstance().getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_1.addBoolean("Running", TurnToAngle::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  private void initLayout2() {
    ShuffleboardLayout layout_2 = sim_tab.getLayout("DriveDistance", BuiltInLayouts.kList)
      .withPosition(3, 0)
      .withSize(3, 7);

    layout_2.addDouble("Setpoint", DriveDistance.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -225, "max", 225, "center", 0));

    if (ShuffleboardManager.m_simulatedDrive) {
      drivetrainPos_sim = layout_2.add("Position", 0)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", -225, "max", 225, "block increment", 3))
        .getEntry();
    } else {
      layout_2.addDouble("Position", Drivetrain.getInstance()::getPosition)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -225, "max", 225, "center", 0));
    }

    layout_2.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", () -> (DriveDistance.isRunning()) ? Drivetrain.getInstance().getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_2.addBoolean("Running", DriveDistance::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  private void initLayout3() {
    ShuffleboardLayout layout_3 = sim_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(6, 0)
      .withSize(3, 7);

    layout_3.addDouble("Setpoint", IntakeTilt::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    if (ShuffleboardManager.m_simulatedTilt) {
      intakeAngle_sim = layout_3.add("Angle", IntakeConstants.kUpPos)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", -110, "max", 110, "block increment", 4))
        .getEntry();
    } else {
      layout_3.addDouble("Angle", IntakeTilt.getInstance()::getPosition)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -110, "max", 110, "center", 0));
    }

    layout_3.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", IntakeTilt.getInstance()::get, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_3.addBoolean("Running", IntakeTilt::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  private void initLayout4() {
    ShuffleboardLayout layout_4 = sim_tab.getLayout("Intake Wheels", BuiltInLayouts.kList)
      .withPosition(9, 0)
      .withSize(3, 7);

    layout_4.addBoolean("Inverted", IntakeWheels::isInverted)
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_4.addBoolean("Intaking", () -> (Math.signum(IntakeWheels.getInstance().get()) == (IntakeWheels.isInverted() ? -1.0 : 1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_4.addBoolean("Shooting", () -> (Math.signum(IntakeWheels.getInstance().get()) == (IntakeWheels.isInverted() ? 1.0 : -1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_4.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", IntakeWheels.getInstance()::get, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }
}