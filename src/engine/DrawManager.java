package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import entity.*;
import screen.Screen;
import skill.*;

/**
 * Manages screen drawing.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class DrawManager {

	/** Singleton instance of the class. */
	private static DrawManager instance;
	/** Current frame. */
	private static Frame frame;
	/** FileManager instance. */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	private static Graphics backBufferGraphics;
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Normal sized font. */
	private static Font fontRegular;
	/** Normal sized font properties. */
	private static FontMetrics fontRegularMetrics;
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	private static Font fontSmall;

	/** Sprite types mapped to their images. */
	private static Map<SpriteType, int[][]> spriteMap;

	/** Sprite types. */
	public static enum SpriteType {
		/** Player ship. */
		Ship,
		/** Destroyed player ship. */
		ShipDestroyed,
		/** Player bullet. */
		Bullet,
		/** Enemy bullet. */
		EnemyBullet,
		/** First enemy ship - first form. */
		EnemyShipA1,
		/** First enemy ship - second form. */
		EnemyShipA2,
		/** Second enemy ship - first form. */
		EnemyShipB1,
		/** Second enemy ship - second form. */
		EnemyShipB2,
		/** Third enemy ship - first form. */
		EnemyShipC1,
		/** Third enemy ship - second form. */
		EnemyShipC2,
		/** Bonus ship. */
		EnemyShipSpecial,
		/** Destroyed enemy ship. */
		Explosion,
		//추가한것.
		Box, Boom,
		/** test.*/
		NewShipDesign,
		// 두번피격적 추가.
		EnemyShipD1,
		EnemyShipD2,
		EnemyShipD3,
		EnemyShipD4,
		Skill1,
		Skill2,
		Skill3,
		Skill4,
		LargeBoom
	};

	/**
	 * Private constructor.
	 */
	private DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			spriteMap = new LinkedHashMap<SpriteType, int[][]>();
			// 각각의 이름그대로, 배, 배파괴되어을때, 총알등등이있음.
			spriteMap.put(SpriteType.Ship, new int[13][8]);
			spriteMap.put(SpriteType.ShipDestroyed, new int[13][8]);
			spriteMap.put(SpriteType.Bullet, new int[5][5]);
			spriteMap.put(SpriteType.EnemyBullet, new int[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial, new int[16][7]);
			spriteMap.put(SpriteType.Explosion, new int[13][7]);
			spriteMap.put(SpriteType.Box, new int[13][8]);
			spriteMap.put(SpriteType.Boom, new int[8][8]);
			spriteMap.put(SpriteType.NewShipDesign, new int[13][8]);
			spriteMap.put(SpriteType.EnemyShipD1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipD2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipD3, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipD4, new int[12][8]);
			spriteMap.put(SpriteType.Skill1, new int[8][8]);
			spriteMap.put(SpriteType.Skill2, new int[8][8]);
			spriteMap.put(SpriteType.Skill3, new int[8][8]);
			spriteMap.put(SpriteType.Skill4, new int[8][8]);
			spriteMap.put(SpriteType.LargeBoom, new int[100][100]);


			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			// Font loading.
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			fontSmall = fileManager.loadFont(10f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	/**
	 * Returns shared instance of DrawManager.
	 * 
	 * @return Shared instance of DrawManager.
	 */
	protected static DrawManager getInstance() {
		if (instance == null)
			instance = new DrawManager();
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 * 
	 * @param currentFrame
	 *            Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

	}

	/**
	 * Draws the completed drawing on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 * 
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public void drawEntity(final Entity entity, final int positionX,
						   final int positionY) {
		int[][] image = spriteMap.get(entity.getSpriteType());

		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j] != 0){
					if(image[i][j] == 1) backBufferGraphics.setColor(Color.white);
					else if(image[i][j] == 2) backBufferGraphics.setColor(Color.red);
					else if(image[i][j] == 3) backBufferGraphics.setColor(Color.blue);
					else if(image[i][j] == 4) backBufferGraphics.setColor(Color.green);
					else if(image[i][j] == 5) backBufferGraphics.setColor(Color.yellow);
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);
				}
	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Draws current score on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Current score.
	 */
	public void drawScore(final Screen screen, final int score) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score);
		backBufferGraphics.drawString(scoreString, screen.getWidth() - 60, 25);
	}

	/**
	 * Draws number of remaining lives on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param lives
	 *            Current lives.
	 */
	public void drawLives(final Screen screen, final int lives, SpriteType lifeShape) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(lives), 20, 25);
		Ship dummyShip = new Ship(0, 0, lifeShape);
		for (int i = 0; i < lives; i++)
			drawEntity(dummyShip, 40 + 35 * i, 10);
	}

	/**
	 * Draws a thick line from side to side of the screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param positionY
	 *            Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	public void drawBooms(final Screen screen , final int boomtimes){
		Boom dummyBoom = new Boom(0,0,0,0);
		for (int i = 0; i < boomtimes; i++)
			drawEntity(dummyBoom,150+ 20 * i,10);
	}

	//stageLevel에 따른 스킬해제. cursor의 위치에 따른 효과
	public void drawSkills(final int cursor, Skill1 skill1, Skill2 skill2, Skill3 skill3 , Skill4 skill4,long pauseTime){
		int y = 36;
		int sizePlus = 72; //화면이 늘어남에 따라
		drawString("SKILL",213 +sizePlus ,25);
		backBufferGraphics.setFont(fontSmall);
		if (skill1.checkOpen()) { //열려있으면 그려줌.
			drawEntity(skill1,267 + 22 * 0 +sizePlus, 10);
			if(pauseTime == 0) {
				if (skill1.returnSkillCoolTime() > 0 && skill1.returnSkillCoolTime() < 10) {
					backBufferGraphics.drawString(Integer.toString(skill1.returnSkillCoolTime()), 267 + 22 * 0 + 4 + sizePlus, y);
				} else if (skill1.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill1.returnSkillCoolTime()), 267 + 22 * 0 + 3 + sizePlus, y);
				}
			}
		}
		if (skill2.checkOpen()) {
			drawEntity(skill2, 267 + 22 * 1 + sizePlus, 10);
			if(pauseTime == 0) {
				if (skill2.returnSkillCoolTime() > 0 && skill2.returnSkillCoolTime() < 10)
					backBufferGraphics.drawString(Integer.toString(skill2.returnSkillCoolTime()), 270 + 22 * 1 + 3 + sizePlus, y);
				else if (skill2.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill2.returnSkillCoolTime()), 267 + 22 * 1 + 3 + sizePlus, y);
				}
			}
		}
		if (skill3.checkOpen()) {
			drawEntity(skill3, 267 + 22 * 2 + sizePlus, 10);
			if(pauseTime == 0) {
				if (skill3.returnSkillCoolTime() > 0 && skill3.returnSkillCoolTime() < 10)
					backBufferGraphics.drawString(Integer.toString(skill3.returnSkillCoolTime()), 270 + 22 * 2 + 3 + sizePlus, y);
				else if (skill3.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill3.returnSkillCoolTime()), 267 + 22 * 2 + 3 + sizePlus, y);
				}
			}
		}
		if (skill4.checkOpen()) {
			drawEntity(skill4, 267 + 22 * 3 + sizePlus, 10);
			if(pauseTime == 0) {
				if (skill4.returnSkillCoolTime() > 0 && skill4.returnSkillCoolTime() < 10)
					backBufferGraphics.drawString(Integer.toString(skill4.returnSkillCoolTime()), 270 + 22 * 3 + 3 + sizePlus, y);
				else if (skill4.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill4.returnSkillCoolTime()), 267 + 22 * 3 + 3 + sizePlus, y);
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			if( i == cursor){
				backBufferGraphics.setColor(Color.green);
			}
			else {
				backBufferGraphics.setColor(Color.gray);
			}
			backBufferGraphics.drawRect(267 + 22 * i + sizePlus, 10, skill1.getWidth(), skill1.getHeight());
		}
	}
	//필살기 인터페이스
	public void drawLargeBoom(final int largeBoomtimes){
		if(largeBoomtimes == 1) backBufferGraphics.setColor(Color.green);
		else backBufferGraphics.setColor(Color.gray);
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.drawString("Large", 230,20);
		backBufferGraphics.drawString(" Boom!", 230,31);
	}

	/**
	 * Draws game title.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawTitle(final Screen screen) {
		String titleString = "Invaders";
		String instructionsString =
				"select with w+s / arrows, confirm with space";

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 2);

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 3);
	}

	/**
	 * Draws main menu.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Option selected.
	 */
	public void drawMenu(final Screen screen, final int option) {
		String playString = "Play";
		String highScoresString = "High scores";
		String exitString = "exit";
		String loadString = "Load";
		String customizeString = "Custom";

		if (option == 2)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, playString, screen.getHeight()
				/ 3 * 2 - fontRegularMetrics.getHeight() * 2);
		// load 메뉴 추가.
		if (option == 8)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, loadString, screen.getHeight()
				/ 3 * 2 );
		if (option == 3)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, highScoresString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == 9)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, customizeString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 4);
		if (option == 0)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, exitString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 6);
		}

	/**
	 * Draws game results.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Score obtained.
	 * @param livesRemaining
	 *            Lives remaining when finished.
	 * @param shipsDestroyed
	 *            Total ships destroyed.
	 * @param accuracy
	 *            Total accuracy.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawResults(final Screen screen, final int score,
			final int livesRemaining, final int shipsDestroyed,
			final float accuracy, final boolean isNewRecord) {
		String scoreString = String.format("score %04d", score);
		String livesRemainingString = "lives remaining " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
		String accuracyString = String
				.format("accuracy %.2f%%", accuracy * 100);

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, scoreString, screen.getHeight()
				/ height);
		drawCenteredRegularString(screen, livesRemainingString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 2);
		drawCenteredRegularString(screen, shipsDestroyedString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 4);
		drawCenteredRegularString(screen, accuracyString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 6);
	}

	/**
	 * Draws interactive characters for name input.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param name
	 *            Current name selected.
	 * @param nameCharSelected
	 *            Current character selected for modification.
	 */
	public void drawNameInput(final Screen screen, final char[] name,
			final int nameCharSelected) {
		String newRecordString = "New Record!";
		String introduceNameString = "Introduce name:";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, newRecordString, screen.getHeight()
				/ 4 + fontRegularMetrics.getHeight() * 10);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, introduceNameString,
				screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

		// 3 letters name.
		int positionX = screen.getWidth()
				/ 2
				- (fontRegularMetrics.getWidths()[name[0]]
						+ fontRegularMetrics.getWidths()[name[1]]
						+ fontRegularMetrics.getWidths()[name[2]]
								+ fontRegularMetrics.getWidths()[' ']) / 2;

		for (int i = 0; i < 3; i++) {
			if (i == nameCharSelected)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.WHITE);

			positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
			positionX = i == 0 ? positionX
					: positionX
							+ (fontRegularMetrics.getWidths()[name[i - 1]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			backBufferGraphics.drawString(Character.toString(name[i]),
					positionX,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight()
							* 14);
		}
	}

	/**
	 * Draws basic content of game over screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param acceptsInput
	 *            If the screen accepts input.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawGameOver(final Screen screen, final boolean acceptsInput,
			final boolean isNewRecord) {
		String gameOverString = "Game Over";
		String continueOrExitString =
				"Press Space to play again, Escape to exit";

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, gameOverString, screen.getHeight()
				/ height - fontBigMetrics.getHeight() * 2);

		if (acceptsInput)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, continueOrExitString,
				screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws high score screen title and instructions.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawHighScoreMenu(final Screen screen) {
		String highScoreString = "High Scores";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws high scores.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param highScores
	 *            List of high scores.
	 */
	public void drawHighScores(final Screen screen,
			final List<Score> highScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		String scoreString = "";

		for (Score score : highScores) {
			scoreString = String.format("%s        %04d", score.getName(),
					score.getScore());
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
		}
	}

	/**
	 * Draws a centered string on regular font.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
			final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
			final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}
	//정한위치에 string을 적어줌.
	public void drawString(final String string, final int x,final int y){
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.yellow);
		backBufferGraphics.drawString(string,x,y);
	}
	//작은글씨 그리기, 스킬발동로그용
	public void drawSmallString(final String string, final int x, final int y){
		backBufferGraphics.setColor(Color.yellow);
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.drawString(string,x,y);
	}

	/**
	 * Countdown to game start.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param level
	 *            Game difficulty level.
	 * @param number
	 *            Countdown number.
	 * @param bonusLife
	 *            Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
			final int number, final boolean bonusLife) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4)
			if(level == 2 || level == 3 ||level == 4 || level ==5) {
				drawCenteredBigString(screen, "Level " + level + " NEW SKILL!",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			}
			else if (!bonusLife) {
					drawCenteredBigString(screen, "Level " + level,
							screen.getHeight() / 2
									+ fontBigMetrics.getHeight() / 3);
				}
			else {
					drawCenteredBigString(screen, "Level " + level
									+ " - Bonus life!",
							screen.getHeight() / 2
									+ fontBigMetrics.getHeight() / 3);
			}
		else if (number != 0)
			drawCenteredBigString(screen, Integer.toString(number),
					screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		else
			drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
					+ fontBigMetrics.getHeight() / 3);
	}


	// 추가한 부분 : 일시정지 화면 타이틀 구성.
	public void drawPauseTitle(final Screen screen) {
		String pauseString = "Pause";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, pauseString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}
	// 추가한 부분 : 일시정지 화면 메뉴 구성.
	public void drawPauseMenu(final Screen screen, final int option) {
		String restartString = "Restart";
		String saveString = "Save";
		String musicString = "Music";
		String mainString = "Main";
		String continueString = "Continue";
		String soundString = "Sound";

		if (option == 2)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, continueString,
				screen.getHeight() / 3 * 1);
		if (option == 1)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, mainString,
				screen.getHeight() / 3 * 1 + fontRegularMetrics.getHeight() * 2);

		if (option == 4)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, restartString,
				screen.getHeight() / 3 * 1 + fontRegularMetrics.getHeight() * 4);
		if (option == 5)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, saveString, screen.getHeight()
				/ 3 * 1 + fontRegularMetrics.getHeight() * 6);
		if (option == 6)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, musicString, screen.getHeight() / 3
				* 1 + fontRegularMetrics.getHeight() * 8);

		if (option == 11)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString("<", screen.getWidth() / 2
						- fontBigMetrics.stringWidth(musicString) / 2 - 5,
				screen.getHeight() / 3
						* 1 + fontRegularMetrics.getHeight() * 8);
		if (option == 12)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(">", screen.getWidth() / 2
						+ fontBigMetrics.stringWidth(musicString) / 2 - 5,
				screen.getHeight() / 3
						* 1 + fontRegularMetrics.getHeight() * 8);

		if (option == 15)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, soundString, screen.getHeight() / 3
				* 1 + fontRegularMetrics.getHeight() * 10);

		if (option == 13)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString("<", screen.getWidth() / 2
						- fontBigMetrics.stringWidth(soundString) / 2 - 5,
				screen.getHeight() / 3
						* 1 + fontRegularMetrics.getHeight() * 10);
		if (option == 14)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(">", screen.getWidth() / 2
						+ fontBigMetrics.stringWidth(soundString) / 2 - 5,
				screen.getHeight() / 3
						* 1 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws Ship screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawShipCustomMenu(final Screen screen) {
		String customString = "Customize";
		String instructionsString = "Press Space to return";
		String currentString = "Current";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, customString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, currentString,
				screen.getHeight() / 3);

	}
	/**
	 * Draws sprites given as list and cursor.
	 *
	 * @param screen
	 * 				Screen to draw on.
	 * @param list
	 * 				List of sprites to draw.
	 * @param option
	 * 				Cursor index.
	 */
	public void drawDesigns(final Screen screen, final ArrayList<SpriteType> list, int option, DesignSetting designSetting){
		int count = 0;
		int positionX = 40;
		int j = 0, positionY = screen.getWidth()-60;
		int cursorX = 0;
		int cursorY = 0;
		int margin = 10;

		// 두 줄에 다 안들어오는 경우는 고려하지 못함.
		for(SpriteType sprite: list){
			if( positionX + spriteMap.get(sprite).length*2 + margin >= frame.getWidth() ){
				j++;
				positionX = 40;
				positionY = positionY + 40*j;
			}
			if(count == option){
				cursorX = positionX;
				cursorY = positionY;
			}
			Ship dummyShip = new Ship(0, 0, sprite);
			drawEntity(dummyShip, positionX, positionY);
			if(designSetting.getShipType() == sprite){
				drawEntity(dummyShip, 200, screen.getHeight() / 3 + 20);
			}
			count++;
			positionX += spriteMap.get(sprite).length*2 + margin;
		}
		backBufferGraphics.setColor(Color.RED);
		drawTriangle(cursorX+12, cursorY-6, true);

	}


	/**
	 * Draws triangle which has a horizontal side.
	 *
	 * @param tipPositionX
	 * 					X-position of tip.
	 * @param tipPositionY
	 * 					Y-position of tip.
	 */
	private void drawTriangle(final int tipPositionX, final int tipPositionY){
		drawTriangle(tipPositionX, tipPositionY, false);
	}

	/**
	 * Draws triangle which has a horizontal side.
	 *
	 * @param tipPositionX
	 * 					X-position of tip
	 * @param tipPositionY
	 * 					Y-position of tip
	 * @param reverse
	 * 				Is Tip's direction reversed? (false: up, true: down)
	 */
	private void drawTriangle(final int tipPositionX, final int tipPositionY, boolean reverse){
		// size of triangle.
		int size = 6;

		int[] x = {tipPositionX, tipPositionX+size, tipPositionX-size};
		int[] y = {tipPositionY, tipPositionY+size*2, tipPositionY+size*2};

		if(reverse) {
			y[1] -= size*4;
			y[2] -= size*4;
		}
		backBufferGraphics.drawPolygon(x, y, 3);		// 3점의 x좌표와 y좌표를 전달해서 삼각형을 그리는 함수
	}
}
