import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 *  * Quinlan John
 *  * ICS 440-50 Parallel and Distributed Algorithms
 *
    TrashSiteProximity Class for doing all calculations
 *
 */
public class TrashSiteProximity implements Callable<ArrayList<TrashSite>> {

    private double userLatitude;
    private double userLongitude;
    private ArrayList<TrashSite> facilities;
    private ArrayList<File> files;

    /**
     *  default constructor
     *
     */
    public TrashSiteProximity() {
        this.userLatitude = 0;
        this.userLongitude = 0;
    }

    /**
     *  constructor
     *
     */
    public TrashSiteProximity(double userLatitude, double userLongitude) {
        this.userLongitude = userLongitude;
        this.userLatitude = userLatitude;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    /**
     *
     * parse the file for the data
     *
     * @param f takes a file as an argument
     *
     */
    private void fileParse(File f) throws IOException {
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(f));
        facilities = new ArrayList<>(8);
        int i = 0;
        while ((line = br.readLine()) !=  null){
            if(i == 0) {
                i++;
            }
                else {
                String[] parsedLine = line.split(",");
                TrashSite trashSite = new TrashSite();
                trashSite.setURL(parsedLine[0].replaceAll("^\"|\"$", ""));
                trashSite.setName(parsedLine[1].replaceAll("^\"|\"$", ""));
                trashSite.setAddress(parsedLine[2].replaceAll("^\"|\"$", ""));
                for(int col = 26 ; col < parsedLine.length ; col++){
                    try {
                        trashSite.setLat(Double.parseDouble(parsedLine[col].replaceAll("^\"|\"$", "")));
                        trashSite.setLng(Double.parseDouble(parsedLine[col+1].replaceAll("^\"|\"$", "")));
                        break;
                    } catch (NumberFormatException nex) {
                    }
                }
                if(trashSite.getLat() != Double.MAX_VALUE && trashSite.getLng() != Double.MAX_VALUE) {
                    double dist = distance(trashSite.getLat(), trashSite.getLng());
                    trashSite.setDistanceTo(dist);
                    facilities.add(trashSite);
                    trashSite.setFile(f.getName());
                }
            }
            i++;
        }
    }

    public ArrayList<TrashSite> getNearest() {
        Collections.sort(facilities);
        ArrayList<TrashSite> sites = new ArrayList<>();
        for(int i = 0 ; i < 8 && i < facilities.size() ; i++) {
            sites.add(facilities.get(i));

        }
        return sites;
    }


    /**
     *
     * @author Ryan Hankins
     *
     * distance method calculates the distance using parsed out longitidinal and latitudinal
     * data from the source file.
     *
     * @param lat2, long2 The longitudinal and latitudinal values for each location
     *              to calcualate distance
     * @return distance as a double between both locations latitutde and longitiude values.
     *
     */
    public double distance(double lat2, double long2) {
        double lat1 = Math.toRadians(userLatitude);
        double long1 = Math.toRadians(userLongitude);
        lat2 = Math.toRadians(lat2);
        long2 = Math.toRadians(long2);
        return 6371 * 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin((lat2 - lat1) / 2), 2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                                Math.pow(Math.sin((long2 - long1) / 2), 2)));
    }

    /**
     *  call functions implemented for callables. Creates array facilities
     *  calls fileParse for files.
     *
     *
     * @return Arraylist<TrashSite> of facilities
     *
     */
    @Override
    public ArrayList<TrashSite> call() throws Exception {
        facilities = new ArrayList<>();
        for(int i = 0 ; i < files.size() ; i++) {
            fileParse(files.get(i));
        }
        return facilities;
    }
}
