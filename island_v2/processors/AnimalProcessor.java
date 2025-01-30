package island_v2.processors;

import island_v2.entity.Island;
import island_v2.entity.creature.Animal;
import island_v2.entity.creature.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalProcessor {
    private final Island island;
    private final ExecutorService executor = Executors.newWorkStealingPool();
    int finalX;
    int finalY;

    public AnimalProcessor(Island island){
        this.island = island;
    }
    public void process() {
        executor.submit(() -> {
            for (int x = 0; x < island.getWidth(); x++) {
                for (int y = 0; y < island.getHeight(); y++) {
                    Location location = island.getLocation(x, y);
                    location.lock();
                    try {
                        //Удаляем мертвых животных
                        location.getAnimals().removeIf(Animal::isDead);
                        finalX = x;
                        finalY = y;
                        location.getAnimals().forEach(animal -> {
                            animal.eat(location);
                            animal.move(island, finalX, finalY);
                        });
                        Animal.reproduce(location);
                    } finally {
                        location.unlock();
                    }
                }
            }
        });
    }

}
