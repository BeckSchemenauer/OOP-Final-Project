import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class MarkRoberFull extends Animate {
    private final double actionPeriod;
    private boolean clicked;
    private Entity target;
    public MarkRoberFull(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        clicked = false;
        target = null;
        System.out.println("full is made");
    }

    public void executeMarkRoberFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = Functions.findNearest(world, getPosition(), new ArrayList<>(List.of(Windmill.class)));

        if (target.isPresent() && moveToMarkRober(world, target.get().getPosition(), scheduler)) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        }
    }
    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity mark = Functions.createMarkRober(getId(), getPosition(), actionPeriod, getAnimationPeriod(), imageStore.getImageList(Functions.MARKROBER_KEY));

        world.removeEntity(scheduler, this);

        world.addEntity(mark);
        ((Animate)mark).scheduleActions(scheduler, world, imageStore);
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

}
