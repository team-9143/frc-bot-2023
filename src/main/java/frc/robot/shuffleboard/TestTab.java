package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardTabBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import java.util.Map;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeTilt;
import frc.robot.subsystems.IntakeWheels;
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.DriveDistance;

import frc.robot.OI;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

/** Contains PID errors and speed of motors. */
public class TestTab implements ShuffleboardTabBase {
  private final ShuffleboardTab test_tab;
  private final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  protected TestTab() {
    test_tab = Shuffleboard.getTab("Test");
  }

  public void initialize() {
    initLayout1();
    initLayout2();
    initLayout3();
    initLayout4();

    test_tab.add("Drivetrain", new Sendable() {
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

    test_tab.add("Gyro", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", () -> -OI.pigeon.getYaw() % 360, null);
      }
    }).withPosition(11, 4)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
  }

  // Intake angle
  private void initLayout1() {
    ShuffleboardLayout layout_1 = test_tab.getLayout("Intake Tilt", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 8);

    layout_1.addDouble("Angle", sIntakeTilt::getPosition)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Setpoint", IntakeTilt::getSetpoint)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Error", () -> IntakeTilt.getSetpoint() - sIntakeTilt.getPosition())
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    layout_1.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", sIntakeTilt::getSpeed, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }

  // Turn to angle
  private void initLayout2() {
    ShuffleboardLayout layout_2 = test_tab.getLayout("Turn to Angle", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(3, 4);

    layout_2.addDouble("Error", TurnToAngle.m_controller::getPositionError)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));

    layout_2.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", () -> (TurnToAngle.isRunning()) ? sDrivetrain.getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }

  // Intake wheels
  private void initLayout3() {
    ShuffleboardLayout layout_3 = test_tab.getLayout("Intake Wheels", BuiltInLayouts.kList)
      .withPosition(4, 4)
      .withSize(3, 4);

      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Inverted", IntakeWheels::isCone)

    layout_3.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", sIntakeWheels::getSpeed, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }

  // Drive distance
  private void initLayout4() {
    ShuffleboardLayout layout_4 = test_tab.getLayout("Drive Distance", BuiltInLayouts.kList)
      .withPosition(7, 0)
      .withSize(3, 6);

    layout_4.addDouble("Error", DriveDistance.m_controller::getPositionError)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));

    layout_4.add("Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", () -> (TurnToAngle.isRunning()) ? sDrivetrain.getLeft() : 0, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));

    layout_4.addDouble("Position", sDrivetrain::getPosition)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -225, "max", 225, "center", 0));
  }
}