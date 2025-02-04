package island_v2.entity;

import island_v2.Settings;
import island_v2.entity.creature.Animal;
import island_v2.entity.creature.AnimalFactory;
import island_v2.entity.creature.Location;
import island_v2.entity.creature.animal.predactor.Predator;
import island_v2.processors.AnimalProcessor;
import island_v2.processors.PlantProcessor;
import island_v2.service.StatisticsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Island {
    private final Location[][] locations;
    private final Lock lock = new ReentrantLock(); // Глобальная блокировка для острова


    public Island (int columnsCount, int rowsCount){
        locations = new Location[columnsCount][rowsCount];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j] = new Location(i,j);
            }
        }
    }

    public Location getLocation(int columnsCount, int rowsCount){
        if (columnsCount >= 0 && columnsCount < locations.length && rowsCount >= 0 && rowsCount < locations[0].length) {
            return locations[columnsCount][rowsCount];
        }
        throw new IllegalArgumentException("Некорректные координаты: x=" + columnsCount + ", y=" + rowsCount);
    }
    public int getWidth(){
        return locations.length;
    }
    public int getHeight(){
        return  locations[0].length;
    }


    private static boolean allAnimalsCreated(Map<Class<? extends Animal>, Integer> counts,
                                             Map<Class<? extends Animal>, Integer> limits) {
        return counts.entrySet().stream().allMatch(entry -> entry.getValue() >= limits.get(entry.getKey()));
    }
    //логика заселения животных
    public static void populateIsland (Island island){
        Random random = new Random();
        //Счетчик создания животных
        Map<Class<?extends Animal>,Integer> animalCounts = new HashMap<>();
        Settings.INITIAL_COUNT.keySet().forEach(type -> animalCounts.put(type,0));
        //Инициализация клеток
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                Location location = island.getLocation(x,y);
                // Случайное количество травы в пределах от 0 до countMAX
                int initialPlants = random.nextInt(location.getPlant().getCountMAX());
                location.getPlant().setCount(initialPlants);// Метод для установки начального значения
            }
        }
        // Заселение животных
        while (!allAnimalsCreated(animalCounts,Settings.INITIAL_COUNT)){
            int x = random.nextInt(island.getWidth());
            int y = random.nextInt(island.getHeight());
            Location location = island.getLocation(x,y);
            for (Class<?extends Animal> animalType : Settings.INITIAL_COUNT.keySet()){
                if (animalCounts.get(animalType)<Settings.INITIAL_COUNT.get(animalType)){
                    Animal newAnimal = AnimalFactory.create(animalType);
                    if(location.canAddAnimal(newAnimal)){
                        location.addAnimal(newAnimal);
                        animalCounts.put(animalType,animalCounts.get(animalType)+1);
                        //System.out.println("Создано: " + animalType.getSimpleName() + " в локации (" + x + ", " + y + ")");
                    }
                }
            }
        }
    }



}
