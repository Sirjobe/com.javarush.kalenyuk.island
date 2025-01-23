package island.entity.creature.animal.herbivore;

import island.entity.creature.Animal;

public class Mouse extends Animal implements Herbivore {
    public Mouse() {
        super(0.05, 500, 1, 0.01);
    }
}
