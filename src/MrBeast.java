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
    private PriorityQueue<Point> targetQ;

    public MrBeast(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;

        Comparator<Point> comp = (p1, p2) -> (int) (p1.distance(this.getPosition()) - p2.distance(this.getPosition()));
        targetQ = new PriorityQueue<>(comp);
    }

    public void executeMrBeastActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (targetQ.isEmpty()){
            wander(world, scheduler);
        }

        else if (moveToMrBeast(world, targetQ, scheduler)) {
            System.out.println(targetQ);
            Entity sapling = Functions.createSapling(Functions.getSaplingKey() + "_" + targetQ.peek().toString(), targetQ.peek(), imageStore.getImageList(Functions.getSaplingKey()), 0);
            targetQ.poll();
            world.addEntity(sapling);
            ((Animate)sapling).scheduleActions(scheduler, world, imageStore);
            //offClicked();
        }
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToMrBeast(WorldModel world, Queue<Point> target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), targetQ.peek())) {
            return true;
        }else{
            updateQueue(world);
            if(targetQ.isEmpty()) return false;
            System.out.println("\n" + targetQ);
            System.out.println(targetQ.peek());
            Point nextPos = nextPositionMrBeast(world, targetQ.peek());

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

    public boolean onClicked(WorldModel world, Point pressed) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(
                getPosition(),
                pressed,
                p1 -> world.withinBounds(p1) && !world.isOccupied(p1),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if(path == null) return false;
        pressed.setPathLen(path.size());
        targetQ.add(pressed);
        System.out.println("added point: " + pressed);
        System.out.println(getPosition() + "--" + targetQ);
        return true;
    }


    public void updateQueue(WorldModel world) {
        Comparator<Point> comp = Comparator.comparingInt(Point::getPathLen);
        PriorityQueue<Point> temp = new PriorityQueue<>(comp);

        PathingStrategy strat = new AStarPathingStrategy();
        for(Point p: targetQ) {

            List<Point> path = strat.computePath(
                    getPosition(),
                    p,
                    p1 -> world.withinBounds(p1) && !world.isOccupied(p1),
                    Functions::adjacent,
                    PathingStrategy.CARDINAL_NEIGHBORS);
            Point p1 = p;

            if(path != null && !temp.contains(p1)) {
                p1.setPathLen(path.size());
                temp.add(p1);
            }
        }
        System.out.print("updated queue: ");
        System.out.println(getPosition() + "----" + temp);
        targetQ = temp;
    }

}
