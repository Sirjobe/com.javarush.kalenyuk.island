package island_v2;

import island_v2.entity.Island;
import island_v2.processors.AnimalProcessor;
import island_v2.processors.PlantProcessor;
import island_v2.service.StatisticsService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Application {
    public static void main(String[] args) {
        Island island = new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
        //Заселяем остров
        Island.populateIsland(island);
        //Запускаем симуляцию

        AnimalProcessor animalProcessor = new AnimalProcessor(island);
        PlantProcessor plantProcessor = new PlantProcessor(island);
        StatisticsService statisticsService = new StatisticsService(island);

        // Создаем планировщик задач
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

        // Запускаем рост растений каждые 5 секунд
        scheduler.scheduleAtFixedRate(plantProcessor::growPlants, 0, 5, TimeUnit.SECONDS);
        // Запускаем жизненный цикл животных каждые 2 секунды
        scheduler.scheduleAtFixedRate(animalProcessor::process, 0, 2, TimeUnit.SECONDS);
        // Выводим статистику каждые 10 секунд
        scheduler.scheduleAtFixedRate(statisticsService::display, 0, 10, TimeUnit.SECONDS);
        Thread.getAllStackTraces().keySet().forEach(thread ->
           System.out.println(thread.getName() + " - " + thread.getState()));
        // Запускаем симуляцию на 1 минуту
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Симуляция завершена!");
        scheduler.shutdown();
        animalProcessor.shutdown();
    }
}











