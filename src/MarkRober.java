import java.util.*;
import java.util.stream.Collectors;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class MarkRober extends Animate {
    private final double actionPeriod;
    private int resourceCount;
    private PriorityQueue<Trash> targetQ;
    public MarkRober(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;

        Comparator<Trash> comp = (e1, e2) -> (int) (e1.getPosition().distance(this.getPosition()) - e2.getPosition().distance(this.getPosition()));
        targetQ = new PriorityQueue<>(comp);
    }

    public void executeMarkRoberActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (targetQ.isEmpty()){
            wander(world, scheduler);
        }

        else if (world.getOccupancyCell(targetQ.peek().getPosition()).getClass() == Trash.class && moveToMarkRober(world, targetQ, scheduler)) {
            Point source = new Point(5, 8);
            if(world.isOccupied(source)){
                source = new Point(5, 7);
                if(world.isOccupied(source)) {
                    source = new Point(6, 7);
                    if(world.isOccupied(source)) {
                        source = new Point(7, 7);
                    }
                }
            }
            world.swapEntity(targetQ.poll(), world.getOccupancyCell(source));
            //System.out.println(targetQ.poll() + " picked up");
            resourceCount++;
            if(resourceCount>4)transform(world, scheduler, imageStore);
        }
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity mark = Functions.createMarkRoberFull(getId(), getPosition(), actionPeriod, getAnimationPeriod(), imageStore.getImageList(Functions.MARKROBERFULL_KEY));

        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(mark);
        ((Animate)mark).scheduleActions(scheduler, world, imageStore);

    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToMarkRober(WorldModel world, Queue<Trash> target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), targetQ.peek().getPosition())) {
            return true;
        }else{
            updateQueue(world);
            if(targetQ.isEmpty())
                return false;
            //System.out.println("\n" + targetQ);
            //System.out.println(targetQ.peek());
            Point nextPos = nextPositionMarkRober(world, targetQ.peek().getPosition());

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

    private void wander(WorldModel world, EventScheduler scheduler) {
        List<Point> neighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition()).filter(p1 -> world.withinBounds(p1) && !world.isOccupied(p1)).collect(Collectors.toList());

        if(neighbors.isEmpty()) return;
        int randIndex = (int) (Math.random() * neighbors.size());
        if((int) (Math.random() * 10) == 5)
            world.moveEntity(scheduler, this, neighbors.get(randIndex));
    }

    public void onClicked(WorldModel world, Point pressed) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(
                getPosition(),
                pressed,
                p1 -> world.withinBounds(p1) && !world.isOccupied(p1),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        Trash t = (Trash)world.getOccupancyCell(pressed);
        if(path != null){t.setPathLen(path.size());} else {t.setPathLen(9999);} // sets pathlen to high number if inaccesible
        targetQ.add((Trash)world.getOccupancyCell(pressed));
        System.out.println("added trash: " + world.getOccupancyCell(pressed));
        //System.out.println(getPosition() + "--" + targetQ);
    }
    public void updateQueue(WorldModel world) {
        Comparator<Trash> comp = Comparator.comparingInt(Trash::getPathLen);
        PriorityQueue<Trash> temp = new PriorityQueue<>(comp);

        PathingStrategy strat = new AStarPathingStrategy();
        for(Trash e: targetQ) {

            List<Point> path = strat.computePath(
                    getPosition(),
                    e.getPosition(),
                    p1 -> world.withinBounds(p1) && !world.isOccupied(p1),
                    Functions::adjacent,
                    PathingStrategy.CARDINAL_NEIGHBORS);
            Point p1 = e.getPosition();
            Trash t = (Trash)world.getOccupancyCell(p1);
            if(!temp.contains(t)) {
                if(path != null){t.setPathLen(path.size());} else {t.setPathLen(9999);}
                temp.add(t);
            }
        }
        System.out.print("updated queue: ");
        System.out.println(getPosition() + "----" + temp);
        targetQ = temp;
    }
}
