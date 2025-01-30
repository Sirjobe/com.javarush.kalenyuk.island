package island_v2.entity.creature;

import island_v2.Settings;
import island_v2.entity.Island;
import island_v2.entity.creature.animal.herbivore.Herbivore;
import island_v2.entity.creature.animal.predactor.Predator;
import island_v2.entity.creature.plant.Plant;

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
        return this.satiety<=0.00;
    }
    // метод для уменьшения сытости
    public void decreaseSatiety(){
        this.satiety = Math.max(0, this.satiety - Settings.SATIETY_DECREASE_PER_ITERATION);
      //  System.out.println(this.getClass().getSimpleName() + " сытость: " + this.satiety);
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
           if(newLocation.tryLock()) {
               try {
                   currentLocation.removeAnimal(this);
                   newLocation.addAnimal(this);
               } finally {
                   newLocation.unlock();
               }
           }
        }
    }
    // Питание
    public void eat(Location location) {
        Random random = ThreadLocalRandom.current();
        boolean hasEaten = false; // Флаг, указывающий, что животное уже поело

        if (this instanceof Herbivore) {
            Plant plant = location.getPlant();
            double plantProbability = getEatingProbability(plant);

            // Попытка съесть растение
            if (!hasEaten && plant.getCount() > 0 && random.nextDouble() < plantProbability) {
                if (plant.consume()) {
                    double newSatiety = this.satiety + plant.getNutritionalValue();
                    this.satiety = Math.min(this.foodNeeded, newSatiety);
                  //  System.out.println(this.getClass().getSimpleName() + " съел растение. Сытость: " + this.satiety);
                    hasEaten = true;
                }
            }

            // Попытка съесть другое травоядное
            if (!hasEaten) {
                Animal eatHerbivore = location.getRandomAnimal();
                if (eatHerbivore != null) {
                    double herbivoreProbability = getEatingProbability(eatHerbivore);
                    if (random.nextDouble() < herbivoreProbability) {
                        location.removeAnimal(eatHerbivore);
                        double newSatiety = this.satiety + eatHerbivore.getNutritionalValue();
                        this.satiety = Math.min(this.foodNeeded, newSatiety);
                     //   System.out.println(this.getClass().getSimpleName() + " съел " + eatHerbivore.getClass().getSimpleName() + ". Сытость: " + this.satiety);
                        hasEaten = true;
                    }
                }
            }
        } else if (this instanceof Predator) {
            // Попытка съесть другое животное (для хищников)

                Animal prey = location.getRandomAnimal();
                if (prey != null && !hasEaten) {
                    double preyProbability = getEatingProbability(prey);
                    if (random.nextDouble() < preyProbability) {
                        location.removeAnimal(prey);
                        double newSatiety = this.satiety + prey.getNutritionalValue();
                        this.satiety = Math.min(this.foodNeeded, newSatiety);
                      //  System.out.println(this.getClass().getSimpleName() + " съел " + prey.getClass().getSimpleName() + ". Сытость: " + this.satiety);
                        hasEaten = true;
                    }
                }

        }

        // Если животное не поело, уменьшаем сытость
        if (!hasEaten) {
            this.decreaseSatiety();
        }
    }
    // Размножение
    public static void reproduce(Location location) {
        synchronized (location) {
            // Проверяем, достаточно ли травы для размножения травоядных
            boolean hasHerbivores = location.getAnimals().stream()
                    .anyMatch(animal -> animal instanceof Herbivore);
            if (location.getPlant().getCount() < location.getPlant().getCountMAX() / 2 && hasHerbivores) {
                return; // Недостаточно травы для размножения
            }

            // Группируем животных по их типам
            Map<Class<? extends Animal>, List<Animal>> groupedAnimals = location.getAnimals().stream()
                    .collect(Collectors.groupingBy(Animal::getClass));

            // Перебираем животных
            groupedAnimals.forEach((aClass, animals) -> {
                int pairCount = animals.size() / 2; // кол-во пар
                int offspringCount = Settings.OFFSPRING_COUNT.getOrDefault(aClass, 1);

                for (int i = 0; i < pairCount; i++) {
                    Animal animal1 = animals.get(i * 2);
                    Animal animal2 = animals.get(i * 2 + 1);

                    // Проверяем, что сытость обоих животных меньше 1/3 от foodNeeded
                    if (animal1.getSatiety() < animal1.getFoodNeeded() / 3 &&
                            animal2.getSatiety() < animal2.getFoodNeeded() / 3) {

                        for (int j = 0; j < offspringCount; j++) {
                            if (location.canAddAnimal(animal1)) { // Проверка можно ли добавить животное
                                Animal newAnimal = AnimalFactory.create(aClass);
                                location.addAnimal(newAnimal);
                              //  System.out.println(aClass.getSimpleName() + " размножились. Новое животное добавлено.");
                            } else {
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

}