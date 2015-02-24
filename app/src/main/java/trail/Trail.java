package trail;

import android.content.Context;

import java.util.LinkedList;

import hackathon.com.theseus.R;

/**
 * Created by caioa_000 on 22/02/2015.
 */
public class Trail{

    LinkedList<String> coordinates;

    public Trail() {
        this.coordinates = new LinkedList<String>();
    }

    public Trail(LinkedList<String> trails) {
        this.coordinates = coordinates;
    }

    public LinkedList<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LinkedList<String> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isInTrail(String coordinate){
        if(this.coordinates.contains(coordinate)) return true;
        else return false;
    }

}
