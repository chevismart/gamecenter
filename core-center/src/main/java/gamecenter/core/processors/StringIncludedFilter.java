package gamecenter.core.processors;

public class StringIncludedFilter implements Filter<String> {
    private final String except;

    public StringIncludedFilter(String expect) {
        this.except = expect;
    }

    public boolean shouldInclude(String value) {
        return except.equals(value);
    }
}
