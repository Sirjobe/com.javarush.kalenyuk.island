package island_v1.entity;

import island_v1.Settings;
import island_v1.entity.creature.Animal;
import island_v1.entity.creature.AnimalFactory;
import island_v1.entity.creature.Location;
import island_v1.entity.creature.animal.predactor.Predator;
import island_v1.entity.creature.plant.Plant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    public void interact(){
        // Аул потоков для параллельной обработки локаций
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int x=0; x<getWidth(); x++){
            for (int y=0; y<getHeight();y++){
                final int currentX = x; // Создаём локальную копию
                final int currentY = y; // Создаём локальную копию
                executor.submit(()->{
                    Location location = getLocation(currentX,currentY);
                    synchronized (location) {
                        location.getAnimals().forEach(animal -> {
                            if (animal.isDead()) {
                                location.removeAnimal(animal);
                                return;
                            }
                            animal.eat(location);
                        });
                        // Обработка размножения для всех животных
                        Animal.reproduce(location);
                        // Перемещение животных
                        location.getAnimals().forEach(animal -> animal.move(this,currentX,currentY));
                        // Удаление мертвых животных
                        location.getAnimals().removeIf(Animal::isDead);
                        //Рост растений
                        location.growPlants();
                    }
                });
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean checkGameOver() {
        boolean allAnimalsDead = true;
        boolean onlyHerbivoresLeft = true;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Location location = getLocation(x, y);
                synchronized (location) {
                    if (!location.getAnimals().isEmpty()) {
                        allAnimalsDead = false; // Есть живые животные

                        // Проверяем, есть ли хищники
                        for (Animal animal : location.getAnimals()) {
                            if (animal instanceof Predator) {
                                onlyHerbivoresLeft = false; // Найден хищник
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Условие завершения: все животные умерли или остались только травоядные
        return allAnimalsDead || onlyHerbivoresLeft;
    }
    public void display(){
        Map<Class<?extends Animal>,Integer> animalCounts = new HashMap<>();
        int totalPlants = 0;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Location location = getLocation(i,j);
                synchronized (location){
                    //Считаем растения
                    totalPlants +=location.getPlant().getCount();
                    //Считаем животных
                    location.getAnimals().forEach(animal -> {
                        Class<?extends Animal> type = animal.getClass();
                        animalCounts.put(type,animalCounts.getOrDefault(type,0)+1);
                    });
                }
            }
        }
        // Вывод на экран
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

                //System.out.print("[" + cellRepresentation.toString().trim() + "]");
            }

            //System.out.println();
        }
        // Выводим общую статистику
        System.out.println("Общая статистика:");
        animalCounts.forEach((animalClass,count)->{
            String name = animalClass.getSimpleName();
            String icon = Settings.ENTITY_ICONS.getOrDefault(animalClass,"?");
            System.out.printf("%s %s: %d\n",icon,name,count);
        });
        System.out.printf("Растения %s: %d\n\n", Settings.ENTITY_ICONS.get(Plant.class),totalPlants);

    }




}
