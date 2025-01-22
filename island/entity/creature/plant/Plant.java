package island.entity.creature.plant;

import island.entity.creature.Eatable;

public class Plant implements Eatable {
    private int count;
    private final int countMAX = 200;
    private final int weightPlant = 1;


    public void grow(){
        if(count!=countMAX){
            count++;
        }
    }
    public boolean consume(){
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
    public int getCount(){
        return count;
    }
    public int getCountMAX(){
        return countMAX;
    }
    public void setCount(int count){
        this.count = Math.min(count,countMAX);
    }
}
