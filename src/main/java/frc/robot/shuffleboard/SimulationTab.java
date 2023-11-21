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

import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

/** Contains auton selector and data for driver and operator. */
public class SimulationTab implements ShuffleboardTabBase {
  private final ShuffleboardTab sim_tab;
  private final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  public static GenericEntry yaw_sim;
  public static GenericEntry drivetrainPos_sim;
  public static GenericEntry intakeAngle_sim;
  public static GenericEntry pitch_sim;

  protected SimulationTab() {
    sim_tab = Shuffleboard.getTab("Simulation");
  }

  public void initialize() {
    initLayout1();
    initLayout2();
    initLayout3();
    initLayout4();

    sim_tab.add("Drivetrain", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", sDrivetrain::getLeft, null);
        builder.addDoubleProperty("Right Motor Speed", () -> -sDrivetrain.getRight(), null);
      }
    }).withPosition(11, 0)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));

    pitch_sim = sim_tab.add("Docking Angle", 0)
      .withPosition(11, 4)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -45, "max", 45, "block increment", 2))
      .getEntry();
  }

  // Turn to angle
  private void initLayout1() {
    ShuffleboardLayout layout_1 = sim_tab.getLayout("TurnToAngle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(3, 8);

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
        builder.addDoubleProperty("Value", () -> (TurnToAngle.isRunning()) ? sDrivetrain.getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_1.addBoolean("Running", TurnToAngle::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  // Drive distance
  private void initLayout2() {
    ShuffleboardLayout layout_2 = sim_tab.getLayout("DriveDistance", BuiltInLayouts.kList)
      .withPosition(3, 0)
      .withSize(3, 8);

    layout_2.addDouble("Setpoint", DriveDistance.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -225, "max", 225, "center", 0));

    if (ShuffleboardManager.m_simulatedDrive) {
      drivetrainPos_sim = layout_2.add("Position", 0)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", -225, "max", 225, "block increment", 3))
        .getEntry();
    } else {
      layout_2.addDouble("Position", sDrivetrain::getPosition)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -225, "max", 225, "center", 0));
    }

    layout_2.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", () -> (DriveDistance.isRunning()) ? sDrivetrain.getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_2.addBoolean("Running", DriveDistance::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  // Intake angle
  private void initLayout3() {
    ShuffleboardLayout layout_3 = sim_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(6, 0)
      .withSize(3, 8);

    layout_3.addDouble("Setpoint", IntakeTilt::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    if (ShuffleboardManager.m_simulatedTilt) {
      intakeAngle_sim = layout_3.add("Angle", IntakeConstants.kUpPos)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", -110, "max", 110, "block increment", 4))
        .getEntry();
    } else {
      layout_3.addDouble("Angle", sIntakeTilt::getPosition)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -110, "max", 110, "center", 0));
    }

    layout_3.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", sIntakeTilt::getSpeed, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_3.addBoolean("Running", IntakeTilt::isRunning)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

  // Intake wheels
  private void initLayout4() {
    ShuffleboardLayout layout_4 = sim_tab.getLayout("Intake Wheels", BuiltInLayouts.kList)
      .withPosition(9, 0)
      .withSize(2, 8);

      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.addBoolean("Inverted", IntakeWheels::isCone)

    layout_4.addBoolean("Intaking", () -> (Math.signum(sIntakeWheels.getSpeed()) == (IntakeWheels.isCone() ? -1.0 : 1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_4.addBoolean("Shooting", () -> (Math.signum(sIntakeWheels.getSpeed()) == (IntakeWheels.isCone() ? 1.0 : -1.0)))
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_4.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", sIntakeWheels::getSpeed, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }
}