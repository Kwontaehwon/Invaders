package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.GameSettings;
import screen.Screen;

import java.util.*;
import java.util.logging.Logger;

public class BossShipFormation implements Iterable<EnemyShip>{
    /** Initial position in the x-axis. */
    private static final int INIT_POS_X = 215;
    /** Initial position in the x-axis. */
    private static final int INIT_POS_Y = 100;
    /** Lateral speed of the formation. */
    private static final int X_SPEED = 8;
    /** Downwards speed of the formation. */
    private static final int Y_SPEED = 4;
    /** Speed of the bullets shot by the members. */
    private static final int BULLET_SPEED = 4;
    /** Proportion of differences between shooting times. */
    private static final double SHOOTING_VARIANCE = .2;
    /** Margin on the sides of the screen. */
    private static final int SIDE_MARGIN = 100;
    /** Margin on the bottom of the screen. */
    private static final int BOTTOM_MARGIN = 80;
    /** Minimum speed allowed. */
    private static final int MINIMUM_SPEED = 10;

    /** DrawManager instance. */
    private DrawManager drawManager;
    /** Application logger. */
    private Logger logger;
    /** Screen to draw ships on. */
    private Screen screen;

    /** List of enemy ships forming the formation. */
    private List<List<EnemyShip>> enemyShips;
    /** Minimum time between shots. */
    private Cooldown shootingCooldown;
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
    private BossShipFormation.Direction currentDirection;
    /** Direction the formation was moving previously. */
    private BossShipFormation.Direction previousDirection;
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

    /** Directions the formation can move. */
    private enum Direction {
        /** Movement to the right side of the screen. */
        RIGHT,
        /** Movement to the left side of the screen. */
        LEFT
    };

    /**
     * Constructor, sets the initial conditions.
     *
     * @param gameSettings
     *            Current game settings.
     */
    public BossShipFormation(final GameSettings gameSettings) {
        this.drawManager = Core.getDrawManager();
        this.logger = Core.getLogger();
        this.enemyShips = new ArrayList<List<EnemyShip>>();
        this.currentDirection = BossShipFormation.Direction.RIGHT;
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
        DrawManager.SpriteType spriteType;

        this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
                + " ship formation in (" + positionX + "," + positionY + ")");

        // Each sub-list is a column on the formation.
        for (int i = 0; i < this.nShipsWide; i++)
            this.enemyShips.add(new ArrayList<EnemyShip>());

        for (List<EnemyShip> column : this.enemyShips) {
            for (int i = 0; i < this.nShipsHigh; i++) {
                spriteType = DrawManager.SpriteType.BossShip1;

                column.add(new EnemyShip((this.enemyShips.indexOf(column))
                        + positionX,
                        + positionY, spriteType));
                this.shipCount++;
            }
        }

        this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
        this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

        this.width = (this.nShipsWide - 1)
                + this.shipWidth;
        this.height = (this.nShipsHigh - 1)
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
        for (List<EnemyShip> column : this.enemyShips)
            for (EnemyShip enemyShip : column)
                drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
                        enemyShip.getPositionY());
    }

    /**
     * Updates the position of the ships.
     */
    public final void update() {
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
        if (movementInterval >= this.movementSpeed) {
            movementInterval = 0;

            boolean isAtBottom = positionY
                    + this.height > screen.getHeight() - BOTTOM_MARGIN;
            boolean isAtRightSide = positionX
                    + this.width >= screen.getWidth() - SIDE_MARGIN;
            boolean isAtLeftSide = positionX <= SIDE_MARGIN;
            boolean isAtHorizontalAltitude = positionY == 0;

            if (currentDirection == BossShipFormation.Direction.LEFT) {
                if (isAtLeftSide)
                    if (!isAtBottom) {
                        previousDirection = currentDirection;
                        currentDirection = BossShipFormation.Direction.RIGHT;
                        this.logger.info("Formation now moving right 1");
                   }
            } else {
                if (isAtRightSide)
                    if (!isAtBottom) {
                        previousDirection = currentDirection;
                        currentDirection = BossShipFormation.Direction.LEFT;
                        this.logger.info("Formation now moving left 2");
                    }
            }

            if (currentDirection == BossShipFormation.Direction.RIGHT)
                movementX = X_SPEED;
            else if (currentDirection == BossShipFormation.Direction.LEFT)
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

            for (List<EnemyShip> column : this.enemyShips)
                for (EnemyShip enemyShip : column) {
                    enemyShip.move(movementX, movementY);
                    enemyShip.update();
                }
        }
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
     * @param bossBullets
     *            Bullets set to add the bullet being shot.
     */
    public final void bossShoot(final Set<BossBullet> bossBullets) {
        // For now, only ships in the bottom row are able to shoot.
        int index = (int) (Math.random() * this.shooters.size());
        EnemyShip shooter = this.shooters.get(index);

        if (this.shootingCooldown.checkFinished()) {
            this.shootingCooldown.reset();
            for(int i = 1; i < 16; i++){
                if(i == 8)
                    continue;
                bossBullets.add(BulletPool.getBossBullet(shooter.getPositionX() + 40
                        + shooter.width / 2, shooter.getPositionY() + 50, BULLET_SPEED, 0));
                bossBullets.add(BulletPool.getBossBullet(shooter.getPositionX() + 40 - 24 + i*3
                        + shooter.width / 2, shooter.getPositionY() + 50, BULLET_SPEED, i));
            }
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

        if (destroyedShip.isDestroyed()) this.shipCount--;
    }

    /**
     * Gets the ship on a given column that will be in charge of shooting.
     *
     * @param column
     *            Column to search.
     * @return New shooter ship.
     */

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
}
