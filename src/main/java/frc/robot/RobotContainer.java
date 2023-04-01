// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import java.util.Map;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain sDrivetrain = new Drivetrain();
  private final IntakeWheels sIntakeWheels = new IntakeWheels();
  private final IntakeTilt sIntakeTilt = new IntakeTilt();

  private final Balance cBalance = new Balance(sDrivetrain);
  private final TurnToAngle cTurnToAngle = new TurnToAngle(sDrivetrain);
  private final IntakeDown cIntakeDown = new IntakeDown(sIntakeTilt);
  private final IntakeUp cIntakeUp = new IntakeUp(sIntakeTilt);
  private final Command cIntake = sIntakeWheels.getIntakeCommand();
  private final Command cShoot = sIntakeWheels.getShootCommand();
  private final Command cSpit = sIntakeWheels.getSpitCommand();
  private final Command cAimDown = sIntakeTilt.getAimDownCommand();
  private final Command cManualHold = new StartEndCommand(
    () -> sIntakeWheels.set(Constants.IntakeConstants.kHoldingSpeed),
    sIntakeWheels::stop,
    sIntakeWheels
  );
  private final Command cStop = new RunCommand(() -> {
    sDrivetrain.stop();
    sIntakeWheels.stop();
    sIntakeTilt.disable();
    cTurnToAngle.cancel();
  });

  // Dashboard declarations
  private final SendableChooser<Autos.Type> m_autonChooser = new SendableChooser<Autos.Type>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.1978197, -179.08374);
    OI.pigeon.setYaw(0);

    // TODO: Add sendablechooser for starting action before each auto
    // Configure autonomous choices
    m_autonChooser.addOption("Long Auto", Autos.Type.Long);
    m_autonChooser.addOption("Long Spit Auto", Autos.Type.LongSpit);
    m_autonChooser.addOption("Short Auto", Autos.Type.Short);
    m_autonChooser.addOption("Short Spit Auto", Autos.Type.ShortSpit);
    m_autonChooser.addOption("Center Auto", Autos.Type.Center);
    m_autonChooser.addOption("Simple Center Auto", Autos.Type.CenterSimple);
    m_autonChooser.addOption("Just Outtake", Autos.Type.Outtake);
    m_autonChooser.setDefaultOption("None", Autos.Type.None);

    // Configure the trigger bindings
    configureBindings();

    // TODO: Test checklists with addStringArray()
    // Initialize Shuffleboard
    configureDriveTab();
    configureTestTab();
    configureMatchChecklistTab();
    configurePitChecklistTab();
    Shuffleboard.disableActuatorWidgets();
  }

  private void configureDriveTab() {
    ShuffleboardTab drive_tab = Shuffleboard.getTab("Drive");

    drive_tab.add("Auton chooser", m_autonChooser)
      .withPosition(6, 5)
      .withSize(5, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    drive_tab.addDouble("Docking Angle", OI.pigeon::getPitch)
      .withPosition(4, 0)
      .withSize(3, 3)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -45, "max", 45, "show value", true));

    drive_tab.add("Drivetrain", Drivetrain.robotDrive)
      .withPosition(7, 0)
      .withSize(6, 4)
      .withWidget(BuiltInWidgets.kDifferentialDrive)
      .withProperties(Map.of("number of wheels", 6, "wheel diameter", 60, "show velocity vectors", true));

    ShuffleboardLayout layout_1 = drive_tab.getLayout("Rotation", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 6);
    layout_1.add("Gyro", new OI.PigeonSendable(OI.pigeon))
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
    layout_1.addBoolean("TurnToAngle Enabled", () -> TurnToAngle.m_enabled)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_2 = drive_tab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(13, 0)
      .withSize(4, 8);
    layout_2.addBoolean("Inverted", () -> Constants.IntakeConstants.kIntakeSpeed < 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Intaking", cIntake::isScheduled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Outtaking", () -> cShoot.isScheduled() || cSpit.isScheduled())
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addDouble("Intake Angle", () -> sIntakeTilt.getMeasurement() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", false));
  }

  private void configureTestTab() {
    ShuffleboardTab test_tab = Shuffleboard.getTab("Test");

    ShuffleboardLayout layout_1 = test_tab.getLayout("Intake Angle", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 8);
    layout_1.addDouble("Intake Angle", () -> sIntakeTilt.getMeasurement() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_1.addDouble("Intake Setpoint", () ->
      ((cIntakeDown.isScheduled()) ? Constants.IntakeConstants.kDownPos : Constants.IntakeConstants.kUpPos) * 360
    )
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_1.addDouble("Error", () ->
      (((cIntakeDown.isScheduled()) ? Constants.IntakeConstants.kDownPos : Constants.IntakeConstants.kUpPos) - sIntakeTilt.getMeasurement()) * 360
    )
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    ShuffleboardLayout layout_2 = test_tab.getLayout("Intake Status", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(4, 8);
    layout_2.addBoolean("PID enabled", () -> cIntakeDown.isScheduled() || cIntakeUp.isScheduled() || sIntakeTilt.isEnabled())
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Keeping steady", sIntakeTilt::isEnabled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Upright", sIntakeTilt::atUpPos)
      .withWidget(BuiltInWidgets.kBooleanBox);

    test_tab.addDouble("Wheel RPM", sIntakeWheels::getVelocity)
      .withPosition(8, 0)
      .withSize(5, 2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -250, "max", 250, "center", 0));
  }

  private void configureMatchChecklistTab() {
    ShuffleboardTab match_tab = Shuffleboard.getTab("Match Checklist");
    
    String[] robot_checklist = new String[]{
      "Bumpers are the correct match color",
      "Electrical pull test successful",
      "Motor controllers are blinking in sync",
      "Battery is connected and secured",
      "Robot is in the correct start position",
      "Robot arms are in the correct position",
      "Robot has the correct game piece"
    };
    String[] station_checklist = new String[]{
      "Electronic pull test successful",
      "Joysticks are properly connected"
    };

    ShuffleboardLayout layout_1 = match_tab.getLayout("Robot Checklist", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : robot_checklist) {
      layout_1.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }

    ShuffleboardLayout layout_2 = match_tab.getLayout("Drive Station Checklist", BuiltInLayouts.kList)
      .withPosition(8, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : station_checklist) {
      layout_2.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }
  }

  private void configurePitChecklistTab() {
    ShuffleboardTab pit_tab = Shuffleboard.getTab("Pit Checklist");
    
    String[] structural_checklist = new String[]{
      "All structural components are secured",
      "Bumpers are secured",
      "Bumpers are the correct match color",
      "Bumper numbers are not damaged",
      "Motors and controllers are secured"
    };
    String[] electrical_checklist = new String[]{
      "All wiring is secured and clipped",
      "Electrical pull test successful",
      "Fully charged battery is installed",
      "Motor controllers are blinking in sync",
      "Bench test is successful"
    };
    String[] cart_checklist = new String[]{
      "Station has all needed cables",
      "Station has fully charged laptop",
      "Current code is functional and deployed",
      "Joysticks are properly connected",
      "Fully charged backup battery available",
      "Small medical kit is available",
      "Red and blue duct tape available",
      "All necessary utility tools available"
    };
    String[] post_checklist = new String[]{
      "Both batteries are removed and charging"
    };

    ShuffleboardLayout layout_1 = pit_tab.getLayout("Pre-Match Mechanical", BuiltInLayouts.kList)
      .withPosition(0, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : structural_checklist) {
      layout_1.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }

    ShuffleboardLayout layout_2 = pit_tab.getLayout("Pre-Match Electrical", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : electrical_checklist) {
      layout_2.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }

    ShuffleboardLayout layout_3 = pit_tab.getLayout("Pre-Match Cart", BuiltInLayouts.kList)
      .withPosition(8, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : cart_checklist) {
      layout_3.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }

    ShuffleboardLayout layout_4 = pit_tab.getLayout("Post-Match Checklist", BuiltInLayouts.kList)
      .withPosition(12, 0)
      .withSize(4, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String item : post_checklist) {
      layout_4.addString(item, () -> item).withWidget(BuiltInWidgets.kTextView);
    }
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Universal:
    // Button 'B' (hold) will continuously stop all movement
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.B.val)
    .or(new JoystickButton(OI.operator_cntlr, OI.Controller.btn.B.val))
      .whileTrue(cStop);

    configureDriver();
    configureOperator();
  }

  private void configureDriver() {
    // D-pad and right stick will turn to the specified angle
    new Trigger(() -> TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() != -1)
      .whileTrue(new RunCommand(() -> {
        cTurnToAngle.setHeading(OI.driver_cntlr.getPOV());
        cTurnToAngle.schedule();
      }));

    new Trigger(() ->
      TurnToAngle.m_enabled && OI.driver_cntlr.getPOV() == -1 &&
      (Math.abs(OI.driver_cntlr.getRightX()) > 0.2 || Math.abs(OI.driver_cntlr.getRightY()) > 0.2)
    )
      .whileTrue(new RunCommand(() -> {
        cTurnToAngle.setHeading(Math.toDegrees(Math.atan2(OI.driver_cntlr.getRightY(), OI.driver_cntlr.getRightX()) + 90));
        cTurnToAngle.schedule();
      }));

    // Button 'A' (hold) will run auto-balance code
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.A.val)
      .whileTrue(cBalance);

    // Button 'X' (debounced 1s) will reset gyro
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.X.val)
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        OI.pigeon.setYaw(0)
      ));

    // Button 'Y' will toggle TurnToAngle
    new JoystickButton(OI.driver_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() ->
        TurnToAngle.m_enabled ^= true
      ));
  }

  private void configureOperator() {
    // Button 'A' will swap intake and outtake (for cones)
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.A.val)
      .onTrue(new InstantCommand(() -> {
        Constants.IntakeConstants.kIntakeSpeed *= -1;
        Constants.IntakeConstants.kOuttakeSpeed *= -1;
        Constants.IntakeConstants.kSpitSpeed *= -1;
        Constants.IntakeConstants.kHoldingSpeed *= -1;

        if (cIntake.isScheduled() || cManualHold.isScheduled()) {
          sIntakeWheels.set(-sIntakeWheels.get());
        } else if (!(cShoot.isScheduled() || cSpit.isScheduled())) {
          sIntakeWheels.stop();
        }
      }));

    // Button 'X' (debounced 1s) will reset tilt encoder
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        sIntakeTilt.resetEncoder()
      ));

    // Button 'Y' will toggle automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        if (sIntakeTilt.isEnabled()) {sIntakeTilt.disable();} else {sIntakeTilt.enable();}
      }));

    // Button 'LB' (hold) will shoot cubes
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.LB.val)
      .whileTrue(cShoot);

    // Button 'RB' (hold) will lower and activate intake, then raise on release
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.RB.val)
      .whileTrue(cIntakeDown)
      .whileTrue(cIntake)
      .onFalse(cIntakeUp);

    // Triggers will disable intake and manually move up (LT) and down (RT)
    new Trigger(() -> Math.abs(OI.operator_cntlr.getTriggers()) > 0.05)
      .whileTrue(new FunctionalCommand(
        sIntakeTilt::disable,
        () -> sIntakeTilt.useOutput(
          Math.pow(OI.operator_cntlr.getTriggers(), 2) *
          ((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed),
        0),
        interrupted -> sIntakeTilt.disable(),
        () -> false,
        sIntakeTilt
      ));

    // D-pad up will move down and shoot
    new Trigger(() -> sIntakeTilt.atUpPos() && OI.operator_cntlr.getPOV() == 0)
      .onTrue(cAimDown)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimDownTimer)
      .whileTrue(cShoot);

    // D-pad right will spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 90)
      .whileTrue(cSpit);

    // D-pad down will move down and spit
    new Trigger(() -> sIntakeTilt.atUpPos() && OI.operator_cntlr.getPOV() == 180)
      .onTrue(cAimDown)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimDownTimer)
      .whileTrue(cSpit);
    
    // D-pad left will hold pieces
    new Trigger(() -> OI.operator_cntlr.getPOV() == 270)
      .whileTrue(cManualHold);
  }

  public void autoAlign() {
    sIntakeTilt.autoAlign();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Autos start backwards, so robot yaw should be backward
    return Autos.getAuto(m_autonChooser.getSelected(), sDrivetrain, sIntakeWheels, sIntakeTilt)
      .beforeStarting(() -> OI.pigeon.setYaw(180));
  }

  /** Stops all motors and disables PID controllers */
  public void stop() {
    cStop.execute();
  }
}