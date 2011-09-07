package se.chalmers.snake.interfaces;



/**
 * The Interface for get the angle from the MotionDetector.
 */
public interface MotionDetectorIC {
	
	/**
	 * Use for set a ReferensceSurface to be the zero point. 
	 */
	public enum ReferenceSurface{
		/**
		 * This will return the angle to which correspond to the highest point
		 * in the flat surface. That the top of the screen will be the zero point.
		 */
		FLAT_TOP
	}
	
	/**
	 * Set the Reference Surface to be use
	 * @param rs 
	 */
	public void setReferenceSurface(ReferenceSurface rs);
	
	/**
	 * Get the angle the MotionDetector as get from the System.
	 * @return Return 0 to 359 or -1 if no angle are detect or is lower that sensitivity level.
	 */
	public int getAngleByDegrees();
	/**
	 * Get the angle the MotionDetector as get from the System.
	 * @return Return 0 to 2*PI or -1 if no angle are detect or is lower that sensitivity level.
	 */
	public double getAngleByRadians();
	
	
	/**
	 * Set the sensitivity of the MotionDetector
	 * If 0, all motion is detect, if 100 none motions is detect over the sensitivity level.
	 * @param sensitivity 0 to 100.
	 */
	public void setSensitivity(int sensitivity);
	
}