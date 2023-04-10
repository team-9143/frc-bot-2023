// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.shuffleboard.ShuffleboardManager;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import java.util.Map;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private static RobotContainer m_instance;

  /** @return the singleton instance */
  public static RobotContainer getInstance() {
    if (m_instance == null) {
      m_instance = new RobotContainer();
    }
    return m_instance;
  }

  // The robot's subsystems and commands are defined here...
  private static final Drivetrain sDrivetrain = Drivetrain.getInstance();
  private static final IntakeWheels sIntakeWheels = IntakeWheels.getInstance();
  private static final IntakeTilt sIntakeTilt = IntakeTilt.getInstance();

  private final Balance cBalance = new Balance();
  private final TurnToAngle cTurnToAngle = new TurnToAngle(0);
  private final IntakeDown cIntakeDown = new IntakeDown();
  private final IntakeUp cIntakeUp = new IntakeUp();
  private final Command cIntake = sIntakeWheels.getIntakeCommand();
  private final Command cShoot = sIntakeWheels.getShootCommand();
  private final Command cSpit = sIntakeWheels.getSpitCommand();
  private final Command cAimMid = new AimMid();
  private final Command cManualHold = new StartEndCommand(
    () -> sIntakeWheels.set(Constants.IntakeConstants.kHoldingSpeed),
    IntakeWheels::stop,
    sIntakeWheels
  );
  private static final Command cStop = new RunCommand(() -> {
    Drivetrain.stop();
    IntakeWheels.stop();
    IntakeTilt.stop();
  }, sDrivetrain, sIntakeWheels, sIntakeTilt)
    .withInterruptBehavior(InterruptionBehavior.kCancelIncoming);

  /** The container for the robot. Intializes subsystems, teleop commands, and OI devices. */
  private RobotContainer() {
    // Configure Pigeon - make sure to update pitch and roll offsets
    OI.pigeon.configMountPose(0, -0.24665457, -179.574783);
    OI.pigeon.setYaw(0);

    // Configure PID controllers
    configureControllers();

    // Configure the trigger bindings
    configureBindings();

    // TODO(low prio): Test checklists with addStringArray()
    // Initialize Shuffleboard
    ShuffleboardManager.getInstance();
    
    configureMatchChecklistTab();
    configurePitChecklistTab();
    Shuffleboard.disableActuatorWidgets();
  }

  private void configureControllers() {
    TurnToAngle.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kTurnMaxSpeed, Constants.DrivetrainConstants.kTurnMaxSpeed);
    TurnToAngle.m_controller.setTolerance(Constants.DrivetrainConstants.kTurnPosTolerance, Constants.DrivetrainConstants.kTurnVelTolerance);
    TurnToAngle.m_controller.enableContinuousInput(-180, 180);
    TurnToAngle.m_controller.setSetpoint(0);

    DriveDistance.m_controller.setIntegratorRange(-Constants.DrivetrainConstants.kDistMaxSpeed, Constants.DrivetrainConstants.kDistMaxSpeed);
    DriveDistance.m_controller.setTolerance(Constants.DrivetrainConstants.kDistPosTolerance, Constants.DrivetrainConstants.kDistVelTolerance);
    DriveDistance.m_controller.setSetpoint(0);

    IntakeTilt.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeTilt.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);

    IntakeDown.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeDown.m_controller.setSetpoint(Constants.IntakeConstants.kDownPos);

    IntakeUp.m_controller.setIntegratorRange(-Constants.IntakeConstants.kTiltMaxSpeed, Constants.IntakeConstants.kTiltMaxSpeed);
    IntakeUp.m_controller.setTolerance(Constants.IntakeConstants.kUpPosTolerance);
    IntakeUp.m_controller.setSetpoint(Constants.IntakeConstants.kUpPos);
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
        IntakeWheels.invert();

        if (cIntake.isScheduled() || cManualHold.isScheduled()) {
          sIntakeWheels.set(-sIntakeWheels.get());
        } else if (!(cShoot.isScheduled() || cSpit.isScheduled())) {
          IntakeWheels.stop();
        }
      }));

    // Button 'X' (debounced 1s) will reset tilt encoder
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.X.val)
    .debounce(1)
      .onTrue(new InstantCommand(() ->
        sIntakeTilt.resetEncoders()
      ));

    // Button 'Y' will toggle automatic intake control
    new JoystickButton(OI.operator_cntlr, OI.Controller.btn.Y.val)
      .onTrue(new InstantCommand(() -> {
        if (IntakeTilt.isEnabled()) {IntakeTilt.disable();} else {IntakeTilt.enable();}
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
        IntakeTilt::disable,
        () -> sIntakeTilt.set(
          Math.pow(OI.operator_cntlr.getTriggers(), 2) *
          ((OI.operator_cntlr.getTriggers() < 0) ? Constants.IntakeConstants.kUpSpeed : Constants.IntakeConstants.kDownSpeed)
        ),
        interrupted -> IntakeTilt.disable(),
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

  /** Stops all motors, disables PID controllers, and cancels commands requiring actuator subsystems */
  public static void stop() {
    cStop.execute();
  }
}