package kis.di.mvc;

import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.assertThat;

/**
 *
 * @author naoki
 */
public class ServerTest {

    @Test
    public void testTrimSlash() {
        assertThat(Server.trimSlash("/test"), is("test"));
        assertThat(Server.trimSlash("/test/"), is("test"));
        assertThat(Server.trimSlash("test/"), is("test"));
        assertThat(Server.trimSlash("test/test"), is("test/test"));
    }

}
