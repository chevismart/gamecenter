package gamecenter.core.services;

/**
 * Created by Chevis on 15/4/1.
 */
public interface HttpRequestService<T, V> {
    T submit(V value);
}
