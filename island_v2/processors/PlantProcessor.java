package island_v2.processors;
import island_v2.entity.Island;
import island_v2.entity.creature.Location;
import island_v2.entity.creature.plant.Plant;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlantProcessor implements Runnable{
    private final Island island;
    private final ExecutorService processorExecutor;

    public PlantProcessor(Island island) {
        this.island = island;
        this.processorExecutor = Executors.newWorkStealingPool();
    }

    @Override
    public void run() {
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                final int finalX = x;
                final int finalY = y;
                processorExecutor.submit(() -> {
                    Location location = island.getLocation(finalX, finalY);
                    location.getPlant().grow(location);
                });
            }
        }
    }

}
