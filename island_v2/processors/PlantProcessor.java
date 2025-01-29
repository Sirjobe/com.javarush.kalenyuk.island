package island_v2.processors;

import island_v2.entity.Island;
import island_v2.entity.creature.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlantProcessor {
    private final Island island;
    private final ExecutorService executor;

    public PlantProcessor(Island island){
        this.island = island;
        this.executor = Executors.newWorkStealingPool();
    }
    public void process(){
        for (int x = 0; x < island.getWidth(); x++){
            for (int y = 0; y < island.getHeight(); y++ ){
                Location location = island.getLocation(x,y);
                executor.submit(()-> processLocation(location));
            }
        }
    }
    public void processLocation(Location location){
        synchronized (location){
            location.growPlants();
        }
    }
    public void shutdown(){
        executor.shutdown();
    }
}
