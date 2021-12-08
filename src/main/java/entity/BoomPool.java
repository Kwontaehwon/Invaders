package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BoomPool implements Serializable {

    /** Set of already created booms. */
    private static Set<Boom> pool = new HashSet<>();

    /** Constructor, not called */
    private BoomPool() {

    }

    /**
     * Returns a boom from the pool if one is available, a new one if there
     * isn't.
     *
     * @param positionX
     *            Requested position of the boom in the X axis.
     * @param positionY
     *            Requested position of the boom in the Y axis.
     * @param speedX
     *            Requested speed of the boom, positive or negative depending
     *            on direction - positive is down.
     * @param speedY
     * 			  Requested speed of the boom, positive or negative depending
     * 	 *            on direction - positive is down.
     * @return Requested boom.
     */
    public static Boom getBoom(final int positionX,
                                   final int positionY, final int speedX , final int speedY) {
        Boom boom;
        if (!pool.isEmpty()) {
            boom = pool.iterator().next();
            pool.remove(boom);
            boom.setPositionX(positionX - boom.getWidth() / 2);
            boom.setPositionY(positionY);
            boom.setSpeed(speedX,speedY);

        } else {
            boom = new Boom(positionX, positionY, speedX,speedY);
            boom.setPositionX(positionX - boom.getWidth() / 2);
        }
        return boom;
    }


    /**
     * Adds one or more booms to the list of available ones.
     *
     * @param boom
     *            Booms to recycle.
     */
    public static void recycle(final Set<Boom> boom) {
        pool.addAll(boom);
    }
}
