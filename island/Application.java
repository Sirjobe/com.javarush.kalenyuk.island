package island;

import island.entity.Island;
import island.entity.creature.Animal;
import island.entity.creature.AnimalFactory;
import island.entity.creature.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH,Settings.ISLAND_HEIGHT);
        //Заселяем остров
        populateIsland(island);
        int maxCycles = 100;
        int cycle = 0;
        //Создаем пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (cycle<maxCycles) {
            System.out.println("Такт: " + (cycle + 1));
            // Выполнение действий для каждой клетки параллельно
            for (int x = 0; x < island.getWidth(); x++) {
                for (int y = 0; y < island.getHeight(); y++) {
                    Location location = island.getLocation(x, y);
                    executor.submit(() -> processLocation(location, island, x, y));
                }
            }

            // Ждём завершения всех задач
            executor.shutdown();
            while (!executor.isTerminated()) {
                //ждем завершения
            }
            //Проверяем, остались ли животные
            boolean animalsAlive = island.hasAliveAnimals();
            if (!animalsAlive) {
                System.out.println("Все животные вымерли. Симуляция завершена.");
                break;
            }
            cycle++;
        }
            if (cycle >= maxCycles) {
                System.out.println("Максимальное количество тактов достигнуто");
            }
    }
    private static void processLocation(Location location, Island island, int x, int y){
        location.getAnimals().forEach(animal -> {
            animal.eat(location);
            animal.reproduce(location);
            animal.move(island,x,y);
        });
        //удаляем мертвых животных
        location.getAnimals().removeIf(animal -> {
            if(animal.isDead()){
                System.out.println(animal.getClass().getSimpleName()+"умер.");
                return true;
            }
            return false;
        });
        location.growPlants();
    }
    private static boolean allAnimalsCreated(Map<Class<? extends Animal>, Integer> counts,
                                             Map<Class<? extends Animal>, Integer> limits) {
        return counts.entrySet().stream().allMatch(entry -> entry.getValue() >= limits.get(entry.getKey()));
    }
    //логика заселения животных
    private static void populateIsland (Island island){
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
                    }
                }
            }
        }
    }

}






