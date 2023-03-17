import processing.core.PImage;

import java.util.List;
import java.util.stream.Collectors;

public class Trash extends Animate{
    private final double actionPeriod;
    public Trash(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }
    public void executeTrashActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        nextPositionTrash(world, imageStore, scheduler);
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public void nextPositionTrash(WorldModel world, ImageStore imageStore,EventScheduler scheduler) {
        Point down = new Point(getPosition().x, getPosition().y + 1);
        Point right = new Point(getPosition().x + 1, getPosition().y);
        Point up = new Point(getPosition().x, getPosition().y - 1);

        if(world.withinBounds(right) && world.getOccupancyCell(right) != null && world.getOccupancyCell(right).getClass() == Water.class) {
            if((int) (Math.random() * 10) ==5) {
                world.swapEntity(this, world.getOccupancyCell(right));
            }
        }
        if(world.withinBounds(down) && world.getOccupancyCell(down) != null && world.getOccupancyCell(down).getClass() == Water.class) {
            if((int) (Math.random() * 15) == 5) {
                world.swapEntity(this, world.getOccupancyCell(down));
            }
        }
        else if(world.withinBounds(up) && world.getOccupancyCell(up) != null && world.getOccupancyCell(up).getClass() == Water.class) {
            if((int) (Math.random() * 100) == 5) {
                world.swapEntity(this, world.getOccupancyCell(up));
            }
        }
    }

}
