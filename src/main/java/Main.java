import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import config.HConfig;
import model.Car;
import model.RepairBook;
import model.RepairBookEntry;

import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        ClientConfig clientConfig = null;
        try {
            clientConfig = HConfig.getClientConfig();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        HazelcastInstance client = HazelcastClient.newHazelcastClient( clientConfig );
        IMap<Long, Car> cars = client.getMap("players");
        IMap<Long, RepairBook> repairBooks = client.getMap("repairBooks");
        IMap<Long, RepairBookEntry> repairBookEntries = client.getMap("repairBookEntry");

        //MENU
        Menu menu = new Menu();
        while (true) {
            int operation = menu.selectOperation();
            if (operation == 0) {
                break;
            }
            int target = menu.selectTarget();
            switch (operation) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Błędny wybór!");
                    return;
            }
        }
        
    }



}
