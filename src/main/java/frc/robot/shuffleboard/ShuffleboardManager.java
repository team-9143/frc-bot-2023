package frc.robot.shuffleboard;

import java.util.ArrayList;

/** Initializes Shuffleboard */
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
    abstract void initialize();
  }

  public interface ShuffleboardChecklistBase extends ShuffleboardTabBase {
    abstract void reset();
  }

  private static final boolean m_debugging = true;

  private static final ArrayList<ShuffleboardTabBase> m_tabs = new ArrayList<ShuffleboardTabBase>();
  private static final ArrayList<ShuffleboardChecklistBase> m_checklists = new ArrayList<ShuffleboardChecklistBase>();

  private ShuffleboardManager() {
    m_tabs.add(new DriveTab());
    if (m_debugging) {
      m_tabs.add(new TestTab());
    }

    // TODO(low prio): Test checklists with toggle switches and reset
    m_checklists.add(new MatchChecklist());
    m_checklists.add(new PitChecklist());

    m_tabs.forEach(e -> e.initialize());
    m_checklists.forEach(e -> e.initialize());
  }

  public void reset() {
    m_checklists.forEach(e -> e.reset());
  }
}