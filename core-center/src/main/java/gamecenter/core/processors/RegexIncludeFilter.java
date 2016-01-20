package gamecenter.core.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexIncludeFilter implements Filter<String> {

    private final Pattern pattern;

    public RegexIncludeFilter(String regex) {
        pattern = Pattern.compile(regex);
    }

    public boolean shouldInclude(String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
