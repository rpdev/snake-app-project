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
	public static XYPoint screenSize = new XYPoint(150,200);
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
								return new XYPoint(100, 100);
							}

							@Override
							public int getSnakeStartLength() {
								return 5;
							}

							@Override
							public XYPoint getSnakeHeadStartLocation() {
								return new XYPoint(50, 50);
							}

							@Override
							public double getStartAngle() {
								return 0;
							}

							@Override
							public List<REPoint> getObstacles() {
								return new ArrayList<REPoint>();
							}

							@Override
							public int getPlayerBodyWidth() {
								return 2;
							}

							@Override
							public int getItemsRadius() {
								return 5;
							}

							@Override
							public int getSpeed(List<Integer> collectTime) {
								return 3;
							}

							@Override
							public boolean hasReachedGoal(List<Integer> collectTime) {
								return false;
							}

							@Override
							public int getAddItems(int totalCollected, int totalItemInGame) {
								return 1;
							}

							@Override
							public int getBodyGrowth(int collectTime, int totalCollected) {
								return 1;
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

					@Override
					public void start() {
					}

					@Override
					public void stop() {
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
						return 1.5;
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
				throw new UnsupportedOperationException("Not supported yet.");
			}

			public SystemEventIC getSystemEventController() {
				return new SystemEventIC() {
					public void systemInterrupt() {
						
					}
				};
			}
		};
	}
	
	public static GameEngineIC getGameEngine(XYPoint size) {
		TestGameEngine.screenSize = size;
		GameEngine gameEngine = new GameEngine(TestGameEngine.getControlResources());
		gameEngine.loadLevel("");
		return gameEngine;
	}
	
}
