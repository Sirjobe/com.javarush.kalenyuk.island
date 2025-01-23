package island.entity.creature;

import island.Settings;
import island.entity.Island;
import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.animal.predactor.Predator;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Animal implements Eatable {
    protected   double weight; // Вес животного
    protected   int maxNumberCell; // Максимальное количество животного в клетке
    protected   int movementSpeed; // Скорость перемещения
    protected   double foodNeeded; // Количество пищи для насыщения
    protected   double satiety; // Текущая сытость

    public Animal(double weight, int maxNumberCell, int movementSpeed, double foodNeeded) {
        this.weight = weight;
        this.maxNumberCell = maxNumberCell;
        this.movementSpeed = movementSpeed;
        this.foodNeeded = foodNeeded;
        this.satiety = foodNeeded;
    }

    @Override
    public double getNutritionalValue() {
        return weight;
    }
    public int getMaxInCell(){
        return maxNumberCell;
    }
    // флаг смерти животного
    public boolean isDead(){
        return this.satiety<=0;
    }
    // метод для уменьшения сытости
    public void decreaseSatiety(){
        this.satiety -=1.0;
        if (this.satiety<0){
            this.satiety = 0;
        }
    }

    public double getEatingProbability (Eatable target){
        Map<Class<?extends Eatable >, Double> probabilities = Settings.PROBABILITY_EAT_ANIMAL.get(this.getClass());
        if(probabilities != null){
            return probabilities.getOrDefault(target.getClass(),0.0);
        }
        return 0.0;
    }

    // Передвижение
    public void move (Island island, int currentX, int currentY){
        Random random = ThreadLocalRandom.current();
        int newX = Math.max(0,Math.min(island.getWidth()-1,currentX+random.nextInt(2*this.movementSpeed+1)-this.movementSpeed));
        int newY = Math.max(0,Math.min(island.getHeight()-1,currentY+ random.nextInt(2*this.movementSpeed+1)-this.movementSpeed));
        if(newX!=currentX||newY!=currentY){
            synchronized (island.getLocation(currentX,currentY)) {
                island.getLocation(currentX, currentY).removeAnimal(this);
            }
            synchronized (island.getLocation(newX,newY)){
                island.getLocation(newX,newY).addAnimal(this);
           }
        }
    }
    // Питание (Переписать с учетом, что все могут есть траву, а также реализовать много поточный Random)
    public void eat (Location location) {

        if (this instanceof Herbivore) {
            synchronized (location) {
                if (location.getPlant().getCount()>0&& location.getPlant().consume()) {
                    this.satiety = Math.min(this.foodNeeded, this.satiety + location.getPlant().getNutritionalValue());
                }
                Animal eatHerbivore = location.getRandomHerbivore();
                if (eatHerbivore != null && ThreadLocalRandom.current().nextDouble() < getEatingProbability(eatHerbivore)) {
                    location.removeAnimal(eatHerbivore);
                    this.satiety = Math.min(this.foodNeeded, this.satiety + eatHerbivore.getNutritionalValue());
                    return;
                }
                this.decreaseSatiety();
            }
        } else if (this instanceof Predator) {
            synchronized (location) {
                Animal prey = location.getRandomHerbivore();
                if (prey != null && ThreadLocalRandom.current().nextDouble() < getEatingProbability(prey)) {
                    location.removeAnimal(prey);
                    this.satiety = Math.min(this.foodNeeded, this.satiety + prey.getNutritionalValue());
                } else {
                    this.decreaseSatiety();
                }
            }
        }


    }
    // Размножение
    public static void reproduce(Location location) {
        synchronized (location) {
            //Группируем животных по их типам
            Map<Class<?extends Animal>, List<Animal>> groupedAnimals = location.getAnimals().stream()
                    .collect(Collectors.groupingBy(Animal::getClass));
            //Перебираем животных
            groupedAnimals.forEach(((aClass, animals) -> {
                int pairCount = animals.size() / 2; //кол-во пар
                int offspringCount = Settings.OFFSPRING_COUNT.getOrDefault(aClass,1);
                for (int i = 0; i < pairCount; i++) {
                    for (int j = 0; j < offspringCount; j++) {
                        if (location.canAddAnimal(animals.getFirst())){//Проверка можно ли добавить животное
                            Animal newAnimal = AnimalFactory.create(aClass);
                            location.addAnimal(newAnimal);
                            System.out.println(aClass.getSimpleName() + " reproduced.");
                        }else {
                            break;
                        }

                    }
                }
            } ));



        }
    }

}