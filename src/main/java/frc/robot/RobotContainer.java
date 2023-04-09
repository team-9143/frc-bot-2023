// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

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

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.cscore.UsbCamera;
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
  private final DriveDistance cDriveDistance = new DriveDistance(sDrivetrain); // Only for shuffleboard
  private final IntakeDown cIntakeDown = new IntakeDown(sIntakeTilt);
  private final IntakeUp cIntakeUp = new IntakeUp(sIntakeTilt);
  private final Command cIntake = sIntakeWheels.getIntakeCommand();
  private final Command cShoot = sIntakeWheels.getShootCommand();
  private final Command cSpit = sIntakeWheels.getSpitCommand();
  private final Command cAimMid = new AimMid(sIntakeTilt);
  private final Command cManualHold = new StartEndCommand(
    () -> sIntakeWheels.set(Constants.IntakeConstants.kHoldingSpeed),
    sIntakeWheels::stop,
    sIntakeWheels
  );
  private final Command cStop = new InstantCommand(() -> {
    sDrivetrain.stop();
    sIntakeWheels.stop();
    sIntakeTilt.disable();
    IntakeWheels.m_holding = false;
    CommandScheduler.getInstance().cancelAll();
  });

  // Dashboard declarations
  private final SendableChooser<Autos.Body> m_autonBodyChooser = new SendableChooser<Autos.Body>();
  private final SendableChooser<Autos.Starter> m_autonStarterChooser = new SendableChooser<Autos.Starter>();
  private final SendableChooser<Autos.Ending> m_autonEndChooser = new SendableChooser<Autos.Ending>();
  private final GenericEntry m_autonOffset =
    Shuffleboard.getTab("Drive").add("Auton Angle Offset", 180)
      .withPosition(1, 5)
      .withSize(2, 2)
      .withWidget(BuiltInWidgets.kTextView)
      .getEntry();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.24665457, -179.574783);
    OI.pigeon.setYaw(0);

    // Configure PID controllers
    configurePID();

    // Configure autonomous choices
    configureChoosers();

    // Configure the trigger bindings
    configureBindings();

    // TODO(low prio): Test checklists with addStringArray()
    // Initialize Shuffleboard
    configureDriveTab();
    configureTestTab();
    configureMatchChecklistTab();
    configurePitChecklistTab();
    Shuffleboard.disableActuatorWidgets();
  }

  private void configurePID() {
    TurnToAngle.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kTurnMaxSpeed, Constants.DrivetrainConstants.kTurnMaxSpeed);
    TurnToAngle.m_controller.setTolerance(Constants.DrivetrainConstants.kTurnPosTolerance, Constants.DrivetrainConstants.kTurnVelTolerance);
    TurnToAngle.m_controller.enableContinuousInput(-180, 180);
    TurnToAngle.m_controller.setSetpoint(0);

    DriveDistance.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kDistMaxSpeed, Constants.DrivetrainConstants.kDistMaxSpeed);
    DriveDistance.m_controller.setTolerance(Constants.DrivetrainConstants.kDistPosTolerance, Constants.DrivetrainConstants.kDistVelTolerance);
    DriveDistance.m_controller.setSetpoint(0);

    IntakeDown.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeDown.m_controller.setSetpoint(Constants.IntakeConstants.kDownPos);

    IntakeUp.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeUp.m_controller.setTolerance(Constants.IntakeConstants.kUpPosTolerance);
    IntakeUp.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);
  }

  private void configureChoosers() {
    m_autonStarterChooser.addOption("Shoot", Autos.Starter.Shoot);
    m_autonStarterChooser.addOption("Spit", Autos.Starter.Spit);
    m_autonStarterChooser.addOption("Shoot Down", Autos.Starter.ShootDown);
    m_autonStarterChooser.addOption("Spit Down", Autos.Starter.SpitDown);
    m_autonStarterChooser.setDefaultOption("None", Autos.Starter.None);

    m_autonBodyChooser.addOption("Long Backward", Autos.Body.LongEscape);
    m_autonBodyChooser.addOption("Short Backward", Autos.Body.ShortEscape);
    m_autonBodyChooser.addOption("Pickup Cone", Autos.Body.PickupCone);
    m_autonBodyChooser.addOption("Center Over Backward", Autos.Body.CenterOver);
    m_autonBodyChooser.addOption("Center Backward", Autos.Body.CenterSimple);
    m_autonBodyChooser.setDefaultOption("None", Autos.Body.None);

    //m_autonEndChooser.addOption("Turn Away", Autos.Ending.TurnAway);
    //m_autonEndChooser.addOption("Turn Close", Autos.Ending.TurnClose);
    m_autonEndChooser.setDefaultOption("None", Autos.Ending.None);
  }

  private void configureDriveTab() {
    ShuffleboardTab drive_tab = Shuffleboard.getTab("Drive");

    // TODO(? prio): Fully implement camera
    // UsbCamera camera = CameraServer.startAutomaticCapture();
    // Shuffleboard.getTab("Camera Test").addCamera("Camera", camera.getName(), camera.getPath())
    //   .withWidget(BuiltInWidgets.kCameraStream)
    //   .withProperties(Map.of("show crosshair", false, "Rotation", "HALF"));

    drive_tab.add("Auton Starter", m_autonStarterChooser)
      .withPosition(3, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Body", m_autonBodyChooser)
      .withPosition(6, 5)
      .withSize(3, 2)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    drive_tab.add("Auton Ending", m_autonEndChooser)
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
    layout_1.add("Gyro", OI.pigeon)
      .withWidget(BuiltInWidgets.kGyro)
      .withProperties(Map.of("major tick spacing", 45, "starting angle", 180, "show tick mark ring", true));
    layout_1.addBoolean("TurnToAngle Enabled", () -> TurnToAngle.m_enabled)
      .withWidget(BuiltInWidgets.kBooleanBox);

    ShuffleboardLayout layout_2 = drive_tab.getLayout("Intake Angle", BuiltInLayouts.kGrid)
      .withPosition(7, 0)
      .withSize(6, 3)
      .withProperties(Map.of("number of columns", 2, "number of rows", 1));
    layout_2.addDouble("Intake Angle", () -> sIntakeTilt.getMeasurement() * 360)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));
    layout_2.addDouble("Intake Setpoint", () ->
      ((cIntakeDown.isScheduled()) ? Constants.IntakeConstants.kDownPos :
      (cAimMid.isScheduled()) ? Constants.IntakeConstants.kMidPos :
      Constants.IntakeConstants.kUpPos) * 360
    )
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    ShuffleboardLayout layout_3 = drive_tab.getLayout("Intake", BuiltInLayouts.kList)
      .withPosition(13, 0)
      .withSize(4, 6);
    layout_3.addBoolean("Inverted", () -> Constants.IntakeConstants.kIntakeSpeed < 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Intaking", () -> (sIntakeWheels.get() * Constants.IntakeConstants.kOuttakeSpeed) < 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_3.addBoolean("Outtaking", () -> (sIntakeWheels.get() * Constants.IntakeConstants.kOuttakeSpeed) > 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
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
      ((cIntakeDown.isScheduled()) ? Constants.IntakeConstants.kDownPos :
      (cAimMid.isScheduled()) ? Constants.IntakeConstants.kMidPos :
      Constants.IntakeConstants.kUpPos) * 360)
        .withWidget(BuiltInWidgets.kDial)
        .withProperties(Map.of("min", -110, "max", 110, "show value", true));

    layout_1.addDouble("Error", () ->
      (((cIntakeDown.isScheduled()) ? Constants.IntakeConstants.kDownPos :
      (cAimMid.isScheduled()) ? Constants.IntakeConstants.kMidPos :
      Constants.IntakeConstants.kUpPos) -
      sIntakeTilt.getMeasurement()) * 360)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -110, "max", 110, "center", 0));

    ShuffleboardLayout layout_2 = test_tab.getLayout("Intake Status", BuiltInLayouts.kList)
      .withPosition(4, 0)
      .withSize(3, 6);
    layout_2.addBoolean("PID enabled", () -> cIntakeDown.isScheduled() || cIntakeUp.isScheduled() || sIntakeTilt.isEnabled())
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Keeping steady", sIntakeTilt::isEnabled)
      .withWidget(BuiltInWidgets.kBooleanBox);
    layout_2.addBoolean("Upright", () -> sIntakeTilt.getMeasurement() < Constants.IntakeConstants.kUpPosTolerance)
      .withWidget(BuiltInWidgets.kBooleanBox);

    test_tab.addDouble("Intake Wheel RPM", sIntakeWheels::getVelocity)
      .withPosition(7, 0)
      .withSize(4, 2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -250, "max", 250, "center", 0));

    test_tab.addDouble("TurnToAngle Error", () -> cTurnToAngle.getController().getPositionError())
      .withPosition(7, 2)
      .withSize(4, 2)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("min", -180, "max", 180, "center", 0));

    test_tab.addDouble("DriveDistance Error", () -> cDriveDistance.getController().getPositionError())
      .withPosition(7, 4)
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
        cTurnToAngle.setHeading(Math.toDegrees(Math.atan2(OI.driver_cntlr.getRightY(), OI.driver_cntlr.getRightX())) + 90);
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
      .onTrue(new InstantCommand(() -> {
        cTurnToAngle.cancel();
        TurnToAngle.m_enabled ^= true;
      }));
  }

  private void configureOperator() {
    // Button 'A' will swap intake and outtake (for cones)
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.A.val)
      .onTrue(new InstantCommand(() -> {
        sIntakeWheels.invert();

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

    // D-pad up will angle down, then shoot
    new Trigger(() -> OI.operator_cntlr.getPOV() == 0)
      .whileTrue(cAimMid)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimMidTimer)
      .whileTrue(cShoot);

    // D-pad right will spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 90)
      .whileTrue(cSpit);

    // D-pad down will angle down, then spit
    new Trigger(() -> OI.operator_cntlr.getPOV() == 180)
      .whileTrue(cAimMid)
      .onFalse(cIntakeUp)
    .debounce(Constants.IntakeConstants.kAimMidTimer)
      .whileTrue(cSpit);

    // D-pad left will hold pieces
    new Trigger(() -> OI.operator_cntlr.getPOV() == 270)
      .whileTrue(cManualHold);
  }

  public void autoAlign() {
    sIntakeTilt.autoAlign();
  }

  public void enableIntake() {
    sIntakeTilt.enable();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Autos.getAuto(m_autonStarterChooser.getSelected(), m_autonBodyChooser.getSelected(), m_autonEndChooser.getSelected(), sIntakeTilt, sIntakeWheels, sDrivetrain)
      .beforeStarting(() -> OI.pigeon.setYaw(0))
      .andThen(() -> OI.pigeon.setYaw(OI.pigeon.getYaw() - m_autonOffset.getDouble(180)));
  }

  /** Stops all motors and disables PID controllers */
  public void stop() {
    cStop.initialize();
  }
}