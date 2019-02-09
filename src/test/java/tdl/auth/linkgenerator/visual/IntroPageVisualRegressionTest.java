package tdl.auth.linkgenerator.visual;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.NginxContainer;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.rnorth.visibleassertions.VisibleAssertions.assertTrue;

public class IntroPageVisualRegressionTest {

    private static File tempFolder;

    @BeforeClass
    public static void setup() {
        tempFolder = new File("./build/serve");
        tempFolder.delete();
        tempFolder.mkdir();
    }

    @AfterClass
    public static void teardown() throws IOException {
        if (tempFolder.exists()) {
            FileUtils.deleteDirectory(tempFolder); //apache-commons-io
        }
    }

    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withCapabilities(new ChromeOptions());

    @Rule
    public NginxContainer nginx = new NginxContainer<>()
            .withCustomContent(tempFolder.getAbsolutePath());

    @Test
    public void render_page() {
        chrome.getWebDriver().get("http://www.google.com");
    }


    @Test
    public void testSimple() throws Exception {
        File indexFile = new File(tempFolder, "index.html");
        PrintStream printStream = new PrintStream(new FileOutputStream(indexFile));
        printStream.println("<html><body>This worked</body></html>");


        RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get("http://" + getHostIp() + ":" + nginx.getMappedPort(80) + "/");
        String source = webDriver.getPageSource();

        assertTrue("Using URLConnection, an HTTP GET from the nginx server returns the index.html from the custom content directory",
                source.contains("This worked"));
    }

    private String getHostIp() {
        try {
            return chrome.getTestHostIpAddress();
        } catch (RuntimeException e) {
            return "docker.for.mac.host.internal";
        }
    }
}
