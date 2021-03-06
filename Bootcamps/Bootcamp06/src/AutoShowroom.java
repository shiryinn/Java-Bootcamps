/**
 * AutoShowroom Class
 * @author Lim Shir Yin
 * @version 11/04/2021
 */

import edu.monash.fit2099.bids.Bid;
import edu.monash.fit2099.bids.BidsManager;
import edu.monash.fit2099.buyers.Buyer;
import edu.monash.fit2099.exceptions.BidException;
import edu.monash.fit2099.exceptions.SedanException;
import edu.monash.fit2099.exceptions.TruckException;
import edu.monash.fit2099.exceptions.VehicleException;
import edu.monash.fit2099.vehicles.Sedan;
import edu.monash.fit2099.vehicles.Truck;
import edu.monash.fit2099.vehicles.Vehicle;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AutoShowroom {

    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    private HashMap<Integer, Buyer> buyersRec = new HashMap<>();

    public ArrayList<Vehicle> getVehicles() { return this.vehicles; }

    private HashMap<Integer, Buyer> getBuyersRec() {
        return this.buyersRec;
    }

    private ArrayList<Integer> veh = new ArrayList<>();

    private ArrayList<Double> price = new ArrayList<>();

    private ArrayList<Vehicle> vehiclesSold = new ArrayList<>();


    /**
     * To print console
     * @param display
     * @return none
     * @exception Exception
     */
    public void printStatus(boolean display) {
        boolean condition = true;
        while (condition) {
            System.out.println("+---------------------------------+");
            System.out.println("|           Welcome to            |");
            System.out.println("|      FIT2099 AutoShowroom       |");
            System.out.println("+---------------------------------+");
            System.out.println("1.  New Sedan " +
                    "\n2.  New Truck " +
                    "\n3.  Add new buyer " +
                    "\n4.  Display current fleek " +
                    "\n5.  Display buyers " +
                    "\n6.  Add new bid " +
                    "\n7.  Highest bid " +
                    "\n8.  Lowest Bid " +
                    "\n9.  Delete Bid " +
                    "\n10. Sell Vehicles " +
                    "\n11. Exit");
            System.out.print("Select an option: ");

            Scanner optionsChosen = new Scanner(System.in);
            int option;
            try {
                option = optionsChosen.nextInt();
                if (option == 12) {
                    condition = false;
                    this.checkOptions(option);
                } else {
                    this.checkOptions(option);
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid option above!");
                this.printStatus(true);
            }
            if (!display) {
                condition = false;
            }
        }
    }

    /**
     * To check if option enter is valid
     * @param option
     * @return none
     * No exceptions is used.
     */
    private void checkOptions(int option) {
        if (option == 1) {
            this.createSedan();
        } else if (option == 2) {
            this.createTruck();
        } else if (option == 3) {
            this.createBuyer();
        } else if (option == 4) {
            this.displayFleet();
        } else if (option == 5) {
            this.displayBuyers();
        } else if (option == 6) {
            this.createBid();
        } else if (option == 7) {
            this.highestBidPrice();
        } else if (option == 8) {
            this.lowestBidPrice();
        } else if (option == 9) {
            this.deleteBid();
        } else if (option == 10) {
            this.sellVehicles();
        } else if (option == 11) {
            System.out.println("+------------------------------------------+");
            System.out.println("| Thank you for visiting FIT2099 Showroom. |");
            System.out.println("|         Goodbye! See you again!          |");
            System.out.println("+------------------------------------------+");
        } else {
            System.out.println("Please enter a valid option above!");
            this.printStatus(true);
        }
    }

    /**
     * To sell Vehicles by entering the Vehicle's ID existed
     */
    private void sellVehicles(){
        int vehId = inputVehId();
        int index = 0;
        for (Vehicle vehicle : vehicles) {
            if (vehId == vehicle.getVId()){
                BidsManager bids = vehicles.get(index).getManageBids();
                HashMap<Integer, Bid> bid = bids.getBidsManage();
                for (Integer currentBid: bid.keySet()) {
                    bid.remove(currentBid);
                    System.out.println("Bid of vehicle sold has been removed!");
                }
                vehicles.remove(index);
                System.out.println("Vehicle sold is removed!");
                vehiclesSold.add(vehicle);
                System.out.println("Vehicle sold is added to successfully!");
            } else {
                System.out.println("Invalid Vehicle ID.");
            }
            index ++;
        }
    }

    /**
     * To show highest Bid Price
     */
    public void highestBidPrice() {
        int vehId = inputVehId();
        double highestBid = 0;
        double bidAmount;
        for (int i = 0; i < veh.size(); i++) {
            if (veh.get(i) == vehId) {
                for (int j = 0; j < price.size(); j++) {
                    if (i == j) {
                        bidAmount = price.get(i);
                        if (bidAmount > highestBid) {
                            highestBid = bidAmount;
                            System.out.println("Highest Bid Price: " + highestBid);
                        }
                    }
                }
            } else {
                System.out.println("Vehicle does not exist.");
            }
        }
    }

    /**
     * To show lowest Bid Price
     */
    public void lowestBidPrice() {
        int vehId = inputVehId();
        double lowestBid = price.get(0);
        double bidAmount;
        for (int i = 0; i < veh.size(); i++) {
            if (veh.get(i) == vehId) {
                for (int j = 0; j < price.size(); j++) {
                    if (i == j) {
                        bidAmount = price.get(i);
                        if (bidAmount < lowestBid) {
                            lowestBid = bidAmount;
                            System.out.println("Lowest Bid Price: " + lowestBid);
                        }
                    }
                }
            } else {
                System.out.println("Vehicle does not exist.");
            }
        }
    }

    /**
     * To delete bid by its ID.
     */
    public void deleteBid() {
        int bidId = inputBidId();
        for (int i = 0; i < vehicles.size(); i++) {
            BidsManager vehBids = vehicles.get(i).getManageBids();
            HashMap<Integer, Bid> bids = vehBids.getBidsManage();
            if (bids.containsKey(bidId)) {
                bids.remove(bidId);
                System.out.println("Bid is deleted successfully!");
            } else {
                System.out.println("Invalid Bid ID.");
            }
        }
    }

    /**
     * To create a Vehicle instance type Sedan
     * @exception SedanException
     * @exception VehicleException
     */
    public void createSedan() {
        int vId = randomId();
        String[] vehicleName = inputVehicleName();
        int seatsNo = inputVehSeats();
        // create new instance for Sedan
        try {
            Sedan newSedan = new Sedan(vehicleName[0], vehicleName[1], vId, seatsNo);
            // add into arraylist
            this.vehicles.add(newSedan);
            System.out.println("Vehicle (Sedan) successfully added!");
            System.out.println("Vehicle details:");
            System.out.println(newSedan.description() + " | Seats: " + seatsNo);
        } catch (SedanException e) {
            System.out.println(e.getMessage());
        } catch (VehicleException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * To create a Vehicle instance type Truck
     * @exception TruckException
     * @exception VehicleException
     */
    public void createTruck() {
        int vId = randomId();
        String[] vehicleName = inputVehicleName();
        int wheelsNo = inputVehWheels();
        int vehCapacity = inputVehCapacity();
        // create new instance for Truck
        try {
            Truck newTruck = new Truck(vehicleName[0], vehicleName[1], vId, wheelsNo, vehCapacity);
            // add into arraylist
            this.vehicles.add(newTruck);
            System.out.println("Vehicle (Truck) successfully added!");
            System.out.println("Vehicle details:");
            System.out.println(newTruck.description() + " | Wheels: " + wheelsNo + " | Capacity: " + vehCapacity);
        } catch (TruckException e) {
            System.out.println(e.getMessage());
        } catch (VehicleException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * To create a Buyer instance
     */
    public void createBuyer() {
        String[] buyerName = inputBuyerName();
        int buyerId = randomId();
        Buyer newBuyer = Buyer.getInstance(buyerId, buyerName[0], buyerName[1]);
        if (newBuyer != null) {
            this.buyersRec.put(buyerId, newBuyer);
            System.out.println("Buyer is successfully added!");
            System.out.println("Buyer's details:");
            System.out.println(newBuyer.description());
        } else {
            System.out.println("Something wrong with the buyer's values!!!");
        }
    }

    /**
     * To create a Bid instance
     * @exception ParseException
     * @exception BidException
     */
    public void createBid() {
        int buyerId = inputBuyerId();
        double bidPrice = inputBidPrice();
        Date bidDate = inputBidDate();
        int vehId = inputVehId();
        int bidId = randomId();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVId() == vehId) {
                try {
                    vehicle.getManageBids().addBid(bidId, buyerId, bidPrice, bidDate);
                    System.out.println("Bid is successfully added!");
                    Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String formatDate = formatter.format(bidDate);
                    this.veh.add(vehId);
                    this.price.add(bidPrice);
                    System.out.println("Bid's details: "
                            + "Bid ID: " + bidId
                            + " | Buyer ID: " + buyersRec.get(buyerId).description()
                            + " | Price: $" + String.format("%.2f", bidPrice)
                            + " | Date: " + formatDate);
                } catch (ParseException e) {
                    System.out.println("Invalid Date Format");
                } catch (BidException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * To display fleet showing Vehicles and Bids added.
     */
    private void displayFleet() {
        for (int i = 0; i < vehicles.size(); i++) {
            String carDesc = "Car (" + (i+1) + ") " + vehicles.get(i).description() + " \n";
            BidsManager vehBids = vehicles.get(i).getManageBids();
            System.out.print(carDesc);
            HashMap<Integer, Bid> bids = vehBids.getBidsManage();
            for (Integer currentBuyer : bids.keySet()) {
                System.out.println("------------------------------------");
                Bid currentBid = bids.get(currentBuyer);
                System.out.println(currentBid.bidsDescription());
                System.out.println("------------------------------------");
            }
        }
    }

    /**
     * To display Buyers added by console input
     */
    public void displayBuyers() {
        int buyersCount = 1;
        System.out.println("Buyers:");
        for (Integer buyerId : buyersRec.keySet()) {
            Buyer buyer = buyersRec.get(buyerId);
            System.out.println(buyersCount + ") " + buyer.description());
            buyersCount++;
        }
    }

    /**
     * To generate random Id
     * @return int
     */
    private static int randomId(){
        Random random = new Random();
        int low = 1;
        int high = 99999;
        int randomId = random.nextInt(high - low) + low;
        return randomId;
    }

    /**
     * IO Console Input for Vehicle Name
     * @return A string list with Vehicle's maker and model
     */
    private static String[] inputVehicleName() {
        System.out.println("Please enter the vehicle's details:");
        System.out.println("------------------------------------");
        // ask for input of vehicle's maker
        Scanner scannerMaker = new Scanner(System.in);
        System.out.print("Maker: ");
        String vehicleMaker = scannerMaker.nextLine();
        // ask for input of vehicle's model
        Scanner scannerModel = new Scanner(System.in);
        System.out.print("Model: ");
        String vehicleModel = scannerModel.nextLine();
        return new String[]{vehicleMaker, vehicleModel};
    }

    /**
     * IO Console Input for Vehicle Seats for Vehicle Type Sedan
     * @return int (seats)
     * @exception Exception
     */
    public static int inputVehSeats() {
        int seats = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerSeats = new Scanner(System.in);
                System.out.print("Enter Sedan's seats: ");
                condition = false;
                seats = Integer.parseInt(scannerSeats.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid integer! Please enter a valid integer.");
            }
        }
        return seats;
    }

    /**
     * IO Console Input for Vehicle's Wheels for Vehicle Type Truck
     * @return int (wheels)
     * @exception Exception
     */
    public static int inputVehWheels() {
        int wheels = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerWheels = new Scanner(System.in);
                System.out.print("Enter number of wheels: ");
                condition = false;
                wheels = Integer.parseInt(scannerWheels.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid integer! Please enter a valid integer.");
            }
        }
        return wheels;
    }

    /**
     * IO Console Input for Vehicle's Capacity for Vehicle Type Truck
     * @return int (capacity)
     * @exception Exception
     */
    public static int inputVehCapacity() {
        int capacity = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerCapacity = new Scanner(System.in);
                System.out.print("Enter capacity: ");
                condition = false;
                capacity = Integer.parseInt(scannerCapacity.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid integer! Please enter a valid integer.");
            }
        }
        return capacity;
    }

    /**
     * IO Console Input for Buyer's Given Name and Family Name
     * @return a string list with buyer's given name, family name
     */
    private static String[] inputBuyerName(){
        System.out.println("Please enter the details below:");
        System.out.println("------------------------------------");
        // input buyer's given name
        Scanner scannerGivName = new Scanner(System.in);
        System.out.print("Buyer's first name: ");
        String buyerGivName = scannerGivName.nextLine();
        // input buyer's family name
        Scanner scannerFamName = new Scanner(System.in);
        System.out.print("Buyer's last name: ");
        String buyerFamName = scannerFamName.nextLine();

        return new String[]{buyerGivName, buyerFamName};
    }

    /**
     * IO Console Input for Vehicle's ID
     * @return int
     * @exception Exception
     */
    private int inputVehId() {
        int vehId = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerVehicleId = new Scanner(System.in);
                System.out.print("Vehicle's ID: ");
                vehId = scannerVehicleId.nextInt();
                condition = false;
            } catch (Exception e) {
                System.out.println("Invalid ID! Please enter a valid ID.");
            }
        }
        return vehId;
    }

    /**
     * IO Console Input for Buyer's ID
     * @return int
     * @exception Exception
     */
    private int inputBuyerId(){
        int buyerId = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerBuyerId = new Scanner(System.in);
                System.out.print("Buyer's ID: ");
                buyerId = scannerBuyerId.nextInt();
                condition = false;
                // check buyer ID match
                if (!this.buyersRec.containsKey(buyerId)) {
                    System.out.println("Buyer does not exist. Please enter again: ");
                    condition = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid ID! Please enter a valid ID.");
            }
        }
        return buyerId;
    }

    /**
     * IO Console Input for Bid's Price
     * @return double (Bid's Price)
     * @exception Exception
     */
    private double inputBidPrice(){
        double buyerBidPrice = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerBidPrice = new Scanner(System.in);
                System.out.print("Buyer's bid price: ");
                buyerBidPrice = scannerBidPrice.nextDouble();
                condition = false;
            } catch (Exception e) {
                System.out.println("Invalid bid price! Please enter a valid bid price");
            }
        }
        return buyerBidPrice;
    }

    /**
     * IO Console Input for Bid's Date
     * @return Date
     * @exception ParseException
     */
    private static Date inputBidDate() {
        Date buyerBidDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        boolean condition = true;
        while (condition) {
            try {
                // ensures dates entered are valid
                formatter.setLenient(false);

                Scanner scannerBidDate = new Scanner(System.in);
                System.out.print("Bid's date: ");
                buyerBidDate = formatter.parse(scannerBidDate.next());
                condition = false;
            } catch (ParseException e) {
                System.out.println("Please enter a valid date in the format dd/MM/yyyy");
            }
        }
        return buyerBidDate;
    }

    private static int inputBidId() {
        int bidId = 0;
        boolean condition = true;
        while (condition) {
            try {
                Scanner scannerBidId = new Scanner(System.in);
                System.out.print("Bid's ID: ");
                bidId = scannerBidId.nextInt();
                condition = false;
            } catch (Exception e) {
                System.out.println("Invalid ID! Please enter a valid ID.");
            }
        }
        return bidId;
    }
}

