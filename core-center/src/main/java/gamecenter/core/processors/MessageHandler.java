package gamecenter.core.processors;

public interface MessageHandler<V> {
    void process(V value);
}
