package repository;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import config.HConfig;
import model.Car;

import java.net.UnknownHostException;
import java.util.*;

public class CarRepository {

    IMap<Long, Car> cars;
    HazelcastInstance client;
    private final Random random = new Random(System.currentTimeMillis());

    public CarRepository() {
        ClientConfig clientConfig = null;
        try {
            clientConfig = HConfig.getClientConfig();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client = HazelcastClient.newHazelcastClient(clientConfig);
        cars = client.getMap("cars");
    }

    public Car addCar() {
        return cars.put(random.nextLong(), enterCarInfo());
    }

    public void deleteById(Long id) {
        if (cars.containsKey(id)) {
            Car car = cars.remove(id);
            System.out.println("Successfully removed car");
        } else {
            System.out.println("Id not found");
        }
    }

    public Car getById(Long id) {
        Car car = null;
        if (cars.containsKey(id)) {
            car = cars.get(id);
            System.out.println(car);
        } else {
            System.out.println("Id not found");
        }
        return car;
    }

    public Car updateById(Long id) {
        Car car = null;
        if (cars.containsKey(id)) {
            //car = cars.get(id);
            car = enterCarInfo();
            System.out.println(car);
            cars.put(id, car);
        } else {
            System.out.println("Id not found");
        }
        return car;
    }

    public Collection<Car> getByModel(String model) {
        Predicate<?,?> modelPredicate = Predicates.equal( "model", model );
        return cars.values(Predicates.and(modelPredicate));
    }

    private Car enterCarInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID Plate: ");
        String idPlate = scanner.nextLine();
        System.out.print("Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("ManufactureYear: ");
        int manufactureYear = scanner.nextInt();
        return new Car(idPlate, brand, model, manufactureYear);
    }
}
