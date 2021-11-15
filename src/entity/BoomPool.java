package entity;

import java.util.HashSet;
import java.util.Set;

//BulletPool.java와 똑같음.
public class BoomPool {

    private static Set<Boom> pool = new HashSet<Boom>();

    private BoomPool() {

    }

    public static Boom getBoom(final int positionX,
                                   final int positionY, final int speed) {
        Boom boom;
        if (!pool.isEmpty()) {
            boom = pool.iterator().next(); // 객체로 Set의 모든 아이템을 순회(for loop 같은느낌)
            pool.remove(boom); //있으면 삭제.
            boom.setPositionX(positionX - boom.getWidth() / 2);
            boom.setPositionY(positionY);
            boom.setSpeed(speed);
            //boom.setSprite();

        } else {
            boom = new Boom(positionX, positionY, speed);
            boom.setPositionX(positionX - boom.getWidth() / 2);
        }
        return boom;
    }

    public static void recycle(final Set<Boom> boom) {
        pool.addAll(boom);
    }
}
