package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import engine.Cooldown;
import engine.Core;
import engine.GameSettings;
import engine.GameState;
import entity.*;
import engine.*;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Entity;
import entity.Ship;

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
	// 추가한 부분
	private Item item;
	private Cooldown itemCooldown;
	private Random random = new Random();
	private int boomTimes ; //폭탄발사횟수.
	private Set<Boom> booms; //화면상 발사된 폭탄
	private Boom boomItem; // 폭탄아이템(떨어지는)
	// 추가한 부분 (세이브, 로드 관련)
	private int initScore;
	private int initLive;
	private int initBullet;
	private int initShip;
	Frame frame;


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

		// 추가한 부분,다음세이브 폭탄추가
		this.boomTimes = gameState.getBoomtimes();
		// 추가한 부분
		this.initScore = this.score;
		this.initLive = this.lives;
		this.initBullet = bulletsShot;
		this.initShip = this.shipsDestroyed;
		this.frame = frame;
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);

		this.ship = new Ship(this.width / 2, this.height - 30, designSetting.getShipType());
		//this.ship = new Ship(this.width / 2, this.height - 30, gameSettings.getShipColor());
		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();

		//폭탄집합 생성
		this.booms = new HashSet<Boom>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();

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

		if (this.inputDelay.checkFinished() && !this.levelFinished) {

			if (!this.ship.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A);
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
				//총알발사부분 (+일시정지 확인 추가)
				if (!isPauseScreen&&inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
					if (this.ship.shoot(this.bullets)) {
						this.bulletsShot++;
						effectSound.shootingSound.start();		// 총 발사 소리
					}
				}
				//폭탄발사부분
				if (!isPauseScreen && inputManager.isKeyDown(KeyEvent.VK_Z) && boomTimes > 0) {
					if (this.ship.boomShoot(this.booms)) {
						boomTimes--;
						effectSound.boomingSound.start();		// 폭탄 발사 소리
					}
				}
				//일시정지 부분 : 일시정지 화면으로 넘어감
				if(!isPauseScreen&&inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){
					GameState gameState = getGameState();
					gameState.setState(initScore, initLive, initBullet, initShip);
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

			//아이템움직임
			if(this.item != null ){ //아이템이 존재한다면, 아이템을 아래로떨어짐.
				this.item.update();
			}
			//폭탄아이템움직임.
			if(this.boomItem != null){
				this.boomItem.update();
			}
			this.ship.update();
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets);
		}

		manageCollisions();
		//ship과 아이템의 충돌
		if(this.item != null && checkCollision(this.item,this.ship)){
			if(random.nextInt(2) == 1)
				this.ship.setShootingCooldown(100);
			else
				this.ship.setBulletSpeed(-9);
			//아이템쿨타임 시작.item 쿨타임 5초
			this.itemCooldown = Core.getCooldown(5000);
			this.itemCooldown.reset();
			this.item = null;
			effectSound.getItemSound.start();		// 드랍된 아이템 얻는 소리
		}
		// 아이템 지속시간이끝나면 원래대로 돌아옴.
		if(this.itemCooldown != null && this.itemCooldown.checkFinished()){
			this.ship.setShootingCooldown(750);
			this.ship.setBulletSpeed(-6);
			this.itemCooldown = null;
		}
		//ship과 폭탄아이템의 충돌
		if(this.boomItem != null && checkCollision(this.boomItem,this.ship)){
			if(this.boomTimes < 3)
				boomTimes++;
			this.boomItem= null;
			effectSound.getItemSound.start();		// 드랍된 폭탄 아이템 얻는 소리
		}

		//스크린 밖으로 나간 총알 없애기, 또한 Bullets업데이트
		cleanBullets();
		//스크린 밖으로 나간 폭탄없애기, 또한 Booms업데이트
		cleanBooms();

		draw();
		if ((this.enemyShipFormation.isEmpty() || this.lives == 0)
				&& !this.levelFinished) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.ship, this.ship.getPositionX(),
				this.ship.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		// 내가 발사한 폭탄
		for (Boom boom : this.booms)
			drawManager.drawEntity(boom, boom.getPositionX(),
					boom.getPositionY());
		// 아이템생성.
		if(this.item != null ){ //아이템이 존재하면
			if (item.getPositionY() > this.height){ //아이템이 맵밖으로 떨어지면 사라짐.
				this.item = null;
			}
			else {
				drawManager.drawEntity(this.item, this.item.getPositionX(), this.item.getPositionY());
			}
		}
		// 아이템 폭탄
		if(this.boomItem != null){
			if (this.boomItem.getPositionY() > this.height){
				this.boomItem = null;
			}
			else {
				drawManager.drawEntity(this.boomItem, this.boomItem.getPositionX(), this.boomItem.getPositionY());
			}
		}

		//
		// Interface.
		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives,designSetting.getShipType());
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
		// 폭탄 인터페이스 추가
		drawManager.drawBooms(this, this.boomTimes);


		// Countdown to game start.
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
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}
	//스크린밖으로 나간 폭탄없애기 + 폭탄 update
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
		//폭탄 충돌판단
		for(Boom boom : this.booms) {
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed() //enemyShip과 충돌을 하면
						&& checkCollision(boom, enemyShip)) {
					for (EnemyShip enemyShip2 : this.enemyShipFormation) {
						if (!enemyShip2.isDestroyed() //좌표상 폭탄범위에 해당하는 enemyShip destroy.
								&& checkBoomCollision(boom,enemyShip2)) {
							if (enemyShip.getLive() >= 2) {
								effectSound.hitEnemySound.start();			// 적이 폭탄에 타격되는 소리 (사라지지는 않음)
								this.enemyShipFormation.destroy(enemyShip2);
							} else {
								effectSound.destroyedEnemySound.start();	// 적이 폭탄에 파괴되는 소리
								this.score += enemyShip2.getPointValue();
								this.shipsDestroyed++;
								this.enemyShipFormation.destroy(enemyShip2);
								// item 떨어짐.
								dropItem(enemyShip);
							}
						}
					}
					recyclableBoom.add(boom); //충돌에 사용된 폭탄제거.
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
			if (bullet.getSpeed() > 0) { //적이 발사한 경우
				if (checkCollision(bullet, this.ship) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship.isDestroyed()) {
						effectSound.deathSound.start();		// 플레이어가 총알에 맞는 소리
						this.ship.destroy();
						this.lives--;
						this.logger.info("Hit on player ship, " + this.lives
								+ " lives remaining.");
					}
				}
			} else { // 내가 발사한 경우.
				// 적ship 총알 하나하나마다 충돌되었는지 확인
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed() //파괴된 적비행기가아니고
							&& checkCollision(bullet, enemyShip)) {
						if(enemyShip.getLive()>=2){			// 기본 live값이 2 이상인 적을 카운트하지 않음.
							effectSound.hitEnemySound.start();			// 적이 총알에 타격되는 소리 (사라지지는 않음)
							this.enemyShipFormation.destroy(enemyShip);
						}
						else{
							effectSound.destroyedEnemySound.start();	// 적이 총알에 파괴되는 소리
							this.score += enemyShip.getPointValue();
							this.shipsDestroyed++;
							this.enemyShipFormation.destroy(enemyShip);
							// item 떨어짐.
							dropItem(enemyShip);
						}
						recyclable.add(bullet); //충돌에 사용된 총알제거.
					}
				// 적특별개체 판단여부
				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {
					effectSound.destroyedEnemySound.start();	// 적이 총알에 파괴되는 소리
					this.score += this.enemyShipSpecial.getPointValue();
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();
					recyclable.add(bullet);
				}

			}

		this.bullets.removeAll(recyclable); //충돌에 사용된 총알제거.
		BulletPool.recycle(recyclable); // Pool업데이트한후 Pool내 에서 제거해주는듯
		//폭탄도 똑같이해줌.
		this.booms.removeAll(recyclableBoom); // 충돌에 사용된 폭탄제거
		BoomPool.recycle(recyclableBoom); // Pool업데이트한후 Pool내 에서 제거해주는듯
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

	//boom효과를 구현하기 위한 함수
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

	// 적개체 부서였을때 확률적으로 아이템을 드랍.
	private void dropItem(EnemyShip enemyShip){
		if(random.nextInt(5) == 1 ) { // 5분의 1의확률, 중복으로아이템생성x
			if(random.nextInt(2) == 1 ){ // 폭탄과 아이템중
				if(this.item == null) { //아이템이 존재하지않으면
					effectSound.dropItemSound.start();		// 아이템 드랍 소리
					this.item = new Item(enemyShip.getPositionX(), enemyShip.getPositionX());
				}
			}
			else { //폭탄이드랍.
				if(this.boomItem == null){
					effectSound.dropItemSound.start();		// 폭탄 아이템 드랍 소리
					this.boomItem = new Boom(enemyShip.getPositionX(), enemyShip.getPositionX(),2);
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
				this.bulletsShot, this.shipsDestroyed,this.boomTimes);
	}
}