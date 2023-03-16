import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling extends Plant{
    private final int healthLimit;

    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health);
        this.healthLimit = healthLimit;
    }

    public void executePlantActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        incrementHealth();
        super.executePlantActivity(world, imageStore, scheduler);
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (super.transformPlant(world, scheduler, imageStore)) {return true;}
        else if (getHealth() >= healthLimit) {
            Entity tree = Functions.createTree(Functions.getTreeKey() + "_" + getId(), getPosition(), Functions.getNumFromRange(Functions.getTreeActionMax(), Functions.getTreeActionMin()), Functions.getNumFromRange(Functions.getTreeAnimationMax(), Functions.getTreeAnimationMin()), Functions.getIntFromRange(Functions.getTreeHealthMax(), Functions.getTreeHealthMin()), imageStore.getImageList(Functions.getTreeKey()));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            ((Animate)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
