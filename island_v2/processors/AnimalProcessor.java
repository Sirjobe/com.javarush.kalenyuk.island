package island_v2.processors;

import island_v2.entity.Island;
import island_v2.entity.creature.Animal;
import island_v2.entity.creature.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalProcessor {
    private final Island island;
    private final ExecutorService executor;

    public AnimalProcessor(Island island){
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
            location.getAnimals().forEach(animal -> {
                if(animal.isDead()){
                    location.removeAnimal(animal);
                    return;
                }
                animal.eat(location);
                animal.move(island,location.getX(),location.getY());
            });
            Animal.reproduce(location);
        }
    }
    public void shutdown(){
        executor.shutdown();
    }
}
