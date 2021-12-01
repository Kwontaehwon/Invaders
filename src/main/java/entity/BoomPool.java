package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BoomPool implements Serializable {

    private static Set<Boom> pool = new HashSet<Boom>();

    private BoomPool() {

    }

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



    public static void recycle(final Set<Boom> boom) {
        pool.addAll(boom);
    }
}
