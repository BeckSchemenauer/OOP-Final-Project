/**
 * An action that can be taken by an entity
 */
public final class Activity extends Action{
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        if (getEntity() instanceof Plant) {
            ((Plant) getEntity()).executePlantActivity(world, imageStore, scheduler);
        } else if (getEntity().getClass().equals(Fairy.class)) {
            ((Fairy) getEntity()).executeFairyActivity(world, imageStore, scheduler);
        } else if (getEntity().getClass().equals(MrBeast.class)) {
            ((MrBeast) getEntity()).executeMrBeastActivity(world, imageStore, scheduler);
            //System.out.println("CASE MrBeast");
        } else if (getEntity().getClass().equals(DudeNotFull.class)) {
            ((DudeNotFull) getEntity()).executeDudeNotFullActivity(world, imageStore, scheduler);
        } else if (getEntity().getClass().equals(DudeFull.class)) {
            ((DudeFull) getEntity()).executeDudeFullActivity(world, imageStore, scheduler);
        } else {
            throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", getEntity().getClass()));
        }
    }
}
