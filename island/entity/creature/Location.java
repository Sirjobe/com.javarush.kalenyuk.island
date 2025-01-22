package island.entity.creature;

import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.plant.Plant;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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
}
