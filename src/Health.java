import processing.core.PImage;

import java.util.List;

public abstract class Health extends Animate{
    private int health;
    public Health(String id, Point position, List<PImage> images, double animationPeriod, int health) {
        super(id, position, images, animationPeriod);
        this.health = health;
    }
    public int getHealth() {return health;}
    public void incrementHealth() {health++;}
    public void decrementHealth() {health--;}

}
