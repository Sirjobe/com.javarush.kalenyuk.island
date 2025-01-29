package island_v2_beta.entity.creature.animal.herbivore;

import island_v2_beta.entity.creature.Animal;

public class Caterpillar extends Animal implements Herbivore {

    public Caterpillar() {
        super(0.01, 1000, 0, 0);
    }

    @Override
    public boolean isDead() {
        return false;
    }
}
