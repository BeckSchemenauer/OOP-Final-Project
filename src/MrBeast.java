import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class MrBeast extends Animate {
    private final double actionPeriod;
    private boolean clicked;
    private Point target;
    public MrBeast(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        clicked = false;
        target = null;
    }

    public void executeMrBeastActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!clicked){
            target = getPosition(); // find something else for MrBeast to do
            if(!wander(world, scheduler)) System.out.println("MrBeast is stuck!");
        }

        else if (moveToMrBeast(world, target, scheduler)) {

            System.out.print("Success");
            offClicked();
        }
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToMrBeast(WorldModel world, Point target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target)) {
            System.out.print("Success?");
            return true;
        }else{
            Point nextPos = nextPositionMrBeast(world, target);
            if (target.equals(getPosition()))
                return false;

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionMrBeast(WorldModel world, Point destPos) {
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

    public void onClicked(Point pressed) {
        clicked = true;
        target = pressed;
    }
    public void offClicked() {
        clicked = false;
        target = null;
    }

}
