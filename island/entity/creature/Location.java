package island.entity.creature;

import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.plant.Plant;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;


public class Location {
    private final Queue<Animal> animals = new ConcurrentLinkedQueue<>();
    private final Plant plant = new Plant();
    private  final ReentrantLock lock = new ReentrantLock();

    public void lock(){
        lock.lock();
    }
    public void unlock(){
        lock.unlock();
    }

    public Queue<Animal> getAnimals(){
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
        return herbivores.isEmpty()? null : herbivores.get(ThreadLocalRandom.current().nextInt(herbivores.size()));
    }
    public void growPlants(){
        plant.grow();
    }
}
