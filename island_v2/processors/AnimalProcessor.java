package island_v2.processors;

import island_v2.entity.Island;
import island_v2.entity.creature.Animal;
import island_v2.entity.creature.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AnimalProcessor implements Runnable{
    private final Island island;
    private final ExecutorService processorExecutor;

    public AnimalProcessor(Island island) {
        this.island = island;
        this.processorExecutor = Executors.newWorkStealingPool();
    }

    @Override
    public void run() {
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                final int finalX = x;
                final int finalY = y;
                processorExecutor.submit(() -> processLocation(finalX, finalY));
            }
        }
    }

    private void processLocation(int x, int y) {
        Location location = island.getLocation(x, y);
        if (location.tryLock()) {
            try {
                location.getAnimals().removeIf(Animal::isDead);
                location.getAnimals().forEach(animal -> {
                    animal.eat(location);
                    animal.move(island, x, y);
                });
                Animal.reproduce(location);
            } finally {
                location.unlock();
            }
        }
    }

}



