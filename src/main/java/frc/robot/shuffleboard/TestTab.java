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
import frc.robot.Constants.IntakeConstants;

public class TestTab implements ShuffleboardTabBase {
  protected static final ShuffleboardTab test_tab = Shuffleboard.getTab("Test");

  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  public void initialize() {
    ShuffleboardLayout layout_1 = test_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 8);
    layout_1.addDouble("Intake Angle", () -> sIntakeTilt.getPosition() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_1.addDouble("Intake Setpoint", () -> IntakeTilt.m_setpoint * 360)
        .withWidget(BuiltInWidgets.kDial)
        .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Intake Error", () -> (IntakeTilt.m_setpoint - sIntakeTilt.getPosition()) * 360)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    ShuffleboardLayout layout_2 = test_tab.getLayout("Intake Status", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(3, 6);
    layout_2.addBoolean("Steady", IntakeTilt::isEnabled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addDouble("Tilt Speed", sIntakeTilt::get)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -IntakeConstants.kTiltMaxSpeed, "max", IntakeConstants.kTiltMaxSpeed, "center", 0));
    layout_2.addDouble("Intake Wheel RPM", sIntakeWheels::getVelocity)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -250, "max", 250, "center", 0));

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

    test_tab.add("Drivetrain", Drivetrain.robotDrive)
      .withPosition(11, 0)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));

    test_tab.add("Gyro", OI.pigeon)
      .withPosition(11, 4)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
  }
}