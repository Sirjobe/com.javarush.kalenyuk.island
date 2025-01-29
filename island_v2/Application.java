package island_v2;

import island_v2.entity.Island;


public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
        //Заселяем остров
        Island.populateIsland(island);
        //Запускаем симуляцию
        island.startSimulation();
//        try {
//            Thread.sleep(6000);
//            island.startSimulation();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if(island.checkGameOver()){
            island.stopSimulation();
        }

    }
}






