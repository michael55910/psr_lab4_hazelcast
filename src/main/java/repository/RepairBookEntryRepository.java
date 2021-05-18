package repository;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import config.HConfig;
import model.Car;
import model.RepairBookEntry;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RepairBookEntryRepository {

    IMap<Long, RepairBookEntry> repairBookEntries;
    IMap<Long, Car> cars;
    HazelcastInstance client;
    private final Random random = new Random(System.currentTimeMillis());

    public RepairBookEntryRepository() {
        ClientConfig clientConfig = null;
        try {
            clientConfig = HConfig.getClientConfig();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client = HazelcastClient.newHazelcastClient(clientConfig);
        repairBookEntries = client.getMap("repairBookEntries");
        cars = client.getMap("cars");
    }

    public RepairBookEntry addEntry() {
        return repairBookEntries.put(random.nextLong(), enterEntryInfo());
    }

    public void deleteById(Long id) {
        if (repairBookEntries.containsKey(id)) {
            RepairBookEntry repairBookEntry = repairBookEntries.remove(id);
            System.out.println("Successfully removed entry");
        } else {
            System.out.println("Id not found");
        }
    }

    public RepairBookEntry getById(Long id) {
        RepairBookEntry repairBookEntry = null;
        if (cars.containsKey(id)) {
            repairBookEntry = repairBookEntries.get(id);
            System.out.println(repairBookEntry);
        } else {
            System.out.println("Id not found");
        }
        return repairBookEntry;
    }

    public RepairBookEntry updateById(Long id) {
        RepairBookEntry repairBookEntry = null;
        if (repairBookEntries.containsKey(id)) {
            repairBookEntry = enterEntryInfo();
            System.out.println(repairBookEntry);
            repairBookEntries.put(id, repairBookEntry);
        } else {
            System.out.println("Id not found");
        }
        return repairBookEntry;
    }

    public Collection<RepairBookEntry> getByDate(Date date) {
        Predicate<?,?> datePredicate = Predicates.equal( "date", date );
        return repairBookEntries.values(Predicates.and(datePredicate));
    }

    public void MarkAllDone() {
        Set<Map.Entry<Long, RepairBookEntry>> entries = repairBookEntries.entrySet();
        for (Map.Entry<Long,RepairBookEntry> entry: entries) {
            entry.getValue().setDescription(entry.getValue().getDescription() + "DONE");
            repairBookEntries.put(entry.getKey(), entry.getValue());
        }
    }

    private RepairBookEntry enterEntryInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Car ID: ");
        Long carId = scanner.nextLong();
        Car car = null;
        if (cars.containsKey(carId)) {
            car = cars.get(carId);
            System.out.println(car);
        } else {
            System.out.println("Id not found");
        }
        System.out.print("Date: ");
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("Description: ");
        String description = scanner.nextLine();
        return new RepairBookEntry(car, date, description);
    }
}
