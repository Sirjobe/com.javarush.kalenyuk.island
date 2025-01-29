package island_v1.entity.creature;

import island_v1.entity.creature.plant.Plant;

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
    public Animal getRandomAnimal() {
        synchronized (animals) {
            if (animals.isEmpty()) return null;
            List<Animal> animalList = new ArrayList<>(animals);
            int randomIndex = ThreadLocalRandom.current().nextInt(animalList.size());
            return animalList.get(randomIndex);
        }
    }

    public void growPlants(){
        plant.grow();
    }
}
