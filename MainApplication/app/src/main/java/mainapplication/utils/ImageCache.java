package mainapplication.utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luis Gouveia on 20/12/2017.
 */

public class ImageCache {
    private static Map<Integer, Bitmap> facilitiesCache = new HashMap<>();

    public static void addFacilityImage(int id, Bitmap image){
        facilitiesCache.put(id,image);
    }

    public static boolean contains(int id){
        return facilitiesCache.containsKey(id);
    }
    public static Bitmap getImage(int id){
        return facilitiesCache.get(id);
    }

}
