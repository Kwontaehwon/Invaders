package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import entity.*;
import engine.*;
import skill.*;

import static engine.Core.backgroundMusic;
import static engine.Core.effectSound;
import static engine.Core.getCooldown;
import static java.lang.Math.abs;


/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen implements Serializable {


	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	private static final int SEPARATION_LINE_HEIGHT = 100;
	/** Time between changing skill cursor */
	private static final int SKILL_CURSOR_DELAY = 200;
	/** boss stage level */
	private static final int BOSS_STAGE_LEVEL = 8;
	/** bonus stage level */
	private static final int BONUS_STAGE_LEVEL = 6;
	/** bonus stage shootingFrequency */
	private static final int BONUS_LEVEL_SHOOTING_FREQ = 2100000;

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship. */
	private Ship ship;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets;
	/** Current score. */
	private int score;
	/** Player lives left. */
	private int lives;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
    /** boss object */
	private Boss boss;
	/** Generator for random number. */

	private Random random = new Random();
	/** Number of remained boom. */
	private int boomTimes ;
	/** Set of all booms on screen fired by player. */
	private Set<Boom> booms;
	/** Index of focused skill. */
	private int skillCursor;
	/** Time between shift of skill cursor */
	private Cooldown skillInputDelay
			;
	/** Object of skill on screen. */
	private Skill1 skill1;
	private Skill2 skill2;
	private Skill3 skill3;
	private Skill4 skill4;
	/** Times for cooldown of skills. */
	private int[] skillCool;
	/** The paused time used for correct running time. */
	private long pauseTime;
	/** Object of ultimate on screen. */
	private Ultimate ultimate;
	/** Number of Ultimates that the player has. */
	private int ultimateTimes;
	/** Time for bonus stage */
	private Cooldown bonusTime;
	/** save second for bonus stage */
	private int bonusSeconds ;
	/** Frame */
	Frame frame;
	/** Object of Items */
	private Item bulletSpeedItem;
	private Item shootingCoolItem;
	private Item bonusLifeItem;
	private Item bonusScoreItem;
	private Item boomItem;

	/** Set position of background image */
	private int backgroundPos = 0;
	/** Remained count before start. */
	private int countdown=INPUT_DELAY/1000;
	/** Design setting for the ship. */
	private transient DesignSetting designSetting;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
					  final GameSettings gameSettings, final boolean bonusLife, final DesignSetting designSetting,
					  final int width, final int height, final int fps,
					  Frame frame) {
		super(width, height, fps);

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining();
		if (this.bonusLife)
			this.lives++;
		this.designSetting = designSetting;
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.boomTimes = gameState.getBoomTimes();
		this.skillCool = gameState.getSkillCool();
		this.skill1 = new Skill1(this.level,this.skillCool[0]);
		this.skill2 = new Skill2(this.level,this.skillCool[1]);
		this.skill3 = new Skill3(this.level,this.skillCool[2]);
		this.skill4 = new Skill4(this.level,this.skillCool[3]);

		this.ultimateTimes = gameState.getUltimateTimes();
		this.frame = frame;
		this.bonusTime = null;
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();
		if(this.level == BOSS_STAGE_LEVEL ){
			this.boss = new Boss();
			this.boss.attach(this);
		}
		else {
			enemyShipFormation = new EnemyShipFormation(this.gameSettings);
			enemyShipFormation.attach(this);
		}

		this.ship = new Ship(this.width / 2, this.height - 70, designSetting.getSizeX(), designSetting.getSizeY(), designSetting.getShipType());

		// Initialize ship
		if(this.level == 1) {
			ship.setBulletSpeed(-6);
			ship.setShootingCoolDown(750);
		}

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);

		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();

		this.booms = new HashSet<Boom>();
		// Skill Cursor , input delay
		this.skillCursor = 0;
		this.skillInputDelay
				= Core.getCooldown(SKILL_CURSOR_DELAY);
		this.skillInputDelay
				.reset();

		this.skill1.startCoolTime();
		this.skill3.startCoolTime();
		this.skill4.startCoolTime();
		this.skill2.startCoolTime();

		this.pauseTime = 0;

		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();

		if(this.level == BONUS_STAGE_LEVEL){
			this.bonusTime = getCooldown(10000);
			bonusTime.reset();
		}

	}
	/**
	 * load a game, connect object
	 */
	public final void load(){
		logger = Core.getLogger();
		designSetting = new DesignSetting(DrawManager.SpriteType.Ship);
		enemyShipFormation.setLogger(this.logger);
		enemyShipFormation.attach(this);
		skill1.setLogger(logger);
		if(this.level == BONUS_STAGE_LEVEL ){
			this.bonusTime = getCooldown(this.bonusSeconds * 1000);
			bonusTime.reset();
		}

	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() throws IOException, ClassNotFoundException {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() throws IOException, ClassNotFoundException {
		super.update();

		// Countdown to game start. 스테이지 시작 전 5초
		if (!this.inputDelay.checkFinished() && countdown >= 0) {

			int countDowned = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			if (countDowned >= -1 && countDowned < countdown) {
				countdown = countDowned;
				if(countdown == 5){
					if(this.level == 2 || this.level == 3 ||this.level == 4 || this.level ==5) effectSound.skillUnlockSound.start();
				}
				if (4 > countdown && countdown > 0) {
					effectSound.countDownSound.start();
				} else if (countdown == 0) {
					effectSound.roundStartSound.start();
				} else if (countdown == -1 && !backgroundMusic.isRunning()){
					backgroundMusic.start();
				}
			}
		}

		if (this.inputDelay.checkFinished() && !this.levelFinished) {
			if (pauseTime != 0) {
				this.skill1.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill2.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill3.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill4.pause(System.currentTimeMillis() - this.pauseTime);
				if (bonusTime != null && !bonusTime.checkFinished()) {
					if(bonusSeconds ==  0 ) bonusTime.pause(System.currentTimeMillis() - this.pauseTime);
					else bonusSeconds = 0;
				}
				this.pauseTime = 0;

			}

			if (!this.ship.isDestroyed()) {
				if(inputManager == null) inputManager = Core.getInputManager();
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);
				boolean SkillCursorRight = inputManager.isKeyDown(KeyEvent.VK_D);
				boolean SkillCursorLeft = inputManager.isKeyDown(KeyEvent.VK_A);
				boolean isPauseScreen = false;

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;
				boolean isUpBorder = this.ship.getPositionY()
						- this.ship.getSpeed() < 1;
				boolean isDownBorder = this.ship.getPositionY()
						+ this.ship.getHeight() + this.ship.getSpeed() > this.height - 1;


				if (moveRight && !isRightBorder) {
					this.ship.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship.moveLeft();
				}
				if (this.skillInputDelay
						.checkFinished() && this.inputDelay.checkFinished()) {
					if (SkillCursorRight && skillCursor < 3) {
						this.skillCursor++;
						this.skillInputDelay
								.reset();
					} else if (SkillCursorRight && skillCursor == 3) {
						this.skillCursor = 0;
						this.skillInputDelay
								.reset();
					} else if (SkillCursorLeft && skillCursor > 0) {
						this.skillCursor--;
						this.skillInputDelay
								.reset();
					} else if (SkillCursorLeft && skillCursor == 0) {
						this.skillCursor = 3;
						this.skillInputDelay
								.reset();
					}
				}

				if (this.skillInputDelay
						.checkFinished() && inputManager.isKeyDown(KeyEvent.VK_X)) {
					if (this.skillCursor == 0 && this.skill1.checkOpen() && this.skill1.returnSkillCoolTime() == 0) {
							effectSound.skill1Sound.start();
							this.skill1.startActivate();
							this.skill1.startCoolTime();
					} else if (this.skillCursor == 1 && this.skill2.checkOpen() && this.skill2.checkCoolTime()) {
							effectSound.skill2Sound.start();
							this.skill2.startActivate();
							this.skill2.startCoolTime();

					} else if (this.skillCursor == 2 && this.skill3.checkOpen() && this.skill3.checkCoolTime()) {
							effectSound.skill3Sound.start();
							this.skill3.startActivate();
							this.skill3.startCoolTime();
					} else if (this.skillCursor == 3 && this.skill4.checkOpen() && this.skill4.checkCoolTime()) {
							effectSound.skill4Sound.start();
							this.skill4.startActivate();
							this.skill4.startCoolTime();
							this.ship.boomSkillShoot(this.booms, -1);
							this.ship.boomSkillShoot(this.booms, 0);
							this.ship.boomSkillShoot(this.booms, +1);
							effectSound.boomingSound.start();
					}
					this.skillInputDelay
							.reset();
				}

				if (this.skill1.checkActivate()) {
					this.skill1.checkDuration();
				}
				if (this.skill2.checkActivate()) {
					this.skill2.checkDuration();
				}
				if (this.skill3.checkActivate()) {
					this.skill3.checkDuration();
				}
				if (this.skill4.checkActivate()) {
					this.skill4.checkDuration();
				}

				if (this.inputDelay.checkFinished() && !isPauseScreen && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
					if (this.ship.shoot(this.bullets)) {
						this.bulletsShot++;
						effectSound.shootingSound.start();
					}
				}

				if (!isPauseScreen && inputManager.isKeyDown(KeyEvent.VK_Z) && boomTimes > 0) {
					if (this.ship.boomShoot(this.booms)) {
						boomTimes--;
						effectSound.boomingSound.start();
						this.logger.info("The boom has been launched.");
					}
				}
				if(!isPauseScreen&&inputManager.isKeyDown(KeyEvent.VK_C) && ultimateTimes > 0){
					effectSound.ultimateSound.start();
					this.ultimate = new Ultimate(this.ship.getPositionX() + this.ship.getWidth()/ 2-100,
							this.ship.getPositionY());
					ultimateTimes--;
					this.logger.info("The Ultimate has been started.");
					effectSound.boomingSound.start();
				}
				if (!isPauseScreen && inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
					if (this.pauseTime == 0) this.pauseTime = System.currentTimeMillis();
					GameState gameState = getGameState();
					this.skillCool[0] = this.skill1.returnSkillCoolTime();
					this.skillCool[1] = this.skill2.returnSkillCoolTime();
					this.skillCool[2] = this.skill3.returnSkillCoolTime();
					this.skillCool[3] = this.skill4.returnSkillCoolTime();
					if(this.level == BONUS_STAGE_LEVEL)
						this.bonusSeconds = this.bonusTime.getDuration() - this.bonusTime.passedCooldown();
					GameStatus gameStatus = new GameStatus(gameState, gameSettings, bonusLife);
					Screen currentScreen = new PauseScreen(width, height, fps, gameStatus,getGameScreen());
					this.logger.info("escKey.");
					returnCode = frame.setScreen(currentScreen,0);
				}
				if (Core.flag_main || Core.flag_restart)
					this.isRunning = false;
			}

			if (this.enemyShipSpecial != null) {
				if (!this.enemyShipSpecial.isDestroyed())
					this.enemyShipSpecial.move(2, 0);
				else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
					this.enemyShipSpecial = null;

			}
			if (this.enemyShipSpecial == null
					&& this.enemyShipSpecialCooldown.checkFinished()) {
				this.enemyShipSpecial = new EnemyShip();
				this.enemyShipSpecialCooldown.reset();
				this.logger.info("A special ship appears");
			}
			if (this.enemyShipSpecial != null) this.enemyShipSpecial.update();
			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.enemyShipSpecial = null;
				this.logger.info("The special ship has escaped");
			}

			// 위치업데이트
			if (this.bulletSpeedItem != null) {
				this.bulletSpeedItem.update();
			}
			if (this.shootingCoolItem != null) {
				this.shootingCoolItem.update();
			}
			if (this.bonusLifeItem != null) {
				this.bonusLifeItem.update();
			}
			if (this.bonusScoreItem != null) {
				this.bonusScoreItem.update();
			}
			if (this.ultimate != null) {
				this.ultimate.update();
			}
			if (this.boomItem != null) {
				this.boomItem.update();
			}
			this.ship.update();
			if (this.level == BOSS_STAGE_LEVEL) {
				this.boss.update();
				int r = random.nextInt(6);
				if (r == 0) this.boss.pinwheelShoot(this.bullets);
				else if (r == 1 || r == 2)
					this.boss.targetingShoot(this.bullets, this.ship);
				else this.boss.randomShoot(this.bullets);
			} else {
				this.enemyShipFormation.update(this.skill2.checkActivate(), this.level);
				this.enemyShipFormation.targetingShoot(this.bullets, this.ship);
			}
		} else {
			if (pauseTime == 0) pauseTime = System.currentTimeMillis();
		}

		manageCollisions();

		if(this.shootingCoolItem != null && checkCollision(this.shootingCoolItem, this.ship)){
			if(this.ship.getShootingCoolDown() > 310){
				this.ship.setShootingCoolDown(this.ship.getShootingCoolDown()-110);
			}
			this.logger.info("Get Item : Bullet Shooting Cooldown Up ! " + this.ship.getShootingCoolDown());
			this.shootingCoolItem = null;
			effectSound.getPowerUpSound.start();
		}
		if(this.bulletSpeedItem != null && checkCollision(this.bulletSpeedItem, this.ship)){
			if(this.ship.getBulletSpeed() > -10){
				this.ship.setBulletSpeed(this.ship.getBulletSpeed()-1);
			}
			this.logger.info("Get Item : Bullet Shooting Cool down Up ! " + this.ship.getBulletSpeed());
			this.bulletSpeedItem = null;
			effectSound.getPowerUpSound.start();
		}

		if (this.bonusLifeItem != null && checkCollision(this.bonusLifeItem, this.ship)) {
			if (this.lives < 3) this.lives++;
			this.bonusLifeItem = null;
			effectSound.recoverySound.start();
		}

		if (this.bonusScoreItem != null && checkCollision(this.bonusScoreItem, this.ship)) {
			this.score = this.score + this.bonusScoreItem.getPointValue();
			this.bonusScoreItem = null;
			effectSound.getCoinSound.start();
		}

		if (this.boomItem != null && checkCollision(this.boomItem, this.ship)) {
			if (this.boomTimes < 3) {
				boomTimes++;
				this.logger.info("Get Item : Boom ! ");
			}
			this.boomItem = null;
			effectSound.getItemSound.start();
		}

		cleanBullets();
		cleanBooms();
		draw();

		if (this.level == BOSS_STAGE_LEVEL) {
			if ((this.boss.isDestroyed() || this.lives == 0)
					&& !this.levelFinished) {
				this.skillCool[0] = this.skill1.returnSkillCoolTime();
				this.skillCool[1] = this.skill2.returnSkillCoolTime();
				this.skillCool[2] = this.skill3.returnSkillCoolTime();
				this.skillCool[3] = this.skill4.returnSkillCoolTime();
				this.levelFinished = true;
				this.ultimateTimes = 1;
				this.screenFinishedCooldown.reset();
				if(this.lives == 0) {
					backgroundMusic.stop();
					effectSound.shipDeathSound.start();
				}
			}
		} else {
			if ((this.enemyShipFormation.isEmpty() || this.lives == 0 ||
					(gameSettings.getShootingFrecuency() ==  BONUS_LEVEL_SHOOTING_FREQ
							&& bonusTime.checkFinished())) && !this.levelFinished) {
				this.skillCool[0] = this.skill1.returnSkillCoolTime();
				this.skillCool[1] = this.skill2.returnSkillCoolTime();
				this.skillCool[2] = this.skill3.returnSkillCoolTime();
				this.skillCool[3] = this.skill4.returnSkillCoolTime();
				this.levelFinished = true;
				this.screenFinishedCooldown.reset();
				if(this.lives == 0) {
					backgroundMusic.stop();
					effectSound.shipDeathSound.start();

				}
			}
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished()){
			if(this.level ==  1) designSetting.setDesignAchieved(DrawManager.SpriteType.NewShipDesign1_1, true);
			this.isRunning = false;
		}

	}
	/**
	 * Draws the elements associated with the screen.
	 */

	private void draw () {
		drawManager.initDrawing(this);
		if (drawManager.drawFlowBackground(this, backgroundPos))
			backgroundPos = 0;
			backgroundPos++;
		drawManager.drawEntity(this.ship, this.ship.getPositionX(), this.ship.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial, this.enemyShipSpecial.getPositionX(), this.enemyShipSpecial.getPositionY());

		if (this.level == BOSS_STAGE_LEVEL) {
			drawManager.drawEntity(boss,boss.getPositionX(),boss.getPositionY());
		} else {
			enemyShipFormation.draw();
		}
		if(this.skill1.checkActivate()){

			drawManager.drawSmallString("SKILL 1 : SHIELD IS USED",8,SEPARATION_LINE_HEIGHT + 15);
		}
		if(this.skill2.checkActivate()){
			drawManager.drawSmallString("SKILL 2 : ENEMYSHIP STOP",8 ,SEPARATION_LINE_HEIGHT + 26);
		}
		if(this.skill3.checkActivate()){
			drawManager.drawSmallString("SKILL 3 : ENEMY BULLET SLOW",8 ,SEPARATION_LINE_HEIGHT + 37);


		}
		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		for (Boom boom : this.booms)
			drawManager.drawEntity(boom, boom.getPositionX(),
					boom.getPositionY());
		if (this.ultimate != null) {
			if (this.ultimate.getPositionY() + 200 < 0) {
				this.ultimate = null;
			} else {
				drawManager.drawEntity(this.ultimate, this.ultimate.getPositionX(), this.ultimate.getPositionY());
			}
		}
		if (this.bulletSpeedItem != null) {
			if (bulletSpeedItem.getPositionY() > this.height) {
				this.bulletSpeedItem = null;
			} else {
				drawManager.drawEntity(this.bulletSpeedItem, this.bulletSpeedItem.getPositionX(), this.bulletSpeedItem.getPositionY());
			}
		}
		if (this.shootingCoolItem != null) {
			if (shootingCoolItem.getPositionY() > this.height) {
				this.shootingCoolItem = null;
			} else {
				drawManager.drawEntity(this.shootingCoolItem, this.shootingCoolItem.getPositionX(), this.shootingCoolItem.getPositionY());
			}
		}
		if (this.bonusLifeItem != null) {
			if (bonusLifeItem.getPositionY() > this.height) {
				this.bonusLifeItem = null;
			} else {
				drawManager.drawEntity(this.bonusLifeItem, this.bonusLifeItem.getPositionX(), this.bonusLifeItem.getPositionY());
			}
		}
		if (this.bonusScoreItem != null) {
			if (bonusScoreItem.getPositionY() > this.height) {
				this.bonusScoreItem = null;
			} else {
				drawManager.drawEntity(this.bonusScoreItem, this.bonusScoreItem.getPositionX(), this.bonusScoreItem.getPositionY());
			}
		}
		if (this.boomItem != null) {
			if (this.boomItem.getPositionY() > this.height) {
				this.boomItem = null;
			} else {
				drawManager.drawEntity(this.boomItem, this.boomItem.getPositionX(), this.boomItem.getPositionY());
			}
		}
		// Interface.
		drawManager.drawUltimate(this.ultimateTimes);
		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives);
		drawManager.drawBulletSpeed(this, this.ship.getBulletSpeed() * -1 - 6);
		drawManager.drawShootingCool(this, ((this.ship.getShootingCoolDown() - 310) / 110) * -1 + 4);
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
		drawManager.drawBooms(this, this.boomTimes);
		drawManager.drawSkills(skillCursor, skill1, skill2, skill3, skill4, this.pauseTime);
		if(this.level ==  BONUS_STAGE_LEVEL){
			drawManager.drawBonusTime(this, bonusTime, this.pauseTime);
		}
		// Countdown to game start. 스테이지 시작전 5초
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY - (System.currentTimeMillis()
							- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height / 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height / 12);
		}
		drawManager.completeDrawing(this);
	}
	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets () {
		Set<Bullet> recyclable = new HashSet<>();
		for (Bullet bullet : this.bullets) {
			bullet.update(this.skill3.checkActivate());
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	private void cleanBooms () {
		Set<Boom> recyclable = new HashSet<>();
		for (Boom boom : this.booms) {
			boom.update();
			if (boom.getPositionY() < SEPARATION_LINE_HEIGHT
					|| boom.getPositionY() > this.height)
				recyclable.add(boom);
		}
		this.booms.removeAll(recyclable);
		BoomPool.recycle(recyclable);
	}
	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageCollisions () {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		Set<Boom> recyclableBoom = new HashSet<Boom>();
		for (Boom boom : this.booms) {
			if (this.level == BOSS_STAGE_LEVEL) {
				if (this.level == BOSS_STAGE_LEVEL) {
					if (checkCollision(boom, this.boss)) {
						effectSound.destroyedEnemySound.start();
						effectSound.boomingSound.stop();
						this.boss.destroy();
						if (this.boss.isDestroyed()) {
							this.score += boss.getPointValue();
							this.logger.info("The Boss is destroyed." );
						}
						recyclableBoom.add(boom);
					}
				}
			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(boom, enemyShip)) {
						for (EnemyShip enemyShip2 : this.enemyShipFormation) {
							if (!enemyShip2.isDestroyed()
									&& checkBoomCollision(boom, enemyShip2)) {
								if (enemyShip.getLives() >= 2) {
									effectSound.hitEnemySound.start();
									effectSound.boomingSound.stop();
									this.enemyShipFormation.destroy(enemyShip2);
								} else {
									effectSound.destroyedEnemySound.start();
									effectSound.boomingSound.stop();
									this.score += enemyShip2.getPointValue();
									this.enemyShipFormation.destroy(enemyShip2);
									dropItem(enemyShip);
									this.logger.info("The item is falling !");
								}
							}
						}
						recyclableBoom.add(boom);
					}
			}
			if (this.enemyShipSpecial != null
					&& !this.enemyShipSpecial.isDestroyed()
					&& checkCollision(boom, this.enemyShipSpecial)) {
				this.score += this.enemyShipSpecial.getPointValue();
				this.shipsDestroyed++;
				this.enemyShipSpecial.destroy();
				this.enemyShipSpecialExplosionCooldown.reset();
				recyclableBoom.add(boom);
			}
		}
		for (Bullet bullet : this.bullets) {
			if (bullet.getSpeedY() > 0) {
				if (checkCollision(bullet, this.ship) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship.isDestroyed()) {
						if (!this.skill1.checkActivate()) {
							effectSound.deathSound.start();
							this.ship.destroy();
							this.lives--;
							this.logger.info("Hit on player ship, " + this.lives
									+ " lives remaining.");
							this.ship.setBulletSpeed(-6);
							this.ship.setShootingCoolDown(750);
						} else {
							this.logger.info("This skill1 is being defended. ! ");
						}
					}
				}
			} else {
				if (this.level == BOSS_STAGE_LEVEL) {
					if (checkCollision(bullet, this.boss)) {
						effectSound.hitEnemySound.start();
						this.boss.destroy();
						if (this.boss.isDestroyed()) {
							this.score += boss.getPointValue();
							effectSound.destroyedEnemySound.start();
						}
						recyclable.add(bullet);
					}
				} else {
					for (EnemyShip enemyShip : this.enemyShipFormation)
						if (!enemyShip.isDestroyed()
								&& checkCollision(bullet, enemyShip)) {
							if (enemyShip.getLives() >= 2) {
								effectSound.hitEnemySound.start();
								this.enemyShipFormation.destroy(enemyShip);
							} else {
								effectSound.destroyedEnemySound.start();
								this.score += enemyShip.getPointValue();
								this.shipsDestroyed++;
								this.enemyShipFormation.destroy(enemyShip);
								dropItem(enemyShip);
								this.logger.info("The item is falling !");
							}
							recyclable.add(bullet);
						}
					if (this.enemyShipSpecial != null
							&& !this.enemyShipSpecial.isDestroyed()
							&& checkCollision(bullet, this.enemyShipSpecial)) {
						effectSound.destroyedEnemySound.start();
						this.score += this.enemyShipSpecial.getPointValue();
						this.shipsDestroyed++;
						this.enemyShipSpecial.destroy();
						this.enemyShipSpecialExplosionCooldown.reset();
						recyclable.add(bullet);
					}
				}
			}
		}
		if(this.ultimate != null){
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed()
						&& checkCollision(ultimate, enemyShip)){
					effectSound.destroyedEnemySound.start();
					this.score += enemyShip.getPointValue();
					this.enemyShipFormation.destroy(enemyShip);
					dropItem(enemyShip);
					this.logger.info("The item is falling !");
				}
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		this.booms.removeAll(recyclableBoom);
		BoomPool.recycle(recyclableBoom);
	}
	/**
	 * Checks if two entities are colliding.
	 *
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship.
	 * @return Result of the collision test.
	 */

	private boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	/**
	 * Checks if two entities are boom collision.
	 *
	 * @param a
	 *            First entity, the Boom.
	 * @param b
	 *            Second entity, the enemyship.
	 * @return Result of the collision test.
	 */
	private boolean checkBoomCollision(final Boom a, final Entity b) {

		int centerBoomX = a.getPositionX() + a.getWidth() / 2;
		int centerBoomY = a.getPositionY() + a.getHeight() / 2;
		int centerEntityX = b.getPositionX() + b.getWidth() / 2;
		int centerEntityY = b.getPositionY() + b.getHeight() / 2;

		int distanceX = abs(centerBoomX - centerEntityX);
		int distanceY = abs(centerBoomY - centerEntityY);

		int maxDistanceX = 70;
		int maxDistanceY = 70;


		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	private void dropItem(EnemyShip enemyShip) {
		int r = random.nextInt(5);
		if(r == 1) { // 5분의 1의확률, 중복으로 아이템 생성x
			int c = random.nextInt(4);
			if(c == 0){
				if(this.shootingCoolItem == null){ // 연사속도
					effectSound.dropItemSound.start();
					this.shootingCoolItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), 16, 16, DrawManager.SpriteType.ShootingCoolItem, 3);
				}
			} else if (c == 1) {
				if (this.bulletSpeedItem == null) { // 총알속도
					effectSound.dropItemSound.start();
					this.bulletSpeedItem= new Item(enemyShip.getPositionX(),  enemyShip.getPositionY(), 16, 16, DrawManager.SpriteType.BulletSpeedItem, 2);
				}
			}
			else if(c == 2) { //폭탄이드랍.
				if(this.boomItem == null){
					effectSound.dropItemSound.start(); // 폭탄 아이템 드랍 소리
					this.boomItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), 16, 16,DrawManager.SpriteType.Boom,4);
				}
			}
			else if(c == 3){
				if(this.bonusLifeItem == null){
					effectSound.dropItemSound.start();	// 보너스 라이프 아이템 드랍 소리
					this.bonusLifeItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), 16, 16, DrawManager.SpriteType.BonusLifeItem, 3);
				}
			} else {
				// 점수 오름차순으로 1/2, 1/3, 1/6 확률
				r = random.nextInt(6);
				if(r == 0){
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();		// 보너스 라이프 아이템 드랍 소리
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), 16, 16, DrawManager.SpriteType.BonusScoreItem3, 6);
					}
				}
				else if(r == 1 || r == 2){
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();		// 보너스 라이프 아이템 드랍 소리
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), 16, 16, DrawManager.SpriteType.BonusScoreItem2,4);
					}
				}
				else{
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();		// 보너스 스코어 아이템 드랍 소리
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(),16, 16, DrawManager.SpriteType.BonusScoreItem1, 2);
					}
				}
			}
		}
	}

	/**
	 * Returns a GameState object representing the status of the game.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot,this.shipsDestroyed,this.boomTimes,this.skillCool,this.ultimateTimes);
	}

	/**
	 * for save game
	 * @return GameScreen
	 */
	public final GameScreen getGameScreen(){
		return this;
	}


}