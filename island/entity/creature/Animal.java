package island.entity.creature;

import island.Settings;
import island.entity.Island;
import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.animal.predactor.Predator;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal implements Eatable {
    protected   int weight; // Вес животного
    protected   int maxNumberCell; // Максимальное количество животного в клетке
    protected   int movementSpeed; // Скорость перемещения
    protected   double foodNeeded; // Количество пищи для насыщения
    protected   double satiety; // Текущая сытость

    public Animal(int weight, int maxNumberCell, int movementSpeed, double foodNeeded) {
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
        synchronized (location) {
            if (this instanceof Herbivore) {
                if (location.getPlant().consume()) { // метод уменьшения количества растений
                    this.satiety = Math.min(this.foodNeeded, this.satiety + 1);
                    System.out.println(this.getClass().getSimpleName() + " ate a plant.");
                } else {
                    this.decreaseSatiety();
                }
            } else if (this instanceof Predator) {
                // Поиск жертвы
                Animal prey = location.getRandomHerbivore(); //Поиск случайного травоядного в клетке
                if (prey != null && ThreadLocalRandom.current().nextDouble() < getEatingProbability(prey)) {
                    location.removeAnimal(prey);
                    this.satiety = Math.min(this.foodNeeded, this.satiety + prey.getNutritionalValue());
                    System.out.println(this.getClass().getSimpleName() + " ate a plant."+prey);
                } else {
                    this.decreaseSatiety();
                }

            }
        }
    }
    // Размножение
    public  void reproduce (Location location) {
        synchronized (location) {
            long sameSpeciesCount = location.getAnimals().stream() // количество животных в одной клетке
                    .filter(a -> a.getClass().equals(this.getClass()))
                    .count();
            if (sameSpeciesCount >= 2 && location.canAddAnimal(this) && this.satiety > foodNeeded / 2) { // проверка условий для размножения
                int offspringCount = Settings.OFFSPRING_COUNT.getOrDefault(this.getClass(), 1);// Значение по умолчанию
                // Добавляем потомков в клетку
                for (int i = 0; i < offspringCount; i++) {
                    if (location.canAddAnimal(this)) {// Проверка на добавление нового потомка
                        Animal newAnimal = AnimalFactory.create(this.getClass());
                        location.addAnimal(newAnimal);
                    } else {
                        break;
                    }
                }


            }
        }
    }

}