package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.Drivetrain;

/** Contains auto types, choosers, and compiler. */
public final class AutoSelector {
  public static enum Starter {
    SHOOT,
    SPIT,
    SHOOT_DOWN,
    SPIT_DOWN,
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

  public static final MutableChooser<Starter> m_starterChooser = new MutableChooser<>("None", Starter.NONE);
  public static final MutableChooser<Body> m_bodyChooser = new MutableChooser<>("None", Body.NONE);
  public static final MutableChooser<Ending> m_endingChooser = new MutableChooser<>("None", Ending.NONE);

  public static void initializeChoosers() {
    m_starterChooser.add("Shoot", AutoSelector.Starter.SHOOT);
    m_starterChooser.add("Spit", AutoSelector.Starter.SPIT);
    m_starterChooser.add("Shoot Down", AutoSelector.Starter.SHOOT_DOWN);
    m_starterChooser.add("Spit Down", AutoSelector.Starter.SPIT_DOWN);

    m_bodyChooser.add("Long Backward", AutoSelector.Body.ESCAPE_LONG);
    m_bodyChooser.add("Short Backward", AutoSelector.Body.ESCAPE_SHORT);
    m_bodyChooser.add("Pickup Cone", AutoSelector.Body.PICKUP_CONE);
    m_bodyChooser.add("Center Over Backward", AutoSelector.Body.CENTER_OVER);
    m_bodyChooser.add("Center Backward", AutoSelector.Body.CENTER_SIMPLE);

    m_endingChooser.add("Turn Away", AutoSelector.Ending.TURN_AWAY);
    m_endingChooser.add("Turn Close", AutoSelector.Ending.TURN_CLOSE);
    m_endingChooser.add("Return From Cone", AutoSelector.Ending.RETURN_FROM_CONE);
  }

  /**
   * @return an auton compiled from the starter, body, and ending {@link SendableChooser}
   */
  public static Command getAuto() {
    // TODO(HIGH prio): Fix auton endings - entails rewriting static headings in TurnToAngle and DriveDistance, as well as allowing for values (ex. current encoder position) to be given to the end module at module initialization, potential create as a new type of command
    // TODO:(mid prio): Attempt to add dynamic secondary body module with values depending on the selected first body module, editing values with a refresh button
    return new SequentialCommandGroup(
      Starters.getStarter(m_starterChooser.getSelected())
        .raceWith(new RunCommand(Drivetrain::stop, Drivetrain.getInstance())),
      Bodies.getBody(m_bodyChooser.getSelected()),
      Endings.getEnding(m_endingChooser.getSelected(), m_bodyChooser.getSelected())
    );
  }
}