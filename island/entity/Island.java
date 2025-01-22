package island.entity;

import island.Settings;
import island.entity.creature.Animal;
import island.entity.creature.AnimalFactory;
import island.entity.creature.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Island {
    private Location[][] locations;


    public Island (int columnsCount, int rowsCount){
        locations = new Location[columnsCount][rowsCount];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j] = new Location();
            }
        }
    }

    public Location getLocation(int columnsCount, int rowsCount){
        return locations[columnsCount][rowsCount];
    }
    public int getWidth(){
        return locations.length;
    }
    public int getHeight(){
        return  locations[0].length;
    }
    public boolean hasAliveAnimals(){
        for (Location[] row : locations){
            for (Location location : row){
                if(!location.getAnimals().isEmpty()){
                    return true;
                }
            }
        }
        return false;
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

    public void display(){
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                System.out.print(locations[i][j]+"|");
            }
            System.out.println("|");
        }
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
                    }
                }
            }
        }
    }


}
