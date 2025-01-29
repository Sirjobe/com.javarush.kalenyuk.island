package island_v2.entity.creature;

import island_v2.entity.creature.animal.herbivore.*;
import island_v2.entity.creature.animal.predactor.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimalFactory {
    //Мапа связывающая тип животного с его конструктором
    private static final Map<Class<?extends Animal>, Supplier<?extends Animal>> animalSuppliers = new HashMap<>();
    static {
        animalSuppliers.put(Horse.class, Horse::new);
        animalSuppliers.put(Deer.class, Deer::new);
        animalSuppliers.put(Rabbit.class, Rabbit::new);
        animalSuppliers.put(Mouse.class, Mouse::new);
        animalSuppliers.put(Goat.class, Goat::new);
        animalSuppliers.put(Sheep.class, Sheep::new);
        animalSuppliers.put(Buffalo.class, Buffalo::new);
        animalSuppliers.put(Duck.class, Duck::new);
        animalSuppliers.put(Caterpillar.class, Caterpillar::new);
        animalSuppliers.put(Wolf.class, Wolf::new);
        animalSuppliers.put(Boa.class, Boa::new);
        animalSuppliers.put(Fox.class, Fox::new);
        animalSuppliers.put(Bear.class, Bear::new);
        animalSuppliers.put(Eagle.class, Eagle::new);
    }
    public static Animal create(Class<?extends Animal> animalClass){
        try {
            return animalClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания животного: " + animalClass, e);
        }
    }
}
