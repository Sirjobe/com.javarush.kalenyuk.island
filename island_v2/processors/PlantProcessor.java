package island_v2.processors;
import island_v2.entity.Island;
import island_v2.entity.creature.plant.Plant;

import java.awt.*;

public class PlantProcessor {
    private final Island island;

    public PlantProcessor(Island island) {
        this.island = island;
    }

    public void growPlants() {
        Plant plant = new Plant();
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                plant.grow(island.getLocation(x, y));
            }
        }
    }

}
