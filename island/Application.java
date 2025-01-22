package island;

import island.entity.Island;
import island.entity.creature.Animal;
import island.entity.creature.Location;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
        //Заселяем остров
        Island.populateIsland(island);
        //Создаем пул потоков
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        int maxCycles = 100;
        Runnable simulationTask = new Runnable() {
            int cycle = 0;

            @Override
            public void run() {
                if (cycle >= maxCycles || !island.hasAliveAnimals()) {
                    System.out.println("Симуляция окончена");
                    executor.shutdown();
                    executor.close();
                    return;
                }
                System.out.println("Такт: " + cycle + 1);
                for (int x = 0; x < island.getWidth(); x++) {
                    for (int y = 0; y < island.getHeight(); y++) {
                        Location location = island.getLocation(x, y);
                        int finalX = x;
                        int finalY = y;
                        location.getAnimals().forEach(animal -> {
                            animal.eat(location);
                            animal.reproduce(location);
                            animal.move(island, finalX, finalY);
                        });
                        location.getAnimals().removeIf(Animal::isDead);
                        location.growPlants();
                    }
                }
                island.display();
                cycle++;
            }
        };
        executor.scheduleAtFixedRate(simulationTask, 0, 10, TimeUnit.SECONDS);
    }
}






