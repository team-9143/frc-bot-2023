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

import frc.robot.OI;
import frc.robot.Constants.IntakeConstants;

/** Contains auton selector and data for driver and operator. */
public class SimulationTab implements ShuffleboardTabBase {
  protected static final ShuffleboardTab sim_tab = Shuffleboard.getTab("Simulation");

  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();

  public static GenericEntry drivetrainPos_sim;
  public static GenericEntry pitch_sim;
  public static GenericEntry intakeAngle_sim;

  protected SimulationTab() {}

  public void initialize() { 
    ShuffleboardLayout layout_1 = sim_tab.getLayout("TurnToAngle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(3, 4);
    layout_1.addDouble("Setpoint", TurnToAngle.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));
    layout_1.addDouble("Speed", () -> (sDrivetrain.getLeft() - sDrivetrain.getRight())/2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -1, "max", 1, "center", 0));

    ShuffleboardLayout layout_2 = sim_tab.getLayout("DriveDistance", BuiltInLayouts.kList)
      .withPosition(3, 0)
      .withSize(3, 6);
    layout_2.addDouble("Setpoint", DriveDistance.m_controller::getSetpoint)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -200, "max", 200, "center", 0));
    drivetrainPos_sim = layout_2.add("Position", 0)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -200, "max", 200, "block increment", 2))
      .getEntry();
    layout_2.addDouble("Speed", () -> (sDrivetrain.getLeft() + sDrivetrain.getRight())/2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -1, "max", 1, "center", 0));

    ShuffleboardLayout layout_3 = sim_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(6, 0)
      .withSize(3, 6);
    layout_3.addDouble("Setpoint", () -> IntakeTilt.m_setpoint * 360)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -110, "max", 110, "center", 0));
    intakeAngle_sim = layout_3.add("Angle", IntakeConstants.kUpPos * 360)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", -110, "max", 110, "block increment", 1))
      .getEntry();
    layout_3.addDouble("Speed", sIntakeTilt::get)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -1, "max", 1, "center", 0));
    
    ShuffleboardLayout layout_4 = sim_tab.getLayout("Intake Wheels", BuiltInLayouts.kList)
      .withPosition(9, 0)
      .withSize(3, 6);
    layout_4.addBoolean("Inverted", IntakeWheels::isInverted)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.addBoolean("Intaking", () -> (sIntakeWheels.get() * IntakeConstants.kIntakeSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_4.addBoolean("Shooting", () -> (sIntakeWheels.get() * IntakeConstants.kShootSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    
    pitch_sim = sim_tab.add("Docking Angle", 0)
      .withPosition(0, 4)
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
        builder.addDoubleProperty("Left Motor Speed", Drivetrain.getInstance()::getLeft, null);
        builder.addDoubleProperty("Right Motor Speed", () -> -Drivetrain.getInstance().getRight(), null);
      }
    }).withPosition(12, 0)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));

    sim_tab.add("Gyro", new Sendable() {
      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", () -> -OI.pigeon.getYaw(), v -> OI.pigeon.setYaw(-v));
      }
    }).withPosition(12, 4)
      .withSize(5, 4)
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
  }
}