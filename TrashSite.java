/**
 *  * Quinlan John
 *  * ICS 440-50 Parallel and Distributed Algorithms
 *
 * Data Structure class for managing file data providing getter and setter functionality.
 *
 */
public class TrashSite implements Comparable<TrashSite> {

    double distanceTo;
    String URL;
    String name;
    String address;
    double lat;
    double lng;
    String file;

    public TrashSite() {
        lat = Double.MAX_VALUE;
        lng = Double.MAX_VALUE;
    }

    public double getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(double distanceTo) {
        this.distanceTo = distanceTo;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    /**
     *  providing comparable functionality for determining distance
     *
     * @param o takes a trashsite argument object
     * @@return int 0 is equal to, 1 is less than -1 is else
     *
     */
    @Override
    public int compareTo(TrashSite o) {
        if(o.distanceTo == distanceTo)  return 0;
        if(o.distanceTo < distanceTo) {
            return 1;
        }
        return -1;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
