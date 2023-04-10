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

  private static final boolean m_debugging = false;

  private static final ArrayList<ShuffleboardTabBase> m_tabs = new ArrayList<>();

  private ShuffleboardManager() {
    m_tabs.add(null);
    if (m_debugging) {
      m_tabs.add(null);
    }

    m_tabs.forEach(e -> e.initialize());
  }
}