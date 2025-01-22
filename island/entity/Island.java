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
    public void display(){
        clearConsole();
        System.out.println("Island State:");
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Location location = getLocation(x, y);
                Map<Class<?>, String> icons = Settings.ENTITY_ICONS;

                StringBuilder cellRepresentation = new StringBuilder();
                Map<String, Integer> counts = new HashMap<>();

                location.getAnimals().forEach(animal -> {
                    String icon = icons.getOrDefault(animal.getClass(), "?");
                    counts.put(icon, counts.getOrDefault(icon, 0) + 1);
                });

                counts.forEach((icon, count) -> cellRepresentation.append(icon).append(count).append(" "));

                if (location.getPlant().getCount() > 0) {
                    String plantIcon = icons.getOrDefault(location.getPlant().getClass(), "?");
                    cellRepresentation.append(plantIcon).append(location.getPlant().getCount());
                }

                System.out.print("[" + cellRepresentation.toString().trim() + "]");
            }
            System.out.println();
        }

    }
    private void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
