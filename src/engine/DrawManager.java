package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.logging.Logger;

import entity.*;
import screen.PauseScreen;
import screen.Screen;
import skill.*;

import static engine.Core.*;
import static screen.PauseScreen.*;

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
	/** Image for game screen. */
	private static Image backgroundImage;
	/** Template Image of background*/
	private static Image templateImage;
	/** Small sized font properties */
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
		Bullet1,
		Bullet2,
		Bullet3,
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
		/** cooltime reduction */
		ShootingCoolItem,
		/** increase bullet speed*/
		BulletSpeedItem,
		/** extra skill - bomb */
		Boom,
		/** test ship design */
		NewShipDesign,
		/** Fourth enemy ship (not attacked) - first form */
		EnemyShipD1,
		/** Fourth enemy ship (not attacked) - second form */
		EnemyShipD2,
		/** Fourth enemy ship (attacked) - first form */
		EnemyShipD3,
		/** Fourth enemy ship (attacked) - second form*/
		EnemyShipD4,
		/** shield skill */
		Skill1,
		/** stun skill */
		Skill2,
		/** slowing bullet skill */
		Skill3,
		/** 3 bomb firing skill */
		Skill4,
		/** Bonus life item */
		BonusLifeItem,
		/** Bonus score item - first */
		BonusScoreItem1,
		/** Bonus score item - second */
		BonusScoreItem2,
		/** Bonus score item - third */
		BonusScoreItem3,
		/** Ultimate */
		Ultimate
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
			spriteMap.put(SpriteType.Ship, new int[13][8]);
			spriteMap.put(SpriteType.ShipDestroyed, new int[13][8]);
			spriteMap.put(SpriteType.Bullet1, new int[5][5]);
			spriteMap.put(SpriteType.Bullet2, new int[5][5]);
			spriteMap.put(SpriteType.Bullet3, new int[5][5]);
			spriteMap.put(SpriteType.EnemyBullet, new int[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new int[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial, new int[16][7]);
			spriteMap.put(SpriteType.Explosion, new int[13][7]);
			spriteMap.put(SpriteType.ShootingCoolItem, new int[13][8]);
			spriteMap.put(SpriteType.BulletSpeedItem, new int[13][8]);
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
			spriteMap.put(SpriteType.Ultimate, new int[100][100]);
			spriteMap.put(SpriteType.BonusLifeItem, new int[8][8]);
			spriteMap.put(SpriteType.BonusScoreItem1, new int[5][5]);
			spriteMap.put(SpriteType.BonusScoreItem2, new int[5][5]);
			spriteMap.put(SpriteType.BonusScoreItem3, new int[5][5]);


			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			templateImage = fileManager.loadBackgroundTemplate();
			logger.info("Finished loading the template image.");

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

		if(backgroundImage == null){
			backgroundImage = makeBackgroundImage();
		}

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
	 * Draws an entity, using the appropriate image.
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
	 * Draws a shadowed entity, using the appropriate image.
	 * @param entity Entity to be drawn.
	 * @param positionX Coordinates for the left side of the image.
	 * @param positionY Coordinates for the upper side of the image.
	 */
	public void drawShadowedEntity(final Entity entity, final int positionX,
								   final int positionY) {
		int[][] image = spriteMap.get(entity.getSpriteType());

		backBufferGraphics.setColor(Color.GRAY);
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j] != 0)
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);

	}

	/**
	 * For debugging purposes, draws the canvas borders.
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
	 * For debugging purposes, draws a grid over the canvas.
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
	 * make background image fit to frame size.
	 *
	 * @return adjusted Image
	 */
	private Image makeBackgroundImage(){
		// 원래 이미지에서 높이를 늘린 이미지 생성
		BufferedImage bufferedImage = new BufferedImage(templateImage.getWidth(null),
				templateImage.getHeight(null) + frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics graphics = bufferedImage.getGraphics();

		int stretched = bufferedImage.getHeight() - templateImage.getHeight(null);

		// 원래 이미지를 밑에서부터 그리기
		graphics.drawImage(templateImage, 0, stretched,
				bufferedImage.getWidth(), templateImage.getHeight(null), null);
		// 중복될 이미지를 잘라서 그리기
		graphics.drawImage(templateImage, 0, 0, bufferedImage.getWidth(), stretched,
				0, templateImage.getHeight(null) - stretched,
				bufferedImage.getWidth(), templateImage.getHeight(null), null);

		graphics.dispose();
		fileManager.saveImage(bufferedImage);
		return bufferedImage;
	}

	/**
	 * @param screen
	 * 			Screen to draw in.
	 * @param imgPos
	 * 			position of background image.
	 * @return true if image position meets limit, false otherwise.
	 */
	public boolean drawFlowBackground(final Screen screen, int imgPos) {
		int imageHeight = backgroundImage.getHeight(null);
		boolean isExceeded = false;
		if(imageHeight - screen.getHeight()  - imgPos<=0) {
			imgPos = 0;
			isExceeded = true;
		}

		// 그림 아래부터 그려서 올라가는 방향으로 동작.
		backBufferGraphics.drawImage(backgroundImage, 0, 0, screen.getWidth(), screen.getHeight(),
				0, imageHeight - screen.getHeight() - imgPos,
				screen.getWidth(), imageHeight - imgPos, null);
		return isExceeded;
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

	/**
	 * Draw about the player's bomb
	 * @param screen Screen to draw on.
	 * @param boomtimes value of current boom
	 */
	public void drawBooms(final Screen screen , final int boomtimes){
		Boom dummyBoom = new Boom(0,0,0,0);
		for (int i = 0; i < boomtimes; i++)
			drawEntity(dummyBoom,150+ 20 * i,10);
	}


	/**
	 * Draws a remaining time of bonus stage
	 * @param screen Screen to draw on.
	 * @param cooldown current cooldown
	 * @param pauseTime value of pause time
	 */
	public void drawBonusTime(final Screen screen , final Cooldown cooldown, long pauseTime){
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.setColor(Color.green);
		int cool = cooldown.getDuration() - cooldown.passedCooldown();
		if (pauseTime == 0){
			if (cool > 0 && cool < 10) {
				backBufferGraphics.drawString(Integer.toString(cool), screen.getWidth()/2-10 , 80);
			} else if (cool > 9) {
				backBufferGraphics.drawString(Integer.toString(cool), screen.getWidth()/2-20 , 80);
			}
		}
	}


	/**
	 * Draw a skill bar
	 * @param cursor current cursor
	 * @param skill1 shield skill
	 * @param skill2 stun skill
	 * @param skill3 slow enemy bullet skill
	 * @param skill4 3 bomb fire skill
	 * @param pauseTime value of pause time
	 */
	public void drawSkills(final int cursor, Skill1 skill1, Skill2 skill2, Skill3 skill3 , Skill4 skill4,long pauseTime){
		int y = 36;
		int sizePlus = 72; //화면이 늘어남에 따라
		drawString("SKILL",213 +sizePlus ,25);
		backBufferGraphics.setFont(fontSmall);
		if (skill1.checkOpen()) { //열려있으면 그려줌.
			drawEntity(skill1,267 + sizePlus, 10);
			if(pauseTime == 0) {
				if (skill1.returnSkillCoolTime() > 0 && skill1.returnSkillCoolTime() < 10) {
					backBufferGraphics.drawString(Integer.toString(skill1.returnSkillCoolTime()), 267 + 4 + sizePlus, y);
				} else if (skill1.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill1.returnSkillCoolTime()), 267 + 3 + sizePlus, y);
				}
			}
		}
		if (skill2.checkOpen()) {
			drawEntity(skill2, 267 + 22 + sizePlus, 10);
			if(pauseTime == 0) {
				if (skill2.returnSkillCoolTime() > 0 && skill2.returnSkillCoolTime() < 10)
					backBufferGraphics.drawString(Integer.toString(skill2.returnSkillCoolTime()), 270 + 22 + 3 + sizePlus, y);
				else if (skill2.returnSkillCoolTime() > 9) {
					backBufferGraphics.drawString(Integer.toString(skill2.returnSkillCoolTime()), 267 + 22 + 3 + sizePlus, y);
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

	/**
	 * Ultimate skill interface
	 * @param UltimateTimes value of ultimate time
	 */
	public void drawUltimate(final int UltimateTimes){
		if(UltimateTimes == 1) backBufferGraphics.setColor(Color.green);
		else backBufferGraphics.setColor(Color.gray);
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.drawString("Ultimate", 215,25);
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

		if (option == PLAY)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, playString, screen.getHeight()
				/ 3 * 2 - fontRegularMetrics.getHeight() * 2);
		// load 메뉴 추가.
		if (option == LOAD)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, loadString, screen.getHeight()
				/ 3 * 2 );
		if (option == HIGH_SCORES)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, highScoresString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == CUSTOM)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, customizeString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 4);
		if (option == EXIT)
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
	// 정한 위치에 string을 적어줌.
	public void drawString(final String string, final int x,final int y){
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.yellow);
		backBufferGraphics.drawString(string,x,y);
	}
	// 작은 글씨 그리기, 스킬 발동 로그용
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
			// 일단 임시로 2단계를 보너스 스테이지로 정함.
			if(level == 2) {
				drawCenteredBigString(screen, "Level " + level + " BONUS STAGE!",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			}
			else if(level == 2 || level == 3 ||level == 4 || level ==5) {
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


	/**
	 * Draws Pause Screen title
	 * @param screen screen to draw on
	 */
	public void drawPauseTitle(final Screen screen) {
		String pauseString = "Pause";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, pauseString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws Pause menu
	 * @param screen Screen to draw on.
	 * @param option value of current option
	 */
	public void drawPauseMenu(final Screen screen, final int option) {
		String restartString = "Restart";
		String saveString = "Save";
		String musicString = "Music";
		String mainString = "Main";
		String continueString = "Continue";
		String soundString = "Sound";

		if (option == CONTINUE)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, continueString,
				screen.getHeight() / 3);
		if (option == PauseScreen.MAIN_MENU)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, mainString,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 2);

		if (option == PauseScreen.RESTART)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, restartString,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 4);
		if (option == SAVE)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, saveString,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 6);
		if (option == MUSIC)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, musicString,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 8);

		if (option == MUSIC_DOWN)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString("<", screen.getWidth() / 2
						- fontBigMetrics.stringWidth(musicString) / 2 - 5,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 8);
		if (option == MUSIC_UP)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(">", screen.getWidth() / 2
						+ fontBigMetrics.stringWidth(musicString) / 2 - 5,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 8);

		if (option == SOUND)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, soundString,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 10);

		if (option == SOUND_DOWN)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString("<", screen.getWidth() / 2
						- fontBigMetrics.stringWidth(soundString) / 2 - 5,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 10);
		if (option == SOUND_UP)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(">", screen.getWidth() / 2
						+ fontBigMetrics.stringWidth(soundString) / 2 - 5,
				screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws Ship screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawShipCustomMenu(final Screen screen) {
		String customString = "Customize";
		String instructionsString1 = "Press Space to apply";
		String instructionsString2 = "Press Escape to return";
		String currentString = "Current";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, customString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString1,
				screen.getHeight() / 5);
		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString2,
				screen.getHeight() / 5 + 20);
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, currentString,
				screen.getHeight() / 3);

	}
	/**
	 * Draws cursor and sprites given in List of Entries.
	 *
	 * @param screen
	 * 				Screen to draw on.
	 * @param option
	 * 				Cursor index.
	 * @param designSetting
	 * 				DesignSetting has current design and design list.
	 */
	public void drawDesigns(final Screen screen, int option, DesignSetting designSetting){
		int count = 0;
		int positionX = 40;
		int j = 0, positionY = screen.getWidth()-60;
		int cursorX = 0;
		int cursorY = 0;
		int margin = 10;
		ArrayList<SimpleEntry<SpriteType, Boolean>> designList = designSetting.getDesignList();

		// 두 줄에 다 안들어오는 경우는 고려하지 못함.
		for(SimpleEntry<SpriteType, Boolean> entry: designList){
			SpriteType sprite = entry.getKey();
			boolean isAchieved = entry.getValue();

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

			if(isAchieved)
				drawEntity(dummyShip, positionX, positionY);
			else
				drawShadowedEntity(dummyShip, positionX, positionY);

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
	 * 					X-position of tip
	 * @param tipPositionY
	 * 					Y-position of tip
	 * @param reverse
	 * 				Is Tip's direction reversed? (false: up, true: down)
	 */
	private void drawTriangle(final int tipPositionX, final int tipPositionY, boolean reverse){
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
