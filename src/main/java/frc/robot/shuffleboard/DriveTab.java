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

  public void initialize() {
    // TODO(? prio): Implement camera
    // UsbCamera camera = CameraServer.startAutomaticCapture();
    // Shuffleboard.getTab("Camera Test").addCamera("Camera", camera.getName(), camera.getPath())
    //   .withWidget(BuiltInWidgets.kCameraStream)
    //   .withProperties(Map.of("show crosshair", false, "Rotation", "HALF"));

    drive_tab.add("Auton Starter", AutoSelector.m_starterChooser)
      .withPosition(3, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Body", AutoSelector.m_bodyChooser)
      .withPosition(6, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Ending", AutoSelector.m_endingChooser)
      .withPosition(9, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    drive_tab.addDouble("Docking Angle", OI.pigeon::getPitch)
      .withPosition(4, 0)
      .withSize(3, 3)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -45, "max", 45, "show value", true));

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

    ShuffleboardLayout layout_2 = drive_tab.getLayout("Intake Angle", BuiltInLayouts.kGrid)
      .withPosition(7, 0)
      .withSize(6, 3)
      .withProperties(Map.of("number of columns", 2, "number of rows", 1));
    layout_2.addDouble("Intake Angle", () -> sIntakeTilt.getPosition() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_2.addDouble("Intake Setpoint", () -> IntakeTilt.m_setpoint * 360
    )
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    ShuffleboardLayout layout_3 = drive_tab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(13, 0)
      .withSize(4, 6);
    layout_3.addBoolean("Inverted", IntakeWheels::isInverted)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Intaking", () -> (sIntakeWheels.get() * IntakeConstants.kOuttakeSpeed) < 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Outtaking", () -> (sIntakeWheels.get() * IntakeConstants.kOuttakeSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }
}