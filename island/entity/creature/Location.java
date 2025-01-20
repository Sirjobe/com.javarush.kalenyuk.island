package island.entity.creature;

import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Location {
    private List<Animal> animals = new ArrayList<>();
    private Plant plant = new Plant();


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
    public void removeAnimal (Animal animal){
        animals.remove(animal);
    }
    public Animal getRandomHerbivore(){
        List<Animal> herbivores = animals.stream()
                .filter(a-> a instanceof Herbivore)
                .toList();
        return herbivores.isEmpty()? null : herbivores.get(new Random().nextInt(herbivores.size()));
    }
}
