package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.Drivetrain;

/** Contains auto types, choosers, and compiler. */
public final class AutoSelector {
  public static interface Named {
    public String getName();
  }

  public static enum Starter implements Named {
    SHOOT("Shoot"),
    SPIT("Spit"),
    SHOOT_DOWN("Shoot Down"),
    SPIT_DOWN("Spit Down"),
    NONE("None");

    private final String name;
    private Starter(String name) {this.name = name;}
    public String getName() {return name;}
  }
  public static enum Body implements Named {
    LONG_ESCAPE("Long Escape"),
    SHORT_ESCAPE("Short Escape"),
    PICKUP_CONE("Pickup Cone"),
    CENTER_CLIMB("Center Climb"),
    NONE("None");

    private final String name;
    private Body(String name) {this.name = name;}
    public String getName() {return name;}
  }
  public static enum Secondary implements Named {
    RETURN_FROM_CONE("Return From Cone"),
    CENTER_ESCAPE("Center Escape"),
    NONE("None");

    private final String name;
    private Secondary(String name) {this.name = name;}
    public String getName() {return name;}
  }
  public static enum Tertiary implements Named {
    CONE_SHOOT("Cone Shoot"),
    CONE_SPIT("Cone Spit"),
    CONE_SHOOT_DOWN("Cone Shoot Down"),
    CONE_SPIT_DOWN("Cone Spit Down"),
    NONE("None");

    private final String name;
    private Tertiary(String name) {this.name = name;}
    public String getName() {return name;}
  }
  public static enum Ending implements Named {
    TURN_AWAY("Turn Away"),
    TURN_CLOSE("Turn Close"),
    BALANCE("Balance"),
    NONE("None");

    private final String name;
    private Ending(String name) {this.name = name;}
    public String getName() {return name;}
  }

  public static final MutableChooser<Starter> m_starterChooser = new MutableChooser<>(Starter.NONE);
  public static final MutableChooser<Body> m_bodyChooser = new MutableChooser<>(Body.NONE);
  public static final MutableChooser<Secondary> m_secondaryChooser = new MutableChooser<>(Secondary.NONE);
  public static final MutableChooser<Tertiary> m_tertiaryChooser = new MutableChooser<>(Tertiary.NONE);
  public static final MutableChooser<Ending> m_endingChooser = new MutableChooser<>(Ending.NONE);

  public static void initializeChoosers() {
    m_starterChooser.setAll(Starter.SHOOT, Starter.SPIT, Starter.SHOOT_DOWN, Starter.SPIT_DOWN);

    m_bodyChooser.setAll(Body.LONG_ESCAPE, Body.SHORT_ESCAPE, Body.PICKUP_CONE, Body.CENTER_CLIMB);
    m_bodyChooser.bindTo((t, u) -> {
      switch (u) {
        case PICKUP_CONE:
          m_secondaryChooser.setAll(Secondary.RETURN_FROM_CONE);
          m_tertiaryChooser.setAll(Tertiary.CONE_SHOOT, Tertiary.CONE_SPIT, Tertiary.CONE_SHOOT_DOWN, Tertiary.CONE_SPIT_DOWN);
          m_endingChooser.setAll(Ending.TURN_AWAY, Ending.TURN_CLOSE);
          break;
        case CENTER_CLIMB:
          m_secondaryChooser.setAll(Secondary.CENTER_ESCAPE);
          m_tertiaryChooser.setAll();
          m_endingChooser.setAll(Ending.BALANCE);
          break;
        default:
          m_secondaryChooser.setAll();
          m_tertiaryChooser.setAll();
          m_endingChooser.setAll(Ending.TURN_AWAY, Ending.TURN_CLOSE);
      }
    });

    m_endingChooser.setAll(Ending.TURN_AWAY, Ending.TURN_CLOSE);
  }

  /**
   * @return an auton compiled from the starter, body, and ending {@link SendableChooser}
   */
  public static Command getAuto() {
    Starter starter = m_starterChooser.getSelected();
    Body body = m_bodyChooser.getSelected();
    Secondary secondary = m_secondaryChooser.getSelected();
    Tertiary tertiary = m_tertiaryChooser.getSelected();
    Ending ending = m_endingChooser.getSelected();

    // TODO: Move stop drivetrain run commands to subclasses
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