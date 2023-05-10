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

    test_tab.addDouble("TurnToAngle Error", TurnToAngle.m_controller::getPositionError)
      .withPosition(7, 0)
      .withSize(4, 2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));

    test_tab.addDouble("DriveDistance Error", DriveDistance.m_controller::getPositionError)
      .withPosition(7, 2)
      .withSize(4, 2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -150, "max", 150, "center", 0));

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
    ShuffleboardLayout layout_1 = test_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 8);

    layout_1.addDouble("Intake Angle", sIntakeTilt::getPosition)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Intake Setpoint", IntakeTilt::getSetpoint)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Intake Error", () -> IntakeTilt.getSetpoint() - sIntakeTilt.getPosition())
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    layout_1.add("Tilt Speed", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.addDoubleProperty("Value", sIntakeTilt::getSpeed, null);
      }
    }).withWidget(BuiltInWidgets.kMotorController)
      .withProperties(Map.of("orientation", "HORIZONTAL"));
  }

  // Intake status and speed
  private void initLayout2() {
    ShuffleboardLayout layout_2 = test_tab.getLayout("Intake Status", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(3, 6);

    layout_2.addBoolean("Steady", IntakeTilt::isSteadyEnabled)
      .withWidget(BuiltInWidgets.kBooleanBox);

    layout_2.addDouble("Intake Wheel Speed", sIntakeWheels::getSpeed)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -1, "max", 1, "center", 0));
  }
}