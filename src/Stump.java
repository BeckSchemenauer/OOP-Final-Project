import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Stump extends Entity{

    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public void spawnSwamp(WorldModel world, Point pressed, ImageStore imageStore) {
        world.setBackgroundCell(pressed, new Background(Functions.getSwampKey() + "_" + pressed, imageStore.getImageList(Functions.getSwampKey())));
        for(int col = pressed.x - 1; col <= pressed.x + 1; col++) {
            for(int row = pressed.y - 1; row <= pressed.y + 1; row++) {
                Point p = new Point(col, row);
                if(p.equals(pressed)) continue;
                if(world.withinBounds(p) && world.getOccupancyCell(p) != null && world.getOccupancyCell(p).getClass() == Stump.class && !world.getBackgroundCell(p).getId().contains(Functions.getSwampKey())) {
                    ((Stump)world.getOccupancyCell(p)).spawnSwamp(world, p, imageStore);
                }
                else if(world.withinBounds(p) && (world.getOccupancyCell(p) == null || world.getOccupancyCell(p).getClass() != Water.class)) {
                    world.setBackgroundCell(p, new Background(Functions.getSwampKey() + "_" + p, imageStore.getImageList(Functions.getSwampKey())));
                }
            }
        }
    }
}
