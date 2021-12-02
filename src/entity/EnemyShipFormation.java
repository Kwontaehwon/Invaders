package entity;


import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import screen.Screen;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.GameSettings;

/**
 * Groups enemy ships into a formation that moves together.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShipFormation implements Iterable<EnemyShip> , Serializable {

	/** Initial position in the x-axis. */
	private static final int INIT_POS_X = 20;
	/** Initial position in the x-axis. */
	private static final int INIT_POS_Y = 200;
	/** Distance between ships. */
	private static final int SEPARATION_DISTANCE = 40;
	/** Proportion of C-type ships. */
	private static final double PROPORTION_C = 0.2;
	/** Proportion of B-type ships. */
	private static final double PROPORTION_B = 0.3;
	/** Proportion of B-type ships. */
	private static final double PROPORTION_A = 0.3;
	/** Lateral speed of the formation. */
	private static final int X_SPEED = 8;
	/** Downwards speed of the formation. */
	private static final int Y_SPEED = 4;
	/** Speed of the bullets shot by the members. */
	private static final int BULLET_SPEED = 4;
	/** Proportion of differences between shooting times. */
	private static final double SHOOTING_VARIANCE = .2;
	/** Margin on the sides of the screen. */
	private static final int SIDE_MARGIN = 20;
	/** Margin on the bottom of the screen. */
	private static final int BOTTOM_MARGIN = 350;
	/** Margin on the top of the screen. */
	private static final int TOP_MARGIN = 200;
	/** Distance to go down each pass. */
	private static final int DESCENT_DISTANCE = 20;
	/** Minimum speed allowed. */
	private static final int MINIMUM_SPEED = 10;
	/** moving change time*/
	private static final int MOVE_CHANGE = 3 * 1000;


	/** DrawManager instance. */
	private transient DrawManager drawManager;
	/** Application logger. */
	private transient Logger logger;
	/** Screen to draw ships on. */
	private transient Screen screen;


	/** List of enemy ships forming the formation. */
	private List<List<EnemyShip>> enemyShips;
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** moving change interval */
	private Cooldown moovingCooldown;
	/** Number of ships in the formation - horizontally. */
	private int nShipsWide;
	/** Number of ships in the formation - vertically. */
	private int nShipsHigh;
	/** Time between shots. */
	private int shootingInterval;
	/** Variance in the time between shots. */
	private int shootingVariance;
	/** Initial ship speed. */
	private int baseSpeed;
	/** Speed of the ships. */
	private int movementSpeed;
	/** Current direction the formation is moving on. */
	private Direction currentDirection;
	/** Direction the formation was moving previously. */
	private Direction previousDirection;
	/** Interval between movements, in frames. */
	private int movementInterval;
	/** Total width of the formation. */
	private int width;
	/** Total height of the formation. */
	private int height;
	/** Position in the x-axis of the upper left corner of the formation. */
	private int positionX;
	/** Position in the y-axis of the upper left corner of the formation. */
	private int positionY;
	/** Width of one ship. */
	private int shipWidth;
	/** Height of one ship. */
	private int shipHeight;
	/** List of ships that are able to shoot. */
	private List<EnemyShip> shooters;
	/** Number of not destroyed ships. */
	private int shipCount;
	/** Pattern motion button. */
	private int currentPattern ;
	private int previousPattern;

	/** Directions the formation can move. */
	private enum Direction {
		/** Movement to the right side of the screen. */
		RIGHT,
		RIGHT_DOWN,
		RIGHT_UP,
		/** Movement to the left side of the screen. */
		LEFT,
		LEFT_DOWN,
		LEFT_UP,
		/** Movement to the bottom of the screen. */
		DOWN,
		/** Movement to the top of the screen. */
		UP

	};

	/**
	 * Constructor, sets the initial conditions.
	 * 
	 * @param gameSettings
	 *            Current game settings.
	 */
	public EnemyShipFormation(final GameSettings gameSettings) {
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
		this.moovingCooldown = Core.getCooldown(MOVE_CHANGE);
		this.moovingCooldown.reset();
		this.enemyShips = new ArrayList<List<EnemyShip>>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.nShipsWide = gameSettings.getFormationWidth();
		this.nShipsHigh = gameSettings.getFormationHeight();
		this.shootingInterval = gameSettings.getShootingFrecuency();
		this.shootingVariance = (int) (gameSettings.getShootingFrecuency()
				* SHOOTING_VARIANCE);
		this.baseSpeed = gameSettings.getBaseSpeed();
		this.movementSpeed = this.baseSpeed;
		this.positionX = INIT_POS_X;
		this.positionY = INIT_POS_Y;
		this.shooters = new ArrayList<EnemyShip>();
		//패턴 동작버튼
		this.currentPattern = 0;
		this.previousPattern = 0;

		SpriteType spriteType;

		this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.nShipsWide; i++)
			this.enemyShips.add(new ArrayList<EnemyShip>());

		for (List<EnemyShip> column : this.enemyShips) {
			for (int i = 0; i < this.nShipsHigh; i++) {
				if (i / (float) this.nShipsHigh < PROPORTION_A)
					spriteType = SpriteType.EnemyShipA1;
				else if (i / (float) this.nShipsHigh < PROPORTION_A
						+ PROPORTION_B)
					spriteType = SpriteType.EnemyShipB1;
				else if (i / (float) this.nShipsHigh < PROPORTION_A
						+ PROPORTION_B + PROPORTION_C)
					spriteType = SpriteType.EnemyShipC1;
				else
					spriteType = SpriteType.EnemyShipD1;

				column.add(new EnemyShip((SEPARATION_DISTANCE 
						* this.enemyShips.indexOf(column))
								+ positionX, (SEPARATION_DISTANCE * i)
								+ positionY, spriteType));
				this.shipCount++;
			}
		}

		this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
		this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

		this.width = (this.nShipsWide - 1) * SEPARATION_DISTANCE
				+ this.shipWidth;
		this.height = (this.nShipsHigh - 1) * SEPARATION_DISTANCE
				+ this.shipHeight;

		for (List<EnemyShip> column : this.enemyShips)
			this.shooters.add(column.get(column.size() - 1));
	}

	/**
	 * Associates the formation to a given screen.
	 * 
	 * @param newScreen
	 *            Screen to attach.
	 */
	public final void attach(final Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Draws every individual component of the formation.
	 */
	public final void draw() {
		if (drawManager == null) drawManager = Core.getDrawManager();
		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
						enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	public final void update(boolean skill2, int level) {
		if(this.shootingCooldown == null) {
			this.shootingCooldown = Core.getVariableCooldown(shootingInterval,
					shootingVariance);
			this.shootingCooldown.reset();
		}

		cleanUp();

		int movementX = 0;
		int movementY = 0;
		double remainingProportion = (double) this.shipCount
				/ (this.nShipsHigh * this.nShipsWide);
		this.movementSpeed = (int) (Math.pow(remainingProportion, 2)
				* this.baseSpeed);
		this.movementSpeed += MINIMUM_SPEED;



		movementInterval++;
		if(this.shipCount > level * 2){ //평범한움직임.
			if (movementInterval >= this.movementSpeed) {
				movementInterval = 0;
				boolean isAtBottom = positionY
						+ this.height > screen.getHeight() - BOTTOM_MARGIN;
				boolean isAtRightSide = positionX
						+ this.width >= screen.getWidth() - SIDE_MARGIN;
				boolean isAtLeftSide = positionX <= SIDE_MARGIN;
				boolean isAtHorizontalAltitude = positionY % DESCENT_DISTANCE == 0;
				if (currentDirection == Direction.DOWN) {
					if (isAtHorizontalAltitude)
						if (previousDirection == Direction.RIGHT) {
							currentDirection = Direction.LEFT;
							this.logger.info("Formation now moving left 1");
						} else {
							currentDirection = Direction.RIGHT;
							this.logger.info("Formation now moving right 2");
						}
				} else if (currentDirection == Direction.LEFT) {
					if (isAtLeftSide)
						if (!isAtBottom) {
							previousDirection = currentDirection;
							currentDirection = Direction.DOWN;
							this.logger.info("Formation now moving down 3");
						} else {
							currentDirection = Direction.RIGHT;
							this.logger.info("Formation now moving right 4");
						}
				} else { //RIGHT
					if (isAtRightSide)
						if (!isAtBottom) {
							previousDirection = currentDirection;
							currentDirection = Direction.DOWN;
							this.logger.info("Formation now moving down 5");
						} else {
							currentDirection = Direction.LEFT;
							this.logger.info("Formation now moving left 6");
						}
				}
				if (currentDirection == Direction.RIGHT)
					movementX = X_SPEED;
				else if (currentDirection == Direction.LEFT)
					movementX = -X_SPEED;
				else
					movementY = Y_SPEED;

				positionX += movementX;
				positionY += movementY;

				// Cleans explosions.
				List<EnemyShip> destroyed;
				for (List<EnemyShip> column : this.enemyShips) {
					destroyed = new ArrayList<EnemyShip>();
					for (EnemyShip ship : column) {
						if (ship != null && ship.isDestroyed()) {
							destroyed.add(ship);
							this.logger.info("Removed enemy "
									+ column.indexOf(ship) + " from column "
									+ this.enemyShips.indexOf(column));
						}
					}
					column.removeAll(destroyed);
				}

				if (!skill2) { //스킬 2가 활성화중이 아니면 적개체가 움직임.
					for (List<EnemyShip> column : this.enemyShips){
						for (EnemyShip enemyShip : column) {
							if(shootingInterval != 2100000){
								enemyShip.move(movementX, movementY);
							}
							else{
								enemyShip.move(0,0);
							}
							enemyShip.update();
						}
					}
				}
			}

		}
		else{ //무작위움직임
			if (movementInterval >= this.movementSpeed) {
				movementInterval = 0;
				int bound = 9;
				if(this.currentPattern == 0){ // 9개의 spot중 어느곳으로?
					this.currentPattern = new Random().nextInt(bound)+1;
				}
				if(this.currentPattern == 1){ //  20,150
					int goalX = 20;
					int goalY = 150;
					int X = positionX;
					int Y = positionY;
					System.out.println("spot:1 positionX : "+X);
					System.out.println("spot:1 positionY : "+Y);
					if( X <= goalX && Y <= goalY){
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;

					}
					else if( X > goalX && Y > goalY){
						currentDirection = Direction.LEFT_UP;
					}
					else if( X > goalX){
						currentDirection = Direction.LEFT;
					}
					else if( Y > goalY ){
						currentDirection = Direction.UP;
					}
				}
				if(this.currentPattern == 2){ //330, 150
					int goalX = 330;
					int goalY = 150;
					int X = positionX + this.width/2;
					int Y = positionY;
					System.out.println("spot2: positionX : "+X);
					System.out.println("spot2: positionY : "+Y);
					if( between(X,goalX-10,goalX+10) && Y <= goalY){ // X true Y true
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;
					}
					else if( !between(X,goalX-10,goalX+10) ){ // X false
						if( X <= goalX ){ //일단오른쪽으로이동.
							if(Y <= goalY){ //Y가만족하면
								this.currentDirection = Direction.RIGHT;
							}
							else this.currentDirection = Direction.RIGHT_UP;
						}
						else { //일단왼쪽으로 이동
							if(Y <= goalY){ //Y가만족하면
								this.currentDirection = Direction.LEFT;
							}
							else this.currentDirection = Direction.LEFT_UP;
						}
					}
					else { // Y false ,
						this.currentDirection = Direction.UP;
					}
				}
				if(this.currentPattern == 3){ // 오른쪽위 구석 645,150
					int goalX = 640;
					int goalY = 150;
					int X = positionX + this.width;
					int Y = positionY;
					System.out.println("spot3: positionX : "+X);
					System.out.println("spot3: positionY : "+Y);
					if( X >= goalX && Y <= goalY){
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern)  //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;

					}
					else if( X < goalX && Y > goalY){
						currentDirection = Direction.RIGHT_UP;
					}
					else if( X < goalX) {
						currentDirection = Direction.RIGHT;
					}
					else if( Y > goalY ){
						currentDirection = Direction.UP;
					}
				}
				if(this.currentPattern == 4){ //20, 325
					int goalX = 20;
					int goalY = 325;
					int X = positionX;
					int Y = positionY + this.height/2;
					System.out.println("spot4: positionX : "+X);
					System.out.println("spot4: positionY : "+Y);
					if( X <= goalX  && between(Y,goalY-10,goalY+10)){ // X true Y true
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;
					}
					else if( !between(Y,goalY-10,goalY+10) ){ // Y false
						if( Y <= goalY ){ //일단아래로이동
							if(X <= goalX){ //X True
								this.currentDirection = Direction.DOWN;
							}
							else this.currentDirection = Direction.LEFT_DOWN;
						}
						else { //일단위로 이동
							if(X <= goalX){ //X True
								this.currentDirection = Direction.UP;
							}
							else this.currentDirection = Direction.LEFT_UP;
						}
					}
					else { // X false ,
						this.currentDirection = Direction.LEFT;
					}
				}
				if(this.currentPattern == 5){ //330, 325
					int goalX = 330;
					int goalY = 325;
					int X = positionX + this.width/2;
					int Y = positionY + this.height/2;
					System.out.println("spot5: positionX : "+X);
					System.out.println("spot5: positionY : "+Y);
					if( between(X,goalX-10,goalX+10)  && between(Y,goalY-10,goalY+10)){ // X true Y true
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;
					}
					else if( !between(Y,goalY-10,goalY+10) ){ // Y false
						if( Y <= goalY ){ //일단위로이동
							if(X <= goalX){ //X True
								this.currentDirection = Direction.DOWN;
							}
							else this.currentDirection = Direction.LEFT_DOWN;
						}
						else { //일단아래으로 이동
							if(X <= goalX){ //X True
								this.currentDirection = Direction.UP;
							}
							else this.currentDirection = Direction.LEFT_UP;
						}
					}
					else if( !between(X,goalX-10,goalX+10) ){ // X false
						if( X <= goalX ){ //일단오른쪽으로이동.
							if(Y <= goalY){ //Y가만족하면
								this.currentDirection = Direction.RIGHT;
							}
							else this.currentDirection = Direction.RIGHT_UP;
						}
						else { //일단왼쪽으로 이동
							if(Y <= goalY){ //Y가만족하면
								this.currentDirection = Direction.LEFT;
							}
							else this.currentDirection = Direction.LEFT_UP;
						}
					}
				}
				if(this.currentPattern == 6){ //645, 325
					int goalX = 645;
					int goalY = 325;
					int X = positionX+ this.width;
					int Y = positionY + this.height/2;
					System.out.println("spot6: positionX : "+X);
					System.out.println("spot6: positionY : "+Y);
					if( X >= goalX  && between(Y,goalY-10,goalY+10)){ // X true Y true
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;
					}
					else if( !between(Y,goalY-10,goalY+10) ){ // Y false
						if( Y <= goalY ){ //일단아래로
							if(X >= goalX){ //X True
								this.currentDirection = Direction.DOWN;
							}
							else this.currentDirection = Direction.RIGHT_DOWN;
						}
						else { //일단위로 이동
							if(X >= goalX){ //X True
								this.currentDirection = Direction.UP;
							}
							else this.currentDirection = Direction.RIGHT_UP;
						}
					}
					else { // X false ,
						this.currentDirection = Direction.RIGHT;
					}
				}
				if(this.currentPattern == 7){ // 왼쪽 아래 구석 20,500
					int goalX = 20;
					int goalY = 500;
					int X = positionX;
					int Y = positionY + this.height;
					System.out.println("spot 7 positionX : "+positionX);
					System.out.println("spot 7 positionY : "+ (positionY + this.height));
					if( X <= goalX && Y >= goalY){
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound)+1;
					}
					else if( X > goalX && Y < goalY){
						currentDirection = Direction.LEFT_DOWN;
					}
					else if( X > goalX){
						currentDirection = Direction.LEFT;
					}
					else if( Y < goalY ){
						currentDirection = Direction.DOWN;
					}
				}
				if(this.currentPattern == 8){ //330, 500
					int goalX = 330;
					int goalY = 150;
					int X = positionX + this.width/2;
					int Y = positionY + this.height;
					System.out.println("spot8: positionX : "+X);
					System.out.println("spot8: positionY : "+Y);
					if( between(X,goalX-10,goalX+10) && Y >= goalY){ // X true Y true
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound) + 1;
					}
					else if( !between(X,goalX-10,goalX+10) ){ // X false
						if( X <= goalX ){ //일단오른쪽으로이동.
							if(Y >= goalY){ //Y가만족하면
								this.currentDirection = Direction.RIGHT;
							}
							else this.currentDirection = Direction.RIGHT_DOWN;
						}
						else { //일단왼쪽으로 이동
							if(Y >= goalY){ //Y가만족하면
								this.currentDirection = Direction.LEFT;
							}
							else this.currentDirection = Direction.LEFT_DOWN;
						}
					}
					else { // Y false ,
						this.currentDirection = Direction.DOWN;
					}
				}
				if(this.currentPattern == 9){ //오른쪽 아래 구석 645,500
					int goalX = 640;
					int goalY = 500;
					int X = positionX + this.width;
					int Y = positionY + this.height;
					System.out.println("spot 9 positionX : "+ (positionX + this.width));
					System.out.println("spot 9 positionY : "+ (positionY+ this.height));
					if( X >= goalX && Y >= goalY){
						this.previousPattern = this.currentPattern;
						while(this.currentPattern == this.previousPattern) //이전과 똑같은 방향으로 가지않게.
							this.currentPattern = new Random().nextInt(bound)+1;
					}
					else if( X < goalX && Y < goalY){
						currentDirection = Direction.RIGHT_DOWN;
					}
					else if( X < goalX){
						currentDirection = Direction.RIGHT;
					}
					else if( Y < goalY ){
						currentDirection = Direction.DOWN;
					}
				}

				if (currentDirection == Direction.RIGHT) {
					movementX = X_SPEED+2;
				} else if (currentDirection == Direction.RIGHT_DOWN) {
					movementX = X_SPEED+2;
					movementY = Y_SPEED+5;
				} else if (currentDirection == Direction.RIGHT_UP) {
					movementX = X_SPEED+2;
					movementY = -Y_SPEED-5;
				} else if (currentDirection == Direction.LEFT) {
					movementX = -X_SPEED-2;
				} else if (currentDirection == Direction.LEFT_DOWN) {
					movementX = -X_SPEED-2;
					movementY = Y_SPEED+5;
				} else if (currentDirection == Direction.LEFT_UP) {
					movementX = -X_SPEED-2;
					movementY = -Y_SPEED-5;
				} else if (currentDirection == Direction.DOWN) {
					movementY = Y_SPEED+5;
				} else if (currentDirection == Direction.UP) {
					movementY = -Y_SPEED-5;
				}

				positionX += movementX;
				positionY += movementY;

				// Cleans explosions.
				List<EnemyShip> destroyed;
				for (List<EnemyShip> column : this.enemyShips) {
					destroyed = new ArrayList<EnemyShip>();
					for (EnemyShip ship : column) {
						if (ship != null && ship.isDestroyed()) {
							destroyed.add(ship);
							this.logger.info("Removed enemy "
									+ column.indexOf(ship) + " from column "
									+ this.enemyShips.indexOf(column));
						}
					}
					column.removeAll(destroyed);
				}
				if (!skill2) { //스킬 2가 활성화중이 아니면 적개체가 움직임.
					for (List<EnemyShip> column : this.enemyShips){
						for (EnemyShip enemyShip : column) {
							if(shootingInterval != 2100000){
								enemyShip.move(movementX, movementY);
							}
							else{
								enemyShip.move(0,0);
							}
							enemyShip.update();
						}
					}
				}
			}
		}
	}

	/**
	 * Whether a specific value is within the range.
	 * @param x
	 * 		 specific value
	 * @param max
	 * @param min
	 * @return true or false
	 * */
	private boolean between(int x, int min,int max) {
		return x >= min && x <= max;
	}

	/**
	 * Cleans empty columns, adjusts the width and height of the formation.
	 */
	private void cleanUp() {
		Set<Integer> emptyColumns = new HashSet<Integer>();
		int maxColumn = 0;
		int minPositionY = Integer.MAX_VALUE;
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				// Height of this column
				int columnSize = column.get(column.size() - 1).positionY
						- this.positionY + this.shipHeight;
				maxColumn = Math.max(maxColumn, columnSize);
				minPositionY = Math.min(minPositionY, column.get(0)
						.getPositionY());
			} else {
				// Empty column, we remove it.
				emptyColumns.add(this.enemyShips.indexOf(column));
			}
		}
		for (int index : emptyColumns) {
			this.enemyShips.remove(index);
			logger.info("Removed column " + index);
		}

		int leftMostPoint = 0;
		int rightMostPoint = 0;
		
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				if (leftMostPoint == 0)
					leftMostPoint = column.get(0).getPositionX();
				rightMostPoint = column.get(0).getPositionX();
			}
		}

		this.width = rightMostPoint - leftMostPoint + this.shipWidth;
		this.height = maxColumn;

		this.positionX = leftMostPoint;
		this.positionY = minPositionY;
	}

	/**
	 * Shoots a bullet downwards.
	 * 
	 * @param bullets
	 *            Bullets set to add the bullet being shot.
	 */
	public final void shoot(final Set<Bullet> bullets) {
		// For now, only ships in the bottom row are able to shoot.
		int index = (int) (Math.random() * this.shooters.size());
		EnemyShip shooter = this.shooters.get(index);

		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(shooter.getPositionX()
					+ shooter.getWidth() / 2, shooter.getPositionY(), 0, BULLET_SPEED));
		}
	}

	public final void targetingShoot(final Set<Bullet> bullets, final Entity target) {
		// For now, only ships in the bottom row are able to shoot.
		int index = (int) (Math.random() * this.shooters.size());
		EnemyShip shooter = this.shooters.get(index);
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			int difX = target.getPositionX() + target.width / 2 - shooter.getPositionX() - shooter.width / 2;
			int difY = target.getPositionY() - shooter.getPositionY();
			int divideNum = 200;

			if (difX > 200) divideNum = (int) (difX * 0.95);
			else if (difX < -200) divideNum = (int) (difX * -0.95);

			bullets.add(BulletPool.getBullet(shooter.getPositionX() + shooter.width / 2,
					shooter.getPositionY(), difX * BULLET_SPEED / divideNum, difY * BULLET_SPEED / divideNum));
		}
	}

	/**
	 * Destroys a ship.
	 * 
	 * @param destroyedShip
	 *            Ship to be destroyed.
	 */
	public final void destroy(final EnemyShip destroyedShip) {
		for (List<EnemyShip> column : this.enemyShips)
			for (int i = 0; i < column.size(); i++)
				if (column.get(i).equals(destroyedShip)) {
					column.get(i).destroy();
					this.logger.info("Destroyed ship in ("
							+ this.enemyShips.indexOf(column) + "," + i + ")");
				}

		// Updates the list of ships that can shoot the player.
		if (this.shooters.contains(destroyedShip) && destroyedShip.isDestroyed()) {
			int destroyedShipIndex = this.shooters.indexOf(destroyedShip);
			int destroyedShipColumnIndex = -1;

			for (List<EnemyShip> column : this.enemyShips)
				if (column.contains(destroyedShip)) {
					destroyedShipColumnIndex = this.enemyShips.indexOf(column);
					break;
				}

			EnemyShip nextShooter = getNextShooter(this.enemyShips
					.get(destroyedShipColumnIndex));

			if (nextShooter != null)
				this.shooters.set(destroyedShipIndex, nextShooter);
			else {
				this.shooters.remove(destroyedShipIndex);
				this.logger.info("Shooters list reduced to "
						+ this.shooters.size() + " members.");
			}
		}

		if (destroyedShip.isDestroyed()) this.shipCount--;
	}

	/**
	 * Gets the ship on a given column that will be in charge of shooting.
	 * 
	 * @param column
	 *            Column to search.
	 * @return New shooter ship.
	 */
	public final EnemyShip getNextShooter(final List<EnemyShip> column) {
		Iterator<EnemyShip> iterator = column.iterator();
		EnemyShip nextShooter = null;
		while (iterator.hasNext()) {
			EnemyShip checkShip = iterator.next();
			if (checkShip != null && !checkShip.isDestroyed())
				nextShooter = checkShip;
		}

		return nextShooter;
	}

	/**
	 * Returns an iterator over the ships in the formation.
	 * 
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public final Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShipsList = new HashSet<EnemyShip>();

		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				enemyShipsList.add(enemyShip);

		return enemyShipsList.iterator();
	}

	/**
	 * Checks if there are any ships remaining.
	 * 
	 * @return True when all ships have been destroyed.
	 */
	public final boolean isEmpty() {
		return this.shipCount <= 0;
	}

	public Logger getLogger() {return this.logger;}

	public void setLogger(Logger logger) {this.logger = logger;}
}
