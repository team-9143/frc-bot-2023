// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.*;

public final class AutoSelector {
  public static enum Starter {
    CUBE_SHOOT,
    CUBE_SPIT,
    CUBE_SHOOT_DOWN,
    CUBE_SPIT_DOWN,
    NONE
  }
  public static enum Body {
    ESCAPE_LONG,
    ESCAPE_SHORT,
    PICKUP_CONE,
    CENTER_OVER,
    CENTER_SIMPLE,
    NONE
  }
  public static enum Ending {
    TURN_AWAY,
    TURN_CLOSE,
    RETURN_FROM_CONE,
    NONE
  }

  
  public static Command getAuto(Starter starter, Body body, Ending end, IntakeTilt sIntakeTilt, IntakeWheels sIntakeWheels, Drivetrain sDrivetrain) {
    // TODO(HIGH prio): Fix auton endings - entails rewriting static headings in TurnToAngle and DriveDistance, as well as allowing for values (ex. current encoder position) to be given to the end module at module initialization, potential create as a new type of command
    // TODO:(mid prio): Attempt to add dynamic secondary body module with values depending on the selected first body module, editing values with a refresh button
    return new SequentialCommandGroup(
      Starters.getStarter(starter, sIntakeTilt, sIntakeWheels).raceWith(new RunCommand(sDrivetrain::stop, sDrivetrain)),
      Bodies.getBody(body, sDrivetrain, sIntakeTilt, sIntakeWheels),
      Endings.getEnding(end, body, sDrivetrain, sIntakeTilt, sIntakeWheels)
    );
  }
}