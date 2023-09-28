package frc.robot.util;

import java.util.function.DoubleSupplier;
import java.util.function.DoubleConsumer;

import java.util.ArrayList;

/** Represents a double that can be changed during runtime. */
public class TunableNumber implements DoubleSupplier, DoubleConsumer {
  /** List of instances. */
  private static final ArrayList<TunableNumber> s_instances = new ArrayList<TunableNumber>();
  /** Index of this instance for use with dashboards. Begins at zero. */
  public final int m_index;

  /** Helper variable, should be set to true if the number is visible on the dashboard. */
  public boolean visible = false;

  /** Descriptor of the TunableNumber for use with dashboards. */
  public final String m_name;

  /** Value of the TunableNumber. */
  private double m_value;

  /** True if the value of the number can be changed. */
  private boolean m_mutable = false;

  /** Function to run when the value is changed. */
  private DoubleConsumer m_bindOnChange;

  /**
   * Create a new TunableNumber.
   *
   * @param name descriptor for dashboards
   * @param val initial value
   */
  public TunableNumber(String name, double val) {
    m_name = name;
    m_value = val;
    synchronized (TunableNumber.class) {
      m_index = s_instances.size();
      s_instances.add(this);
    }
  }

  /** @return an array of all instances for class-wide changes (e.g. making all numbers mutable) */
  public static TunableNumber[] getAllInstances() {
    synchronized (TunableNumber.class) {
      return s_instances.toArray(new TunableNumber[s_instances.size()]);
    }
  }

  /** @return {@code true} if the number is mutable */
  public boolean getMutable() {
    return m_mutable;
  }

  /** @param mutable {@code true} if the number should be mutable */
  public void setMutable(boolean mutable) {
    m_mutable = mutable;
  }

  /**
   * Set a function to be called when the value is changed.
   *
   * @param bindOnChange {@link DoubleConsumer} to be called with the new value
   */
  public void bindTo(DoubleConsumer bindOnChange) {
    m_bindOnChange = bindOnChange;
  }

  /** @return the current value */
  @Override
  public double getAsDouble() {
    return m_value;
  }

  /**
   * Change the value. Only works if the number is mutable.
   *
   * @param val new value
   *
   * @see TunableNumber#getMutable
   * @see TunableNumber#setMutable
   */
  @Override
  public void accept(double val) {
    if (!m_mutable) {
      return;
    }

    m_value = val;
    if (m_bindOnChange != null) {
      m_bindOnChange.accept(m_value);
    }
  }
}