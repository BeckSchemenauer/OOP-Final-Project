import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class MarkRober extends Animate {
    private final double actionPeriod;
    private boolean clicked;
    private Entity target;
    public MarkRober(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        clicked = false;
        target = null;
    }

    public void executeMarkRoberActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!clicked){
            if(!wander(world, scheduler)) System.out.println("MrBeast is stuck!");
        }

        else if (world.getOccupancyCell(target.getPosition()).getClass() == Trash.class && moveToMarkRober(world, target.getPosition(), scheduler)) {
            //remove trash
            offClicked();
        }
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToMarkRober(WorldModel world, Point target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target)) {
            return true;
        }else{
            Point nextPos = nextPositionMarkRober(world, target);
            if (target.equals(getPosition()))
                return false;

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionMarkRober(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(
                getPosition(),
                destPos,
                p1 -> world.withinBounds(p1) && !world.isOccupied(p1),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if(path == null)
            return getPosition();
        return path.get(0);
    }

    private boolean wander(WorldModel world, EventScheduler scheduler) {
        List<Point> neighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition()).filter(p1 -> world.withinBounds(p1) && !world.isOccupied(p1)).collect(Collectors.toList());

        if(neighbors.isEmpty()) return false;
        int randIndex = (int) (Math.random() * neighbors.size());
        if((int) (Math.random() * 10) == 5)
            world.moveEntity(scheduler, this, neighbors.get(randIndex));
        return true;
    }

    public void onClicked(Entity pressed) {
        clicked = true;
        target = pressed;
    }
    public void offClicked() {
        clicked = false;
        target = null;
    }

}
