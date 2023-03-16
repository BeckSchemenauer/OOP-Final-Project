import processing.core.PImage;

import java.util.List;

public abstract class Animate extends Entity{
    private final double animationPeriod;

    public Animate(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {return animationPeriod;}
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createAnimationAction(this, 0), this.getAnimationPeriod());
    }
    public Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }
    public Action createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {return new Activity(entity, world, imageStore);}

}
