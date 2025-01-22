package island.entity.creature;

import island.Settings;
import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Location {
    private final CopyOnWriteArrayList<Animal> animals = new CopyOnWriteArrayList<>();
    private final Plant plant = new Plant();


    public List<Animal> getAnimals(){
        return animals;
    }

    public Plant getPlant(){
        return plant;
    }

    public void addAnimal(Animal animal){
        if(animals.size()<animal.maxNumberCell){
            animals.add(animal);
        }else {
            System.out.println("Клетка переполнена для " + animal.getClass().getSimpleName()); // Удалить после проверки
        }
    }
    public boolean canAddAnimal (Animal animal){
        long sameSpeciesCount = animals.stream()
                .filter(a -> a.getClass().equals(animal.getClass()))
                .count();
        return sameSpeciesCount<animal.getMaxInCell();
    }
    public void removeAnimal (Animal animal){
        animals.remove(animal);
    }
    public Animal getRandomHerbivore(){
        List<Animal> herbivores = animals.stream()
                .filter(a-> a instanceof Herbivore)
                .toList();
        return herbivores.isEmpty()? null : herbivores.get(new Random().nextInt(herbivores.size()));
    }
    public void growPlants(){
        plant.grow();
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //подсчитываем количество каждого вида животного
        Map<Class<?>,Long> animalCounts = animals.stream()
                .collect(Collectors.groupingBy(Animal::getClass, Collectors.counting()));
        //добавляем информацию о животных
        animalCounts.forEach((animalClass, count) -> {
            String icon = Settings.ENTITY_ICONS.getOrDefault(animalClass, "?");
            builder.append(icon).append(":").append(count).append(" ");
        });
        builder.append("Plants: ").append(plant.getCount());
        return builder.toString();

    }
}
