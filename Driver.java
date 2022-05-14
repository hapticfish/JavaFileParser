import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * * Quinlan John
 * * ICS 440-50 Parallel and Distributed Algorithms
 * <p>
 * Driver supports testing of program
 */
public class Driver {

    private static double userLatitude;
    private static double userLongitude;
    private static String path="";

    /**
     * collect user input for calculations
     */
    private static void input() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter your locations Latitude: ");

        userLatitude = reader.nextDouble();
        System.out.println("Please enter your locations Longitude: ");

        userLongitude = reader.nextDouble();
        System.out.println("Please enter the local directory path for the Data file: ");
        path = reader.next();

    }


    /**
     * Divide method divides the files up into separate buckets for processing
     *
     * @param path of the data being loaded, n is the number of buckets
     * @return array list of array list of Files
     */
    private static ArrayList<ArrayList<File>> divide(String path, int n) {
        File f = new File(path);
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles();

            ArrayList<ArrayList<File>> divisions = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                divisions.add(new ArrayList<>());
            }
            for (int i = 0; i < files.length; i++) {
                int pos = i % n;
                    divisions.get(pos).add(files[i]);
            }
            return divisions;
        }
        return null;
    }//end divide



    public static void main(String[] args) throws Exception {

        input();

        ArrayList<TrashSite>[] results = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            results[i] = new ArrayList<>();
        }
        //creating a thread pool of 8 threads
        ExecutorService executor = Executors.newFixedThreadPool(8);
        ArrayList<TrashSiteProximity> tasks = new ArrayList<>();
        ArrayList<ArrayList<File>> files = divide(path, 8);
        for (int i = 0; i < 8; i++) {
            TrashSiteProximity tp = new TrashSiteProximity(userLatitude, userLongitude);
            tp.setFiles(files.get(i));
            tasks.add(tp);
        }
        List<Future<ArrayList<TrashSite>>> listofFutures = executor.invokeAll(tasks);
        TreeSet<TrashSite> set = new TreeSet<>();
        Set<TrashSite> finalResult = Collections.synchronizedSet(set);
        List<Future<Void>> gather = new ArrayList<>();
        for(int i = 0 ; i < listofFutures.size() ; i++) {
            Future<ArrayList<TrashSite>> result = listofFutures.get(i);
            int pos = i % 4;
            results[pos].addAll(result.get());
        }

        for(int i = 0 ; i < 4 ; i++) {
            int finalI = i;
            executor.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    finalResult.addAll(results[finalI]);
                    return null;
                }
            }).get();
        }
        printFinal(finalResult);
    }

    /**
     * print the final result per specification, nearest top 8 results from
     * user entered Lat and Long
     *
     */
    public static void printFinal(Collection<TrashSite> facilities) {

        int i = 0;
        for (TrashSite f : facilities) {
            System.out.println("URL=" + f.getURL() + " name=" + f.getName() + " address="+ f.getAddress() +
                   " latitude="+ f.getLat()+ " longitude="+ f.getLng() + "  distance="+ f.getDistanceTo() + " "
                    + f.getFile());
            if (i == 8)
                break;
            i++;
        }
    }


}
