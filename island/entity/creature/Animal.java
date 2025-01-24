package island.entity.creature;

import island.Settings;
import island.entity.Island;
import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.animal.predactor.Predator;
import island.entity.creature.animal.predactor.Wolf;

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
    public double getSatiety(){
        return satiety;
    }
    public double getFoodNeeded() {
        return foodNeeded;
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
        this.satiety -= Settings.SATIETY_DECREASE_PER_ITERATION;
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
            Location currentLocation = island.getLocation(currentX,currentY);
            Location newLocation = island.getLocation(newX,newY);
            // Блокировка в порядке возрастания координат для избежания deadlock
           if(currentX< newX||(currentX == newX && currentY<newY)){
               currentLocation.lock();
               newLocation.lock();
           }else {
               newLocation.lock();
               currentLocation.lock();
           }
           try {
               currentLocation.removeAnimal(this);
               newLocation.addAnimal(this);
           }finally {
               currentLocation.unlock();
               newLocation.unlock();
           }
        }
    }
    // Питание (Переписать с учетом, что все могут есть траву, а также реализовать много поточный Random)
    public void eat (Location location) {

        if (this instanceof Herbivore) {
            synchronized (location) {
                if (location.getPlant().getCount() > 0 && ThreadLocalRandom.current().nextDouble() < getEatingProbability(location.getPlant())) {
                    if (location.getPlant().consume()) {
                        this.satiety = Math.min(this.foodNeeded, this.satiety + location.getPlant().getNutritionalValue());
                        return;
                    }
                }
                //Попытка съесть другое травоядное
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
            // Проверяем, достаточно ли травы для размножения
            if (location.getPlant().getCount() < location.getPlant().getCountMAX() / 2 && location.getAnimals() instanceof Herbivore) {
                return; // Недостаточно травы для размножения
            }


            //Группируем животных по их типам
            Map<Class<?extends Animal>, List<Animal>> groupedAnimals = location.getAnimals().stream()
                    .collect(Collectors.groupingBy(Animal::getClass));
            //Перебираем животных
            groupedAnimals.forEach(((aClass, animals) -> {
                int pairCount = animals.size() / 2; //кол-во пар
                int offspringCount = Settings.OFFSPRING_COUNT.getOrDefault(aClass,1);
                for (int i = 0; i < pairCount; i++) {
                    Animal animal1 = animals.get(i*2);
                    Animal animal2 = animals.get(i*2+1);
                    // Проверяем, что сытость обоих животных меньше 1/3 от foodNeeded
                    if (animal1.getSatiety() < animal1.getFoodNeeded() / 3 &&
                            animal2.getSatiety() < animal2.getFoodNeeded() / 3) {
                        for (int j = 0; j < offspringCount; j++) {
                            if (location.canAddAnimal(animals.getFirst())) {//Проверка можно ли добавить животное
                                Animal newAnimal = AnimalFactory.create(aClass);
                                location.addAnimal(newAnimal);
                            } else {
                                break;
                            }
                        }
                    }


                }

            }));



        }
    }

}