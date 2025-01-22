package island.entity.creature.plant;

import island.entity.creature.Eatable;

public class Plant implements Eatable {
    private int count;
    private final int countMAX = 200;
    private final int weightPlant = 1;


    public synchronized void grow(){
        if(count!=countMAX){
            count++;
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
}
