package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.Drivetrain;

/** Contains auto types, choosers, and compiler. */
public final class AutoSelector {
  // TODO: Add constant title for each auto piece
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
  public static enum Secondary {
    RETURN_FROM_CONE,
    NONE
  }
  public static enum Tertiary {
    CONE_SHOOT,
    CONE_SPIT,
    CONE_SHOOT_DOWN,
    CONE_SPIT_DOWN,
    NONE
  }
  public static enum Ending {
    TURN_AWAY,
    TURN_CLOSE,
    NONE
  }

  public static final MutableChooser<Starter> m_starterChooser = new MutableChooser<>("None", Starter.NONE);
  public static final MutableChooser<Body> m_bodyChooser = new MutableChooser<>("None", Body.NONE);
  public static final MutableChooser<Secondary> m_secondaryChooser = new MutableChooser<>("None", Secondary.NONE);
  public static final MutableChooser<Tertiary> m_tertiaryChooser = new MutableChooser<>("None", Tertiary.NONE);
  public static final MutableChooser<Ending> m_endingChooser = new MutableChooser<>("None", Ending.NONE);

  public static void initializeChoosers() {
    m_starterChooser.add("Shoot", Starter.SHOOT);
    m_starterChooser.add("Spit", Starter.SPIT);
    m_starterChooser.add("Shoot Down", Starter.SHOOT_DOWN);
    m_starterChooser.add("Spit Down", Starter.SPIT_DOWN);

    m_bodyChooser.add("Long Backward", Body.ESCAPE_LONG);
    m_bodyChooser.add("Short Backward", Body.ESCAPE_SHORT);
    m_bodyChooser.add("Pickup Cone", Body.PICKUP_CONE);
    m_bodyChooser.add("Center Over Backward", Body.CENTER_OVER);
    m_bodyChooser.add("Center Backward", Body.CENTER_SIMPLE);

    m_secondaryChooser.add("Return From Cone", Secondary.RETURN_FROM_CONE);

    m_tertiaryChooser.add("Cone Shoot", Tertiary.CONE_SHOOT);
    m_tertiaryChooser.add("Cone Spit", Tertiary.CONE_SPIT);
    m_tertiaryChooser.add("Cone Shoot Down", Tertiary.CONE_SHOOT_DOWN);
    m_tertiaryChooser.add("Cone Spit Down", Tertiary.CONE_SPIT_DOWN);

    m_endingChooser.add("Turn Away", Ending.TURN_AWAY);
    m_endingChooser.add("Turn Close", Ending.TURN_CLOSE);
  }

  /**
   * @return an auton compiled from the starter, body, and ending {@link SendableChooser}
   */
  public static Command getAuto() {
    // TODO:(mid prio): Attempt to add dynamic secondary body module with values depending on the selected first body module, editing values with a refresh button
    Starter starter = m_starterChooser.getSelected();
    Body body = m_bodyChooser.getSelected();
    Secondary secondary = m_secondaryChooser.getSelected();
    Tertiary tertiary = m_tertiaryChooser.getSelected();
    Ending ending = m_endingChooser.getSelected();

    return new SequentialCommandGroup(
      Starters.getStarter(starter)
        .raceWith(new RunCommand(Drivetrain::stop, Drivetrain.getInstance())),
      Bodies.getBody(body),
      Secondaries.getSecondary(secondary, body),
      Tertiaries.getTertiary(tertiary)
        .raceWith(new RunCommand(Drivetrain::stop, Drivetrain.getInstance())),
      Endings.getEnding(ending)
    );
  }
}