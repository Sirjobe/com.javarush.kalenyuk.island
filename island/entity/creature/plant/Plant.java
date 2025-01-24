package island.entity.creature.plant;

import island.entity.creature.Eatable;

public class Plant implements Eatable {
    private int count;
    private final int countMAX = 20;// 200
    private final int weightPlant = 1;
    private int growthRate = 3; // Скорость роста (каждые 10 тактов растет)
    private int growthCounter = 0; //Счетчик тактов роста


    public synchronized void grow(){
        growthCounter++;
        if(count < countMAX && growthCounter >= growthRate){
            count++;
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
