package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import entity.*;
import engine.*;
import skill.*;

import static engine.Core.backgroundMusic;
import static engine.Core.effectSound;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

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
	private static final int SEPARATION_LINE_HEIGHT = 40;
	/** Time between changing skill cursor */
	private static final int SKILL_CURSOR_DELAY = 200;
	/** Time for freezing enemy ships in bonus stage. */
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
	/** Time between shift of skill cursor */
	private Cooldown skillInputDelay;
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
	/** Generator for random number. */
	private Random random = new Random();
	/** Number of remained boom. */
	private int boomTimes ;
	/** Set of all booms on screen fired by player. */
	private Set<Boom> booms;
	/** Index of focused skill. */
	private int skillCursor;
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
	/** inital setting of the stage. */
	private int initScore;
	private int initLive;
	private int initBullet;
	private int initShip;
	/** Time for bonus stage */
	private Cooldown bonusTimeCooldown;
	/** Frame to generate paused screen. */
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
	private int countDown =INPUT_DELAY/1000;
	/** Design setting for the ship. */
	private DesignSetting designSetting;

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

		this.skill1 = new Skill1(20,this.skillCool[0]);
		this.skill2 = new Skill2(20,this.skillCool[1]);
		this.skill3 = new Skill3(20,this.skillCool[2]);
		this.skill4 = new Skill4(20,this.skillCool[3]);

		this.ultimateTimes = gameState.getUltimateTimes();

		this.initScore = this.score;
		this.initLive = this.lives;
		this.initBullet = bulletsShot;
		this.initShip = this.shipsDestroyed;
		this.frame = frame;
		this.bonusTimeCooldown = null;
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);

		this.ship = new Ship(this.width / 2, this.height - 30, designSetting.getShipType());
		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();

		this.booms = new HashSet<Boom>();

		this.skillCursor = 0;
		this.skillInputDelay = Core.getCooldown(SKILL_CURSOR_DELAY);
		this.skillInputDelay.reset();

		this.skill1.startCoolTime();
		this.skill3.startCoolTime();
		this.skill4.startCoolTime();
		this.skill2.startCoolTime();

		this.pauseTime = 0;

		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();

		if(gameSettings.getShootingFrecuency() ==  BONUS_LEVEL_SHOOTING_FREQ){
			this.bonusTimeCooldown = Core.getCooldown(10000);
			bonusTimeCooldown.reset();
		}

	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		// Countdown to game start. 스테이지 시작 전 5초
		if (!this.inputDelay.checkFinished() && countDown >= 0) {

			int countDowned = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);

			if(countDowned >= -1 && countDowned< countDown){
				countDown = countDowned;
				if( 4 > countDown && countDown > 0) {
					effectSound.countDownSound.start();
				}
				else if(countDown == 0) {
					effectSound.roundStartSound.start();
				}
				else if(countDown == -1 && !backgroundMusic.isRunning())
					backgroundMusic.start();
			}
		}

		if (this.inputDelay.checkFinished() && !this.levelFinished) {
			if(pauseTime != 0 ){
				this.skill1.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill2.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill3.pause(System.currentTimeMillis() - this.pauseTime);
				this.skill4.pause(System.currentTimeMillis() - this.pauseTime);
				if(bonusTimeCooldown != null && !bonusTimeCooldown.checkFinished()){
					bonusTimeCooldown.pause(System.currentTimeMillis() - this.pauseTime);
				}
				this.pauseTime = 0;

			}

			if (!this.ship.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);
				boolean skillCursorRight = inputManager.isKeyDown(KeyEvent.VK_D);
				boolean skillCursorLeft = inputManager.isKeyDown(KeyEvent.VK_A);
				boolean isPauseScreen = false;

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;

				if (moveRight && !isRightBorder) {
					this.ship.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship.moveLeft();
				}

				if ( this.skillInputDelay.checkFinished() &&this.inputDelay.checkFinished()) {
					if(skillCursorRight && skillCursor < 3){
						this.skillCursor++;
						this.skillInputDelay.reset();
					}
					else if(skillCursorRight && skillCursor == 3){
						this.skillCursor = 0;
						this.skillInputDelay.reset();
					}
					else if(skillCursorLeft && skillCursor > 0){
						this.skillCursor--;
						this.skillInputDelay.reset();
					}
					else if(skillCursorLeft && skillCursor == 0){
						this.skillCursor = 3;
						this.skillInputDelay.reset();
					}
				}


				if( this.skillInputDelay.checkFinished()&&inputManager.isKeyDown(KeyEvent.VK_X)) {
					if (this.skillCursor == 0 && this.skill1.checkOpen()) {
						if (this.skill1.returnSkillCoolTime() == 0 ) {
							this.skill1.startActivate();
							this.skill1.startCoolTime();
						}
					}
					else if (this.skillCursor == 1 && this.skill2.checkOpen()) {
						if (this.skill2.checkCoolTime()) {
								this.skill2.startActivate();
								this.skill2.startCoolTime();
							}
					}
					else if (this.skillCursor == 2 && this.skill3.checkOpen()) {
						if (this.skill3.checkCoolTime()) {
							this.skill3.startActivate();
							this.skill3.startCoolTime();
						}
					}
					else if (this.skillCursor == 3 && this.skill4.checkOpen()) {
						if (this.skill4.checkCoolTime()) {
							this.skill4.startActivate();
							this.skill4.startCoolTime();

							this.ship.boomSkillShoot(this.booms, -1);
							this.ship.boomSkillShoot(this.booms, 0);
							this.ship.boomSkillShoot(this.booms, +1);
							effectSound.boomingSound.start();
						}
					}
					this.skillInputDelay.reset();
				}

				if(this.skill1.checkActivate()){
					this.skill1.checkDuration();
				}
				if(this.skill2.checkActivate()){
					this.skill2.checkDuration();
				}
				if(this.skill3.checkActivate()){
					this.skill3.checkDuration();
				}
				if(this.skill4.checkActivate()){
					this.skill4.checkDuration();
				}

				// 공격, 폭탄, 궁극기 발사
				if (this.inputDelay.checkFinished()&&!isPauseScreen&&inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
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
					this.ultimate = new Ultimate(this.ship.getPositionX() + this.ship.getWidth()/ 2-100,
							this.ship.getPositionY());
					ultimateTimes--;
					this.logger.info("The Ultimate has been started.");
					effectSound.boomingSound.start();
				}

				if(!isPauseScreen&&inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
					if(this.pauseTime == 0 ) this.pauseTime = System.currentTimeMillis();
					GameState gameState = getGameState();
					gameState.setState(initScore, initLive, initBullet, initShip);
					this.skillCool[0] = this.skill1.returnSkillCoolTime();
					this.skillCool[1] = this.skill2.returnSkillCoolTime();
					this.skillCool[2] = this.skill3.returnSkillCoolTime();
					this.skillCool[3] = this.skill4.returnSkillCoolTime();
					GameStatus gameStatus = new GameStatus(gameState, gameSettings, bonusLife);
					Screen currentScreen = new PauseScreen(width, height, fps, gameStatus);
					Logger LOGGER = Logger.getLogger(Core.class
							.getSimpleName());
					LOGGER.info("escKey.");
					returnCode = frame.setScreen(currentScreen);
				}
				if(Core.flag_main || Core.flag_restart)
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
			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.enemyShipSpecial = null;
				this.logger.info("The special ship has escaped");
			}

			// 위치 업데이트
			if(this.bulletSpeedItem != null ){
				this.bulletSpeedItem.update();
			}
			if(this.shootingCoolItem != null ){
				this.shootingCoolItem.update();
			}
			if(this.bonusLifeItem != null){
				this.bonusLifeItem.update();
			}
			if(this.bonusScoreItem != null){
				this.bonusScoreItem.update();
      		}
			if(this.ultimate != null){
				this.ultimate.update();
			}
			if(this.boomItem != null){
				this.boomItem.update();
			}

			this.ship.update();
			this.enemyShipFormation.update(this.skill2.checkActivate());
			this.enemyShipFormation.targetingShoot(this.bullets, this.ship);
		}
		else{
			// 스테이지 전 5초 Input 딜레이가 안끝났다면 , 스킬쿨타임에 적용시키기 위해 pause한 시간을 잼.
			if(pauseTime ==  0) pauseTime = System.currentTimeMillis();
		}

		manageCollisions();

		// ship과 아이템의 충돌
		if(this.shootingCoolItem != null && checkCollision(this.shootingCoolItem, this.ship)){
			if(this.ship.getShootingCoolDown() > 300){
				this.ship.setShootingCoolDown(this.ship.getShootingCoolDown()-150);
			}
			this.logger.info("Get Item : Bullet Shooting Cooldown Up ! " + this.ship.getShootingCoolDown());
			this.shootingCoolItem = null;
			effectSound.getItemSound.start();
		}
		if(this.bulletSpeedItem != null && checkCollision(this.bulletSpeedItem, this.ship)){
			if(this.ship.getBulletSpeed() > -9){
				this.ship.setBulletSpeed(this.ship.getBulletSpeed()-1);
			}
			this.logger.info("Get Item : Bullet Shooting Cool down Up ! " + this.ship.getBulletSpeed());
			this.bulletSpeedItem = null;
			effectSound.getItemSound.start();
		}
		if(this.bonusLifeItem != null && checkCollision(this.bonusLifeItem,this.ship)){
			if(this.lives < 3) this.lives++;
			this.bonusLifeItem=null;
			effectSound.getItemSound.start();
		}
		if(this.bonusScoreItem != null && checkCollision(this.bonusScoreItem,this.ship)){
			this.score = this.score + this.bonusScoreItem.getPointValue();
			this.bonusScoreItem=null;
			effectSound.getItemSound.start();
		}
		if(this.boomItem != null && checkCollision(this.boomItem,this.ship)){
			if(this.boomTimes < 3) {
				boomTimes++;
				this.logger.info("Get Item : Boom ! ");
			}
			this.boomItem= null;
			effectSound.getItemSound.start();		// 드랍된 폭탄 아이템 얻는 소리
		}

		cleanBullets();
		cleanBooms();

		draw();

		// 스테이지 종료 ( 적기 전멸 or 플레이어 사망 or 보너스 스테이지 종료 )
		if (!this.levelFinished &&
				(this.enemyShipFormation.isEmpty() || this.lives == 0 ||
						(gameSettings.getShootingFrecuency() ==  BONUS_LEVEL_SHOOTING_FREQ && bonusTimeCooldown.checkFinished())
				)) {
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

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		if(drawManager.drawFlowBackground(this, backgroundPos))
			backgroundPos = 0;
		backgroundPos++;

		drawManager.drawEntity(this.ship, this.ship.getPositionX(),
				this.ship.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		// 활성화 중인 스킬의 로그
		if(this.skill1.checkActivate()){
			drawManager.drawSmallString("SKILL 1 : SHIELD IS USED",8,55);
		}
		if(this.skill2.checkActivate()){
			drawManager.drawSmallString("SKILL 2 : ENEMYSHIP STOP",8 ,66);
		}
		if(this.skill3.checkActivate()){
			drawManager.drawSmallString("SKILL 3 : ENEMY BULLET SLOW",8 ,77);
		}

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		for (Boom boom : this.booms)
			drawManager.drawEntity(boom, boom.getPositionX(),
					boom.getPositionY());

		// 개체 생성
		if(this.ultimate != null) {
			if (this.ultimate.getPositionY() + 200 < 0) {
				this.ultimate = null;
			} else {
				drawManager.drawEntity(this.ultimate, this.ultimate.getPositionX(), this.ultimate.getPositionY());
			}
		}
		if(this.bulletSpeedItem != null ){
			if (bulletSpeedItem.getPositionY() > this.height){
				this.bulletSpeedItem = null;
			}
			else {
				drawManager.drawEntity(this.bulletSpeedItem, this.bulletSpeedItem.getPositionX(), this.bulletSpeedItem.getPositionY());
			}
		}
		if(this.shootingCoolItem != null ){
			if (shootingCoolItem.getPositionY() > this.height){
				this.shootingCoolItem = null;
			}
			else {
				drawManager.drawEntity(this.shootingCoolItem, this.shootingCoolItem.getPositionX(), this.shootingCoolItem.getPositionY());
			}
		}
		if(this.bonusLifeItem != null ){
			if (bonusLifeItem.getPositionY() > this.height){
				this.bonusLifeItem = null;
			}
			else {
				drawManager.drawEntity(this.bonusLifeItem, this.bonusLifeItem.getPositionX(), this.bonusLifeItem.getPositionY());
			}
		}
		if(this.bonusScoreItem != null ){
			if (bonusScoreItem.getPositionY() > this.height){
				this.bonusScoreItem = null;
			}
			else {
				drawManager.drawEntity(this.bonusScoreItem, this.bonusScoreItem.getPositionX(), this.bonusScoreItem.getPositionY());
			}
		}
		if(this.boomItem != null){
			if (this.boomItem.getPositionY() > this.height){
				this.boomItem = null;
			}
			else {
				drawManager.drawEntity(this.boomItem, this.boomItem.getPositionX(), this.boomItem.getPositionY());
			}
		}

		drawManager.drawUltimate(this.ultimateTimes);

		// Interface.
		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives,designSetting.getShipType());
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
		drawManager.drawBooms(this, this.boomTimes);
		drawManager.drawSkills(skillCursor, skill1, skill2, skill3, skill4,this.pauseTime);
		if(gameSettings.getShootingFrecuency() ==  BONUS_LEVEL_SHOOTING_FREQ){
			drawManager.drawBonusTime(this, bonusTimeCooldown, this.pauseTime);
		}

		// Countdown to game start. 스테이지 시작전 5초
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update(this.skill3.checkActivate());
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	private void cleanBooms() {
		Set<Boom> recyclable = new HashSet<Boom>();
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
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		Set<Boom> recyclableBoom = new HashSet<Boom>();

		for(Boom boom : this.booms) {
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed()
						&& checkCollision(boom, enemyShip)) {
					for (EnemyShip enemyShip2 : this.enemyShipFormation) {
						if (!enemyShip2.isDestroyed() //좌표상 폭탄범위에 해당하는 enemyShip destroy.
								&& checkBoomCollision(boom,enemyShip2)) {
							if (enemyShip.getLives() >= 2) {
								effectSound.hitEnemySound.start();
								this.enemyShipFormation.destroy(enemyShip2);
							} else {
								effectSound.destroyedEnemySound.start();
								this.score += enemyShip2.getPointValue();
								this.shipsDestroyed++;
								this.enemyShipFormation.destroy(enemyShip2);

								dropItem(enemyShip);
								this.logger.info("The item is falling !");

							}
						}
					}
					recyclableBoom.add(boom);
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
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeedY() > 0) {
				if (checkCollision(bullet, this.ship) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship.isDestroyed()) {
						if(!this.skill1.checkActivate()) {
							effectSound.deathSound.start();
							this.ship.destroy();
							this.lives--;
							this.logger.info("Hit on player ship, " + this.lives
									+ " lives remaining.");
							// 죽었으므로 총알 속도와 연사 속도 초기화
							this.ship.setBulletSpeed(-6);
							this.ship.setShootingCoolDown(750);
						}
						else{
							this.logger.info("This skill1 is being defended. ! ");
						}
					}
				}
			} else { // 내가 발사한 경우.

				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						if(enemyShip.getLives()>=2){
							effectSound.hitEnemySound.start();
							this.enemyShipFormation.destroy(enemyShip);
						}
						else{
							effectSound.destroyedEnemySound.start();
							this.score += enemyShip.getPointValue();
							this.shipsDestroyed++;
							this.enemyShipFormation.destroy(enemyShip);

							dropItem(enemyShip);
							this.logger.info("The item is falling !");
						}
						recyclable.add(bullet);
					}
				// 적특별개체 판정
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
		if(this.ultimate != null){
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed()
						&& checkCollision(ultimate, enemyShip)){
					effectSound.destroyedEnemySound.start();
					this.score += enemyShip.getPointValue();
					this.shipsDestroyed++;
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


	private boolean checkBoomCollision(final Boom a,final Entity b){

		int centerBoomX = a.getPositionX() + a.getWidth() / 2;
		int centerBoomY = a.getPositionY() + a.getHeight() / 2;
		int centerEntityX = b.getPositionX() + b.getWidth() / 2;
		int centerEntityY = b.getPositionY() + b.getHeight() / 2;

		int distanceX = Math.abs(centerBoomX - centerEntityX);
		int distanceY = Math.abs(centerBoomY - centerEntityY);

		int maxDistanceX = 60;
		int maxDistanceY = 60;


		return  distanceX < maxDistanceX && distanceY < maxDistanceY;
	}


	private void dropItem(EnemyShip enemyShip){
		int r = random.nextInt(5);
		if(r == 1) { // 5분의 1의확률, 중복으로 아이템 생성 안함
			int c = random.nextInt(5);
			if(c == 0){
				if(this.shootingCoolItem == null){
					effectSound.dropItemSound.start();
					this.shootingCoolItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(),DrawManager.SpriteType.ShootingCoolItem);
				}
			}
			else if(c == 1) {
				if(this.bulletSpeedItem== null){
					effectSound.dropItemSound.start();
					this.bulletSpeedItem= new Item(enemyShip.getPositionX(), enemyShip.getPositionY(),DrawManager.SpriteType.BulletSpeedItem);
				}
			}
			else if(c == 2) {
				if(this.boomItem == null){
					effectSound.dropItemSound.start();
					this.boomItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(),DrawManager.SpriteType.Boom);
				}
			}
			else if(c == 3){
				if(this.bonusLifeItem == null){
					effectSound.dropItemSound.start();
					this.bonusLifeItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), DrawManager.SpriteType.BonusLifeItem);
				}
			}
			else {
				// 점수 오름차순으로 1/2, 1/3, 1/6 확률
				r = random.nextInt(6);
				if(r == 0){
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), DrawManager.SpriteType.BonusScoreItem3);
					}
				}
				else if(r == 1 || r == 2){
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), DrawManager.SpriteType.BonusScoreItem2);
					}
				}
				else{
					if(this.bonusScoreItem == null){
						effectSound.dropItemSound.start();
						this.bonusScoreItem = new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), DrawManager.SpriteType.BonusScoreItem1);
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
	//스테이지를 클리어했으면 현재 스테이트를 넘겨줌.
	public final GameState getGameState() {
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot, this.shipsDestroyed,this.boomTimes,this.skillCool,this.ultimateTimes);
	}
}