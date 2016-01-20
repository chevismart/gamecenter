package gamecenter.core.processors;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by chevi on 2016/1/21.
 */
public class RegexIncludeFilterTest {

    @Test
    public void normalTextMatcher() throws Exception {
        String regex="test";
        Filter<String> filter = new RegexIncludeFilter(regex);
        assertThat(filter.shouldInclude("test"), is(true));
    }

    @Test
    public void normalTextMisMatcher() throws Exception {
        String regex="test";
        Filter<String> filter = new RegexIncludeFilter(regex);
        assertThat(filter.shouldInclude("/test"), is(false));
    }

    @Test
    public void regexMacher() throws Exception {
        String regex="^[1][\\d]{10}";
        Filter<String> filter = new RegexIncludeFilter(regex);
        assertThat(filter.shouldInclude("12345278901"), is(true));
    }

    @Test
    public void regexMisMacher() throws Exception {
        String regex="^[1][\\d]{10}";
        Filter<String> filter = new RegexIncludeFilter(regex);
        assertThat(filter.shouldInclude("123456"), is(false));
    }

    @Test
    public void testName() throws Exception {
        String regex="^[t][\\d]{1,3}";
        Filter<String> filter = new RegexIncludeFilter(regex);
        assertThat(filter.shouldInclude("t123"), is(true));
    }
}