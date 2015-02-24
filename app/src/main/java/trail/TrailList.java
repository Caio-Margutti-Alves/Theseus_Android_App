package trail;

import android.content.Context;

import java.util.Arrays;
import java.util.LinkedList;

import hackathon.com.theseus.R;

/**
 * Created by caioa_000 on 22/02/2015.
 */
public class TrailList {

    LinkedList<Trail> trails;

    public TrailList() {
        this.trails = trails;
    }


    public TrailList(LinkedList<Trail> trails) {
        this.trails = trails;
    }

    public Trail getTrail(int index) {
        return trails.get(index);
    }

    public LinkedList<Trail> getAllTrails() {
        return trails;
    }

    public void setTrail(int index, Trail trail) {
        trails.set(index, trail);
    }

    public void startTrailList(Context context){
            trails =  new LinkedList<Trail>();

            Trail trail = new Trail();
            trail = new Trail(new LinkedList(Arrays.asList(context.getResources().getStringArray(R.array.trail_jones_valley_corridor))));

            trails.add(trail);

    }

    //public static String updateTrailList(Context context, int posicao){
    //}

    public boolean isInTrail(String coordinate){
        if(this.trails.contains(coordinate)) return true;
        else return false;
    }
}
