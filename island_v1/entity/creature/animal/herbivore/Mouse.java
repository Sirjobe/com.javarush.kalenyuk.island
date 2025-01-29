package island_v1.entity.creature.animal.herbivore;

import island_v1.entity.creature.Animal;

public class Mouse extends Animal implements Herbivore {
    public Mouse() {
        super(0.05, 500, 1, 0.01);
    }
}
