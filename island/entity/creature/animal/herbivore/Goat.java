package island.entity.creature.animal.herbivore;

import island.entity.Island;
import island.entity.creature.Location;
import island.entity.creature.Animal;

public class Goat extends Animal implements Herbivore {
    public Goat() {
        super(70, 140, 3, 15);
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
