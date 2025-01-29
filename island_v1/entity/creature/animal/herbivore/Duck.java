package island_v1.entity.creature.animal.herbivore;

import island_v1.entity.creature.Animal;

public class Duck extends Animal implements Herbivore {
    public Duck() {
        super(1, 200, 4, 0.15);
    }
}
