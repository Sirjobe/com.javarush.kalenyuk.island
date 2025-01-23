package island;

import island.entity.creature.Animal;
import island.entity.creature.Eatable;
import island.entity.creature.animal.herbivore.*;
import island.entity.creature.animal.predactor.*;
import island.entity.creature.plant.Plant;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static final int ISLAND_WIDTH = 100;
    public static final int ISLAND_HEIGHT = 20;
    //Количество животных создаваемых на острове
    public static final Map<Class<?extends Animal>,Integer> INITIAL_COUNT = new HashMap<>();
    static {
        INITIAL_COUNT.put(Horse.class, 10);
        INITIAL_COUNT.put(Deer.class,15);
        INITIAL_COUNT.put(Rabbit.class,12);
        INITIAL_COUNT.put(Mouse.class,13);
        INITIAL_COUNT.put(Goat.class,14);
        INITIAL_COUNT.put(Sheep.class,15);
        INITIAL_COUNT.put(Buffalo.class,16);
        INITIAL_COUNT.put(Duck.class,17);
        INITIAL_COUNT.put(Caterpillar.class,18);
        INITIAL_COUNT.put(Wolf.class,19);
        INITIAL_COUNT.put(Boa.class,21);
        INITIAL_COUNT.put(Fox.class,14);
        INITIAL_COUNT.put(Bear.class,11);
        INITIAL_COUNT.put(Eagle.class,9);
    }

    //1_Ключ-MAP - класс хищник, 2_Ключ-MAP - класс жертв, Значение - вероятность поедания;
    public static final Map<Class<?extends Animal>,Map<Class<?extends Eatable>,Double>> PROBABILITY_EAT_ANIMAL = new HashMap<>();
    static {
        //Вероятность для Лошади
        Map<Class<?extends Eatable>,Double> horseProbability = new HashMap<>();
        horseProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Horse.class,horseProbability);
        //Вероятность для Оленя
        Map<Class<?extends Eatable>,Double> deerProbability = new HashMap<>();
        deerProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Deer.class,deerProbability);
        //Вероятность для Кролика
        Map<Class<?extends Eatable>,Double> rabbitProbability = new HashMap<>();
        rabbitProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Rabbit.class,rabbitProbability);
        //Вероятность для Мыши
        Map<Class<?extends Eatable>,Double> mouseProbability = new HashMap<>();
        mouseProbability.put(Plant.class,1.0);
        mouseProbability.put(Caterpillar.class,0.9);
        PROBABILITY_EAT_ANIMAL.put(Mouse.class,mouseProbability);
        //Вероятность для Козы
        Map<Class<?extends Eatable>,Double> goatProbability = new HashMap<>();
        goatProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Goat.class,goatProbability);
        //Вероятность для Овцы
        Map<Class<?extends Eatable>,Double> sheepProbability = new HashMap<>();
        sheepProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Sheep.class,sheepProbability);
        //Вероятность для Кабана
        Map<Class<?extends Eatable>,Double> hogProbability = new HashMap<>();
        hogProbability.put(Plant.class,1.0);
        hogProbability.put(Caterpillar.class,0.9);
        hogProbability.put(Mouse.class,0.5);
        PROBABILITY_EAT_ANIMAL.put(Hog.class,hogProbability);
        //Вероятность для Буйвола
        Map<Class<?extends Eatable>,Double> buffaloProbability = new HashMap<>();
        buffaloProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Buffalo.class,buffaloProbability);
        //Вероятность для Утки
        Map<Class<?extends Eatable>,Double> duckProbability = new HashMap<>();
        duckProbability.put(Plant.class,1.0);
        duckProbability.put(Caterpillar.class,0.9);
        PROBABILITY_EAT_ANIMAL.put(Duck.class,duckProbability);
        //Вероятность для Гусеницы
        Map<Class<?extends Eatable>,Double> caterpillarProbability = new HashMap<>();
        caterpillarProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Caterpillar.class,caterpillarProbability);
        //Вероятность для Волка
        Map<Class<?extends Eatable>,Double> wolfProbability = new HashMap<>();
        wolfProbability.put(Horse.class,0.1);
        wolfProbability.put(Deer.class,0.15);
        wolfProbability.put(Rabbit.class,0.6);
        wolfProbability.put(Mouse.class,0.8);
        wolfProbability.put(Goat.class,0.6);
        wolfProbability.put(Sheep.class,0.7);
        wolfProbability.put(Hog.class,0.7);
        wolfProbability.put(Buffalo.class,0.1);
        wolfProbability.put(Duck.class,0.4);
        PROBABILITY_EAT_ANIMAL.put(Wolf.class,wolfProbability);
        //Вероятность для Удава
        Map<Class<?extends Eatable>,Double> boaProbability = new HashMap<>();
        boaProbability.put(Fox.class,0.15);
        boaProbability.put(Rabbit.class,0.20);
        boaProbability.put(Mouse.class,0.40);
        boaProbability.put(Duck.class,0.10);
        PROBABILITY_EAT_ANIMAL.put(Boa.class,boaProbability);
        //Вероятность для Лиса
        Map<Class<?extends Eatable>,Double> foxProbability = new HashMap<>();
        foxProbability.put(Rabbit.class,0.7);
        foxProbability.put(Mouse.class,0.9);
        foxProbability.put(Duck.class,0.6);
        foxProbability.put(Caterpillar.class,0.4);
        PROBABILITY_EAT_ANIMAL.put(Fox.class,foxProbability);
        //Вероятность для Медведя
        Map<Class<?extends Eatable>,Double> bearProbability = new HashMap<>();
        bearProbability.put(Boa.class,0.8);
        bearProbability.put(Horse.class,0.4);
        bearProbability.put(Deer.class,0.8);
        bearProbability.put(Rabbit.class,0.8);
        bearProbability.put(Mouse.class,0.9);
        bearProbability.put(Goat.class,0.7);
        bearProbability.put(Sheep.class,0.7);
        bearProbability.put(Hog.class,0.5);
        bearProbability.put(Buffalo.class,0.2);
        bearProbability.put(Duck.class,0.1);
        PROBABILITY_EAT_ANIMAL.put(Bear.class,bearProbability);
        //Вероятность для Орла
        Map<Class<?extends Eatable>,Double> eagleProbability = new HashMap<>();
        eagleProbability.put(Fox.class,0.1);
        eagleProbability.put(Rabbit.class,0.9);
        eagleProbability.put(Mouse.class,0.9);
        eagleProbability.put(Duck.class,0.8);
        PROBABILITY_EAT_ANIMAL.put(Eagle.class,eagleProbability);

    }
    //Количество рождаемого потомcтва
    public static final Map<Class<?extends Animal>,Integer> OFFSPRING_COUNT = new HashMap<>();
    static {
        OFFSPRING_COUNT.put(Horse.class, 1);
        OFFSPRING_COUNT.put(Deer.class,1);
        OFFSPRING_COUNT.put(Rabbit.class,1);
        OFFSPRING_COUNT.put(Mouse.class,1);
        OFFSPRING_COUNT.put(Goat.class,1);
        OFFSPRING_COUNT.put(Sheep.class,1);
        OFFSPRING_COUNT.put(Buffalo.class,1);
        OFFSPRING_COUNT.put(Duck.class,1);
        OFFSPRING_COUNT.put(Caterpillar.class,1);
        OFFSPRING_COUNT.put(Wolf.class,1);
        OFFSPRING_COUNT.put(Boa.class,1);
        OFFSPRING_COUNT.put(Fox.class,1);
        OFFSPRING_COUNT.put(Bear.class,1);
        OFFSPRING_COUNT.put(Eagle.class,1);
    }
    // Изображение сущности
    public static final Map<Class<?>, String> ENTITY_ICONS = new HashMap<>();
    static {
        ENTITY_ICONS.put(Horse.class, "\uD83D\uDC0E");
        ENTITY_ICONS.put(Deer.class,"\uD83E\uDD8C");
        ENTITY_ICONS.put(Rabbit.class,"\uD83D\uDC07");
        ENTITY_ICONS.put(Mouse.class,"\uD83D\uDC01");
        ENTITY_ICONS.put(Goat.class,"\uD83D\uDC11");
        ENTITY_ICONS.put(Sheep.class,"\uD83D\uDC11");
        ENTITY_ICONS.put(Buffalo.class,"\uD83D\uDC03");
        ENTITY_ICONS.put(Duck.class,"\uD83E\uDD86");
        ENTITY_ICONS.put(Caterpillar.class,"\uD83D\uDC1B");
        ENTITY_ICONS.put(Wolf.class,"\uD83D\uDC3A");
        ENTITY_ICONS.put(Boa.class,"\uD83D\uDC0D");
        ENTITY_ICONS.put(Fox.class,"\uD83E\uDD8A");
        ENTITY_ICONS.put(Bear.class,"\uD83D\uDC3B");
        ENTITY_ICONS.put(Eagle.class,"\uD83E\uDD85");
        ENTITY_ICONS.put(Plant.class,"\uD83C\uDF31");
    }



}
