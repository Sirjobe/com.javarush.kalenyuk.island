package island_v2_beta.entity.creature.plant;

import island_v2_beta.entity.creature.Eatable;

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

    public synchronized void grow(){
        growthCounter++;
        if (growthCounter >= growthRate && count < countMAX) {
            count++;
          //  System.out.println("Трава выросла: " + count); // Логирование
            growthCounter = 0;
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
    public synchronized int getCount(){
        return count;
    }
    public int getCountMAX(){
        return countMAX;
    }
    public synchronized void setCount(int count){
        this.count = Math.min(count,countMAX);
    }
    public synchronized boolean canGrow(){
        return growthCounter > growthRate && count < countMAX;
    }
}
