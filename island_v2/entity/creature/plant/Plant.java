package island_v2.entity.creature.plant;

import island_v2.entity.creature.Eatable;
import island_v2.entity.creature.Location;

import java.util.concurrent.ThreadLocalRandom;

public class Plant implements Eatable {
    private int count;
    private final int countMAX = 20;// 200
    private final int growthRate = 3; // Скорость роста (каждые 10 тактов растет)
    private int growthCounter; //Счетчик тактов роста

    public Plant(){
    // Начальный growthCounter случайный
    this.growthCounter = ThreadLocalRandom.current().nextInt(growthRate);
    }

    public void grow(Location location) {
        synchronized (location) {  // Блокируем только конкретную локацию, а не весь класс
            growthCounter++;

            if (growthCounter >= growthRate) {
                if (count < countMAX) {
                    count++;
                  //  System.out.println("Трава выросла на локации: " + location + " (Всего: " + count + ")");
                }
                growthCounter = 0; // Сбрасываем счетчик только после проверки
            }
        }
    }
    public synchronized boolean consume(){
        if(count>0){
            count--;
            return true;
        }
        return false;
    }

    @Override
    public double getNutritionalValue() {
        int weightPlant = 1;
        return weightPlant;
    }
    public synchronized int getCount(){ // synchronized
        return count;
    }
    public synchronized int getCountMAX(){ // synchronized
        return countMAX;
    }
    public synchronized void setCount(int count){ // synchronized
        this.count = Math.min(count,countMAX);
    }

}
