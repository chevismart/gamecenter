package gamecenter.core.processors;

/**
 * Created by Chevis on 15/9/3.
 */
public interface Filter<V> {

    public static Filter<String> alwaysStringPassFilter = new Filter<String>() {
        public boolean shouldInclude(String value) {
            return true;
        }
    };

    boolean shouldInclude(V value);

}
