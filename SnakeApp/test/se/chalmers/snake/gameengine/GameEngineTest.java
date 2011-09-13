
package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.MotionDetectorIC.ReferenceSurface;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

public class GameEngineTest {

	public GameEngineTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	private ControlResourcesIC getControlResources() {
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
								return  new ArrayList<REPoint>(1);
							}

							@Override
							public int getPlayerBodyWidth() {
								return 5;
							}

							@Override
							public int getItemsRadius() {
								return 5;
							}

							@Override
							public int getSpeed(List<Integer> collectTime) {
								return 2;
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
						return 1.0;
					}

					@Override
					public void setSensitivity(int sensitivity) {
					}
				};
			}

			@Override
			public Object getSystemEventController() {
				return null;
			}

			@Override
			public XYPoint getScreenSize() {
				return new XYPoint(100, 100);
			}

			@Override
			public GameEngineIC getGameEngine() {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}

	@Test
	public void testRunGame() {
		GameEngine gameEngine = new GameEngine(this.getControlResources());
		gameEngine.loadLevel("Level 1");
		
		
		gameEngine.startGame();
		try {
			Thread.sleep(100000);
		} catch (Exception ex) {
		}
		gameEngine.pauseGame();
		
	}
}
