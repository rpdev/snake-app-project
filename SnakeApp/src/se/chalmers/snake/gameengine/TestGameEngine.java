package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.List;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.SystemEventIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public class TestGameEngine {

	public static XYPoint screenSize = new XYPoint(150, 200);
	public static MotionDetectorIC motionDetector = null;
	public static GameEngine gameEngine;

	private static ControlResourcesIC getControlResources() {
		return new ControlResourcesIC() {

			@Override
			public LevelDatabaseIC getLevelDatabase() {
				return new LevelDatabaseIC() {

					@Override
					public LevelIC getByLevel(int level) {
						return null;
					}

					@Override
					public LevelIC getByName(String name) {
						return new LevelIC() {

							@Override
							public String getLevelName() {
								return "Level 1";
							}

							@Override
							public String getLevelDescription() {
								return "This is level 1";
							}

							@Override
							public int getLevel() {
								return 1;
							}

							@Override
							public XYPoint getMapSize() {
								return new XYPoint(150, 200);
							}

							@Override
							public int getSnakeStartLength() {
								return 5;
							}

							@Override
							public XYPoint getSnakeHeadStartLocation() {
								return new XYPoint(25, 50);
							}

							@Override
							public double getStartAngle() {
								return Math.PI / 2;
							}

							@Override
							public List<REPoint> getObstacles() {
								ArrayList<REPoint> item = new ArrayList<REPoint>();
								item.add(new REPoint(REPoint.REType.WALL, 125, 175, 20));
								
								
								return item;
							}

							@Override
							public int getPlayerBodyWidth() {
								return 3;
							}

							@Override
							public int getItemsRadius() {
								return 10;
							}

							@Override
							public float getSpeed(List<Integer> collectTime) {
								return (float) 2.5;
							}

							@Override
							public boolean hasReachedGoal(List<Integer> collectTime) {
								return false;
							}

							@Override
							public int getAddItems(int totalCollected, int totalItemInGame) {
								if(totalCollected==0) {
								return 4;
								}
								return 1;
							}

							@Override
							public int getBodyGrowth(int collectTime, int totalCollected) {
									return 10;
							}
						};
					}

					@Override
					public String[] getLevelListByName() {
						return null;
					}

					@Override
					public int[] getLevelListByLevel() {
						return null;
					}
				};
			}

			@Override
			public MotionDetectorIC getMotionDetector() {
				return new MotionDetectorIC() {

					private double angle = Math.PI;

					@Override
					public void start() {
						
						if (TestGameEngine.motionDetector != null) {
							TestGameEngine.motionDetector.start();
						}
					}

					@Override
					public void stop() {
						if (TestGameEngine.motionDetector != null) {
							TestGameEngine.motionDetector.stop();
						}
					}

					@Override
					public void setReferenceSurface(ReferenceSurface rs) {
					}

					@Override
					public int getAngleByDegrees() {
						return 0;
					}

					@Override
					public double getAngleByRadians() {
						if (TestGameEngine.motionDetector != null) {
							double alge = TestGameEngine.motionDetector.getAngleByRadians();
							if(alge!=0.0) {
								return alge;
							}
						}
						angle -= 0.02;
						if(angle<0) angle += Math.PI*2;
						if(angle>Math.PI*2) angle -= Math.PI*2;
						
						return angle;
						//return angle;
					}

					@Override
					public void setSensitivity(int sensitivity) {
					}
				};
			}

			@Override
			public XYPoint getScreenSize() {
				return TestGameEngine.screenSize;
			}

			@Override
			public GameEngineIC getGameEngine() {
				return TestGameEngine.gameEngine;
			}

			public SystemEventIC getSystemEventController() {
				return new SystemEventIC() {

					public void systemInterrupt() {
					}
				};
			}
		};
	}

	public static GameEngineIC getGameEngine(XYPoint size, MotionDetectorIC motionDetector) {
		TestGameEngine.screenSize = size;
		TestGameEngine.motionDetector = motionDetector;
		TestGameEngine.gameEngine = new GameEngine(TestGameEngine.getControlResources());
		TestGameEngine.gameEngine.loadLevel("");
		return TestGameEngine.gameEngine;
	}
}
