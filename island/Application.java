package island;

import island.entity.Island;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
        //Заселяем остров
        Island.populateIsland(island);
        //Создаем пул потоков
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        Runnable simulationTask = new Runnable() {
            int cycle = 0;

            @Override
            public void run() {
                if (island.checkGameOver()) {
                    System.out.println("Симуляция окончена.");
                    if (island.checkGameOver()) {
                        System.out.println("Причина завершения: " + (island.hasAliveAnimals() ? "остались только травоядные" : "все животные умерли"));
                    }
                    executor.shutdown();
                    return;
                }
                // Обновление консоли и вывод текущего такта
                 System.out.println("Такт: " + (cycle + 1)+" до изменений");
                 island.display();
                // Выполнение логики симуляции для острова
                island.interact();
                // Обновление отображения острова
                System.out.println("Такт: " + (cycle + 1)+" после изменений");
                island.display();
                cycle++;
            }
        };
        executor.scheduleAtFixedRate(simulationTask, 0, 10, TimeUnit.MILLISECONDS);
    }
}






