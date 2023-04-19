package frc.robot.shuffleboard;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import edu.wpi.first.networktables.GenericEntry;

import frc.robot.autos.AutoSelector;

// TODO: Map sendables to their subsystems
/** Controls and initializes Shuffleboard tabs. */
public class ShuffleboardManager {
  private static ShuffleboardManager m_instance;

  /** @return the singleton instance */
  public static synchronized ShuffleboardManager getInstance() {
    if (m_instance == null) {
      m_instance = new ShuffleboardManager();
    }
    return m_instance;
  }

  public interface ShuffleboardTabBase {
    /** Creates all widgets. */
    abstract void initialize();
  }

  public interface ShuffleboardChecklistBase extends ShuffleboardTabBase {
    /** Resets all checklist objects to false. */
    abstract void reset();

    /**
     * Adds a checklist to a layout with toggle switches.
     *
     * @param checklist array of items to add
     * @param layout layout to add items to
     * @return an {@link ArrayList} of {@link GenericEntry} corresponding to the checklist
     */
    default ArrayList<GenericEntry> addChecklist(String[] checklist, ShuffleboardLayout layout) {
      ArrayList<GenericEntry> entries = new ArrayList<GenericEntry>();
      for (String e : checklist) {
        entries.add(layout.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
      }
      return entries;
    }
  }

  /** For testing while manipulating the robot's sensors. If used, {@link frc.robot.subsystems.IntakeTilt IntakeTilt}.getPosition(), {@link frc.robot.subsystems.Drivetrain Drivetrain}.getPosition() and resetEncoders(), and {@link frc.robot.OI OI.pigeon}.getYaw(), setYaw(), and getPitch() must be overriden with editable data through NetworkTables. */
  public static final boolean m_simulation = true;

  private static final ArrayList<ShuffleboardTabBase> m_tabs = new ArrayList<ShuffleboardTabBase>();
  private static final ArrayList<ShuffleboardChecklistBase> m_checklists = new ArrayList<ShuffleboardChecklistBase>();

  private ShuffleboardManager() {
    m_tabs.add(new DriveTab());
    if (m_simulation) {
      m_tabs.add(new SimulationTab());
    } else {
      m_tabs.add(new TestTab());
    }

    m_checklists.add(new MatchChecklist());
    m_checklists.add(new PitChecklist());
    m_tabs.addAll(m_checklists);

    m_tabs.forEach(e -> e.initialize());
  }

  public void reset() {
    m_checklists.forEach(e -> e.reset());
  }

  /** Shuffleboard entry. Should be true if a cube is preloaded. */
  protected static GenericEntry cubeLoaded;

  /** Shuffleboard entry. Should be false if any chooser needs to be updated. */
  protected static GenericEntry choosersSynced;

  public boolean getCubePreloaded() {
    return cubeLoaded.getBoolean(true);
  }

  public void updateChoosersSynced() {
    choosersSynced.setBoolean(
      !(AutoSelector.m_starterChooser.isUpdateReq() ||
      AutoSelector.m_bodyChooser.isUpdateReq() ||
      AutoSelector.m_secondaryChooser.isUpdateReq() ||
      AutoSelector.m_tertiaryChooser.isUpdateReq() ||
      AutoSelector.m_endingChooser.isUpdateReq())
    );
  }
}