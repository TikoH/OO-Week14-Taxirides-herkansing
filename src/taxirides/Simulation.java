package taxirides;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiko Huizinga - s4460898
 * @author Conny Blach - s4329872
 *
 */
public class Simulation {

    public static final int TRAIN_TRIPS = 10;
    public static final int MIN_TRAVELLERS = 60;
    public static final int MAX_TRAVELLERS = 90;
    public static final int CAPACITYSMALL = 4;
    public static final int CAPACITYLARGE = 7;
    public static final int TIMESMALL = 2;
    public static final int TIMELARGE = 3;
    public static final int NROFTAXIS = 4;
    public static final int NROFSMALLTAXIS = 2;

    private Taxi[] taxis;
    private Thread[] threads;
    private Train train;
    private Station station;

    private boolean hasEnded = false;
    private int nextTaxi = 0;

    public Simulation() {
        station = new Station();
        taxis = new Taxi[NROFTAXIS];
        threads = new Thread[NROFTAXIS];
        train = new Train(station);
        for (int i = 0; i < NROFTAXIS; i++) {
            taxis[i] = i < NROFSMALLTAXIS ? new Taxi(i + 1, CAPACITYSMALL, TIMESMALL, station) : new Taxi(i + 1,
                    CAPACITYLARGE, TIMELARGE, station);
            threads[i] = new Thread(taxis[i]);
        }
        startTaxis();
    }

    /**
     * Start the taxithreads. 
     */
    private void startTaxis() {
        for (int i = 0; i < NROFTAXIS; i++) {
            threads[i].start();
        }
    }

    /**
     * The step function will keep the trains coming as long as less trains 
     * have arrived then the number of planned trips. When all trains have arrived
     * and there are no more passengers left, the station will be closed. 
     */
    public void step() {
        if (station.getNrOfPassengersWaiting() > 0) {
            System.out.print("");           //If nothing here, the compiler will skip this step while it is important for the simulation..
        } else if (train.getNrOfTrips() < TRAIN_TRIPS) {
            train.getIn(Util.getRandomNumber(MIN_TRAVELLERS, MAX_TRAVELLERS));
            train.getOff();
        } else {
            train.closeStation();
            hasEnded = true;
        }
    }

    public boolean ended() {
        return hasEnded;
    }

    public void showStatistics() {
        System.out.println("All persons have been transported");
        System.out.println("Total time of this simulation:" + calcTotalTime(taxis));
        System.out.println("Total nr of train travellers:" + station.getTotalNrOfPassengers());
        System.out.println("Total nr of persons transported in this simulation:" + calcTotalNrOfPassengers(taxis));
    }

    /**
     * Calculates the total time of the simulation by looping over all taxis
     *
     * @param taxis
     * @return total time
     */
    private static int calcTotalTime(Taxi[] taxis) {
        int time = 0;
        for (Taxi taxi : taxis) {
            time = time + taxi.calcTotalTime();
        }
        return time;
    }

    /**
     * Calculates the total number of passengers that has been transported by
     * looping over all taxis
     *
     * @param taxis
     * @return total number of passengers
     */
    private static int calcTotalNrOfPassengers(Taxi[] taxis) {
        int total = 0;
        for (Taxi taxi : taxis) {
            total += taxi.getTotalNrOfPassengers();
        }
        return total;
    }

}
