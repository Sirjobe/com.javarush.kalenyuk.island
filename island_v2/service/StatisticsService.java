package island_v2.service;

import island_v2.Settings;
import island_v2.entity.Island;
import island_v2.entity.creature.Animal;
import island_v2.entity.creature.Location;
import island_v2.entity.creature.plant.Plant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsService {
    private final Island island;

    public  StatisticsService(Island island){
        this.island=island;
    }
    public void display(){
        Map<Class<?extends Animal>,Integer> animalCounts = new ConcurrentHashMap<>();
        int totalPlants = 0;
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                Location location = island.getLocation(x,y);
                synchronized (location){
                    //Считаем растения
                    totalPlants +=location.getPlant().getCount();
                    //Считаем животных
//                    int finalX = x;
//                    int finalY = y;
                    location.getAnimals().forEach(animal -> {
                        Class<?extends Animal> type = animal.getClass();
                        animalCounts.put(type,animalCounts.getOrDefault(type,0)+1);
//                          System.out.println("Найдено животное: " + type.getSimpleName() + " в локации (" + finalX + ", " + finalY + ")");
                    });
                }
            }
        }
                // Вывод на экран
//        for (int x = 0; x < island.getWidth(); x++) {
//            for (int y = 0; y < island.getHeight(); y++) {
//                Location location = island.getLocation(x, y);
//                Map<Class<?>, String> icons = Settings.ENTITY_ICONS;
//
//                StringBuilder cellRepresentation = new StringBuilder();
//                Map<String, Integer> counts = new HashMap<>();
//
//                location.getAnimals().forEach(animal -> {
//                    String icon = icons.getOrDefault(animal.getClass(), "?");
//                    counts.put(icon, counts.getOrDefault(icon, 0) + 1);
//                });
//
//                counts.forEach((icon, count) -> cellRepresentation.append(icon).append(count).append(" "));
//
//                if (location.getPlant().getCount() > 0) {
//                    String plantIcon = icons.getOrDefault(location.getPlant().getClass(), "?");
//                    cellRepresentation.append(plantIcon).append(location.getPlant().getCount());
//                }
//
//                System.out.print("[" + cellRepresentation.toString().trim() + "]");
//            }
//
//            System.out.println();
//        }
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
