import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Health{
    private final double actionPeriod;
    private final int resourceLimit;

    public Dude(String id, Point position, List<PImage> images, double animationPeriod, int resourceLimit, double actionPeriod, int health) {
        super(id, position, images, animationPeriod, health);
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
    }

    public Point nextPositionDude(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(
                getPosition(),
                destPos,
                p1 -> world.withinBounds(p1) && (!world.isOccupied(p1) || world.getOccupancyCell(p1).getClass() == Stump.class),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if(path == null)
            return getPosition();
        return path.get(0);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), getActionPeriod());
        super.scheduleActions(scheduler, world, imageStore);
    }

    public double getActionPeriod() {
        return actionPeriod;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }
}
