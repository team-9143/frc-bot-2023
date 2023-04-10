package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.Drivetrain;

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

  public static final SendableChooser<Body> m_bodyChooser = new SendableChooser<Body>();
  public static final SendableChooser<Starter> m_starterChooser = new SendableChooser<Starter>();
  public static final SendableChooser<Ending> m_endChooser = new SendableChooser<Ending>();
  
  public static void initializeChoosers() {
    m_starterChooser.addOption("Shoot", AutoSelector.Starter.CUBE_SHOOT);
    m_starterChooser.addOption("Spit", AutoSelector.Starter.CUBE_SPIT);
    m_starterChooser.addOption("Shoot Down", AutoSelector.Starter.CUBE_SHOOT_DOWN);
    m_starterChooser.addOption("Spit Down", AutoSelector.Starter.CUBE_SPIT_DOWN);
    m_starterChooser.setDefaultOption("None", AutoSelector.Starter.NONE);

    m_bodyChooser.addOption("Long Backward", AutoSelector.Body.ESCAPE_LONG);
    m_bodyChooser.addOption("Short Backward", AutoSelector.Body.ESCAPE_SHORT);
    m_bodyChooser.addOption("Pickup Cone", AutoSelector.Body.PICKUP_CONE);
    m_bodyChooser.addOption("Center Over Backward", AutoSelector.Body.CENTER_OVER);
    m_bodyChooser.addOption("Center Backward", AutoSelector.Body.CENTER_SIMPLE);
    m_bodyChooser.setDefaultOption("None", AutoSelector.Body.NONE);

    m_endChooser.addOption("Turn Away", AutoSelector.Ending.TURN_AWAY);
    m_endChooser.addOption("Turn Close", AutoSelector.Ending.TURN_CLOSE);
    m_endChooser.addOption("Return From Cone", AutoSelector.Ending.RETURN_FROM_CONE);
    m_endChooser.setDefaultOption("None", AutoSelector.Ending.NONE);
  }

  public static Command getAuto() {
    // TODO(HIGH prio): Fix auton endings - entails rewriting static headings in TurnToAngle and DriveDistance, as well as allowing for values (ex. current encoder position) to be given to the end module at module initialization, potential create as a new type of command
    // TODO:(mid prio): Attempt to add dynamic secondary body module with values depending on the selected first body module, editing values with a refresh button
    return new SequentialCommandGroup(
      Starters.getStarter(m_starterChooser.getSelected())
        .raceWith(new RunCommand(Drivetrain::stop, Drivetrain.getInstance())),
      Bodies.getBody(m_bodyChooser.getSelected()),
      Endings.getEnding(m_endChooser.getSelected(), m_bodyChooser.getSelected())
    );
  }
}