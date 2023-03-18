import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Health {
    private final double actionPeriod;

    public Plant(String id, Point position, List<PImage> images,double actionPeriod, double animationPeriod, int health) {
        super(id, position, images, animationPeriod, health);
        this.actionPeriod = actionPeriod;
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getHealth() <= 0) {
            Entity stump = Functions.createStump(Functions.getStumpKey() + "_" + getId(), getPosition(), imageStore.getImageList(Functions.getStumpKey()));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);
            if(world.getBackgroundCell(stump.getPosition()).getId().contains(Functions.getSwampKey())) ((Stump)stump).spawnSwamp(world, stump.getPosition(), imageStore);

            return true;
        }
        return false;
    }

    public void executePlantActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), getActionPeriod());}
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public double getActionPeriod() {return actionPeriod;}
}
