package island;

import island.entity.creature.Animal;
import island.entity.creature.Eatable;
import island.entity.creature.animal.herbivore.Buffalo;
import island.entity.creature.animal.herbivore.Goat;
import island.entity.creature.animal.predactor.Wolf;
import island.entity.creature.plant.Plant;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static final int ISLAND_WIDTH = 10;
    public static final int ISLAND_HEIGHT = 10;
    public static final int INITIAL_BUFFALO_COUNT = 10;
    public static final int INITIAL_GOAT_COUNT = 5;
    public static final int INITIAL_WOLF_COUNT = 10;
    //1_Ключ-MAP - класс хищник, 2_Ключ-MAP - класс жертв, Значение - вероятность поедания;
    public static final Map<Class<?extends Animal>,Map<Class<?extends Eatable>,Double>> PROBABILITY_EAT_ANIMAL = new HashMap<>();
    static {
        //Вероятность для Волка
        Map<Class<?extends Eatable>,Double> wolfProbability = new HashMap<>();
        wolfProbability.put(Buffalo.class,0.1);
        wolfProbability.put(Goat.class,0.7);
        PROBABILITY_EAT_ANIMAL.put(Wolf.class,wolfProbability);
        //Вероятность для Буйвола
        Map<Class<?extends Eatable>,Double> buffaloProbability = new HashMap<>();
        buffaloProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Buffalo.class,buffaloProbability);
        //Вероятность для Овцы
        Map<Class<?extends Eatable>,Double> goatProbability = new HashMap<>();
        goatProbability.put(Plant.class,1.0);
        PROBABILITY_EAT_ANIMAL.put(Goat.class,goatProbability);
    }



}
