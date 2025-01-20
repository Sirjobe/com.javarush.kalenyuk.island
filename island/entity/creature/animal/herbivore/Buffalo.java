package island.entity.creature.animal.herbivore;

import island.entity.Island;
import island.entity.creature.Location;
import island.entity.creature.Animal;

import java.util.HashMap;
import java.util.Map;

public class Buffalo extends Animal implements Herbivore {
    public Buffalo(){
        super(700,10,3,100,probabilityEat);
    }
    private final Map<String,Integer>  probabilityEat = new HashMap<>();
    static {

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
