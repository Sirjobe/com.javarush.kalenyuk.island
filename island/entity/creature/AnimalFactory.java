package island.entity.creature;

import island.entity.creature.animal.herbivore.Buffalo;
import island.entity.creature.animal.herbivore.Goat;
import island.entity.creature.animal.predactor.Wolf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimalFactory {
    //Мапа связывающая тип животного с его конструктором
    private static final Map<Class<?extends Animal>, Supplier<?extends Animal>> animalSuppliers = new HashMap<>();
    static {
        animalSuppliers.put(Buffalo.class, Buffalo::new);
        animalSuppliers.put(Goat.class, Goat::new);
        animalSuppliers.put(Wolf.class, Wolf::new);
    }
    public static Animal create(Class<?extends Animal> animalClass){
        try {
            return animalClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания животного: " + animalClass, e);
        }
    }
}
