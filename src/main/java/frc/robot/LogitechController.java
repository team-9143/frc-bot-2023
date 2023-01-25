package frc.robot;

import org.opencv.core.Point;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Logitech game controller wrapper class
 * 
 * @author Shuhao
 * 
 */
public class LogitechController {

	/**
	 * EventHandler.
	 * 
	 * @author shuhao
	 * 
	 */
	public static class EventHandler {
		/**
		 * Handles when button is first pressed
		 */
		public void buttonDown() {

		}

		/**
		 * Handles when button is released
		 */
		public void buttonUp() {

		}

		/**
		 * Handles when button is held down.
		 */
		public void buttonHeld() {

		}
	}

	public static final int BTN_A = 1;
	public static final int BTN_B = 2;
	public static final int BTN_X = 3;
	public static final int BTN_Y = 4;
	public static final int BTN_LB = 5;
	public static final int BTN_RB = 6;
	public static final int BTN_BACK = 7;
	public static final int BTN_START = 8;
	public static final int BTN_LEFT_JOYSTICK = 9;
	public static final int BTN_RIGHT_JOYSTICK = 10;

	/**
	 * The Joystick in the back. Done like this so I don't have to override the
	 * constructors.
	 * 
	 * TODO: Override the constructor and extend from Joystick
	 */
	public Joystick joystick;

	public LogitechController(Joystick j) {
		joystick = j;
	}

	/**
	 * Gets the trigger, which is left and right trigger. Left trigger is
	 * negative and right trigger is positive. They range from 0 - 1 (absolute
	 * values) and additive
	 * 
	 * @return The trigger value.
	 */
	public double getTrigger() {
		return joystick.getRawAxis(3);
	}

	
	// Axis 3 is the RT and LT.. but they're on the same Axis...

	public Point getLeftStick() {
		Point p = new Point(joystick.getRawAxis(1), joystick.getRawAxis(0));
		return p;
	}

	public Point getRightStick() {
		Point p = new Point(joystick.getRawAxis(3), joystick.getRawAxis(4));
		return p;
	}

	public boolean getButton(int button) {
		return joystick.getRawButton(button);
	}

}