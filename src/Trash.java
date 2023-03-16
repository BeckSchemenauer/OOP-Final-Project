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
        if (nextPositionTrash(world, imageStore, scheduler)) {

        }
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(this, world, imageStore), actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean nextPositionTrash(WorldModel world, ImageStore imageStore,EventScheduler scheduler) {
        Point down = new Point(getPosition().x, getPosition().y + 1);
        Point right = new Point(getPosition().x + 1, getPosition().y);

        if(world.withinBounds(down) && world.getOccupancyCell(down) != null && world.getOccupancyCell(down).getClass() == Water.class) {
            if((int) (Math.random() * 10) == 5) {
                Entity water = new Water(Functions.getWaterKey(), getPosition(), imageStore.getImageList(Functions.getWaterKey()), Functions.getWaterAnimationPeriod());

                world.addEntity(water);

                world.moveEntity(scheduler, this, down);
            }
        }
        else if(world.withinBounds(right) && world.getOccupancyCell(right) != null && world.getOccupancyCell(right).getClass() == Water.class) {
            if((int) (Math.random() * 10) == 5) {
                Entity water = new Water(Functions.getWaterKey(), getPosition(), imageStore.getImageList(Functions.getWaterKey()), Functions.getWaterAnimationPeriod());

                world.addEntity(water);

                world.moveEntity(scheduler, this, right);
            }
        }
        return true;
    }

}
