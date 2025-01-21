package island;

import island.entity.Island;

public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH,Settings.ISLAND_HEIGHT);
        //логика заселения животных и растений
       island.growPlants();
        island.display();
//    while (true){
//        island.display();
//    }

    }





}
