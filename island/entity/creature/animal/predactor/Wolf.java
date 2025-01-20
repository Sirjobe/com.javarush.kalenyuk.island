package island.entity.creature.animal.predactor;

import island.entity.Island;
import island.entity.creature.Animal;
import island.entity.creature.Location;

public class Wolf extends Animal implements Predator {

    public Wolf(){
        super(50,30,3,8);
    }
    @Override
    public void move(Island island, int columnsCount, int rowsCount) {

    }

    @Override
    public void eat(Location location) {

    }

    @Override
    public void reproduce(Location location) {

    }
}
