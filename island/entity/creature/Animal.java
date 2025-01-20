package island.entity.creature;

import island.Settings;
import island.entity.Island;
import island.entity.creature.animal.herbivore.Herbivore;
import island.entity.creature.animal.predactor.Predator;

import java.util.Map;
import java.util.Random;

public abstract class Animal implements Eatable {
    protected   int weight; // Вес животного
    protected   int maxNumberCell; // Максимальное количество животного в клетке
    protected   int movementSpeed; // Скорость перемещения
    protected   double foodNeeded; // Количество пищи для насыщения

    public Animal(int weight, int maxNumberCell, int movementSpeed, double foodNeeded) {
        this.weight = weight;
        this.maxNumberCell = maxNumberCell;
        this.movementSpeed = movementSpeed;
        this.foodNeeded = foodNeeded;
    }

    @Override
    public double getNutritionalValue() {
        return weight;
    }
    public double getEatingProbability (Eatable target){
        Map<Class<?extends Eatable >, Double> probabilities = Settings.PROBABILITY_EAT_ANIMAL.get(target);
        if(probabilities != null){
            return probabilities.getOrDefault(target.getClass(),0.0);
        }
        return 0.0;
    }

    // Передвижение
    public void move (Island island, int columnsCount, int rowsCount){
        Random random = new Random();
        int newX = Math.max(0,Math.min(island.getWidth()-1,columnsCount+random.nextInt(2*movementSpeed+1)-movementSpeed));
        int newY = Math.max(0,Math.min(island.getHeight()-1,rowsCount+ random.nextInt(2*movementSpeed+1)-movementSpeed));
        if(newX!=columnsCount||newY!=rowsCount){
            System.out.println(this.getClass().getSimpleName() + " переместился из [" + columnsCount + ", " + rowsCount + "] в [" + newX + ", " + newY + "]"); // удалить после проверки
            island.getLocation(columnsCount,rowsCount).removeAnimal(this);
            island.getLocation(newX,newY).addAnimal(this);
        }

    }
    // Питание
    public void eat (Location location){
        if (this instanceof Herbivore){
            if (location.getPlant().consume()) { // метод уменьшения количества растений
                System.out.println(this.getClass().getSimpleName() + " съел растения."); // удалить после проверки
            }else {
                System.out.println(this.getClass().getSimpleName() + " не нашел растений."); // удалить после проверки
            }
        } else if (this instanceof Predator) {
            // Поиск жертвы
            Animal prey = location.getRandomHerbivore(); //Поиск случайного травоядного в клетке
            if(prey!= null && Math.random()<getEatingProbability(prey)){
                location.removeAnimal(prey);
            }else {
                System.out.println(this.getClass().getSimpleName() + " не нашел добычи."); // удалить после проверки
            }

        }
    }
    public abstract void reproduce (Location location); // Размножение



}