package island.entity.creature.animal.herbivore;

import island.entity.creature.Location;
import island.entity.creature.Animal;


public class Buffalo extends Animal implements Herbivore {
    public Buffalo(){
        super(700,10,3,100);
    }


    @Override
    public void eat(Location location) {
        // Логика питания
    }

    @Override
    public void reproduce(Location location) {
        //Логика размножения
    }
}
