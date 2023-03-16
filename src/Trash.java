import processing.core.PImage;

import java.util.List;

public class Trash extends Animate{
    private final double actionPeriod;
    public Trash(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }
}
