package island_v2_beta.entity.creature.animal.herbivore;

import island_v2_beta.entity.creature.Animal;

public class Mouse extends Animal implements Herbivore {
    public Mouse() {
        super(0.05, 500, 1, 0.01);
    }
}
