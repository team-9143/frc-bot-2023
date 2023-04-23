package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardTabBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import java.util.Map;

import frc.robot.subsystems.IntakeTilt;
import frc.robot.subsystems.IntakeWheels;
import frc.robot.autos.AutoSelector;
import frc.robot.commands.TurnToAngle;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;

/** Contains auton selector and data for driver and operator. */
public class DriveTab implements ShuffleboardTabBase {
  protected static final ShuffleboardTab drive_tab = Shuffleboard.getTab("Drive");

  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  protected DriveTab() {}

  public void initialize() {
    ShuffleboardManager.cubeLoaded = drive_tab.add("Cube Preloaded", true)
      .withPosition(0, 5)
      .withSize(2, 1)
      .withWidget(BuiltInWidgets.kToggleSwitch)
      .getEntry();
    ShuffleboardManager.choosersSynced = drive_tab.add("Choosers Synced", true)
      .withPosition(0, 6)
      .withSize(2, 1)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .getEntry();
    drive_tab.add("Auton Starter", AutoSelector.m_starterChooser)
      .withPosition(2, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Body", AutoSelector.m_bodyChooser)
      .withPosition(5, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Secondary", AutoSelector.m_secondaryChooser)
      .withPosition(8, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Tertiary", AutoSelector.m_tertiaryChooser)
      .withPosition(11, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Ending", AutoSelector.m_endingChooser)
      .withPosition(14, 5)
      .withSize(2, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    ShuffleboardLayout layout_1 = drive_tab.getLayout("Rotation", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 5);
    layout_1.add("Gyro", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", () -> -OI.pigeon.getYaw() % 360, null);
      }
    }).withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
    layout_1.addBoolean("TurnToAngle Enabled", () -> TurnToAngle.m_enabled)
      .withWidget(BuiltInWidgets.kBooleanBox);

    drive_tab.addDouble("Docking Angle", OI.pigeon::getPitch)
      .withPosition(4, 0)
      .withSize(3, 3)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -45, "max", 45, "show value", true));

    ShuffleboardLayout layout_2 = drive_tab.getLayout("Intake Angle", BuiltInLayouts.kGrid)
      .withPosition(7, 0)
      .withSize(6, 3)
      .withProperties(Map.of("number of columns", 2, "number of rows", 1));
    layout_2.addDouble("Intake Angle", () -> sIntakeTilt.getPosition() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_2.addDouble("Intake Setpoint", () -> IntakeTilt.m_setpoint * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    ShuffleboardLayout layout_3 = drive_tab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(13, 0)
      .withSize(4, 5);
    layout_3.addBoolean("Inverted", IntakeWheels::isInverted)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Intaking", () -> (sIntakeWheels.get() * IntakeConstants.kIntakeSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Shooting", () -> (sIntakeWheels.get() * IntakeConstants.kShootSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }
}