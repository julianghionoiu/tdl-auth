package tdl.auth.linkgenerator;

import com.hotwire.imageassert.Image;
import com.hotwire.imageassert.ImageAssert;
import com.hotwire.imageassert.report.HTMLReportResultListener;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.NginxContainer;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;
import tdl.auth.webTests.support.TestExceptionResultListener;

import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;

public class IntroPageVisualRegressionTest {

    private static Path staticResourcesPath;
    private static Path nginxWebServePath;
    private static Path webdriverRecordingPath;
    private static Path webdriverScreenshotPath;
    private static Path imageComparisonReportsPath;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void setup() {
        Path webTestsPath = Paths.get("./build/web-tests");
        webTestsPath.toFile().mkdir();

        staticResourcesPath = Paths.get("./staticResources");

        nginxWebServePath = webTestsPath.resolve("nginx");
        nginxWebServePath.toFile().delete();
        nginxWebServePath.toFile().mkdir();

        webdriverRecordingPath = webTestsPath.resolve("recordings");
        webdriverRecordingPath.toFile().mkdir();
        webdriverScreenshotPath = webTestsPath.resolve("screenshots");
        webdriverScreenshotPath.toFile().mkdir();

        imageComparisonReportsPath = webTestsPath.resolve("comparison-reports");
        imageComparisonReportsPath.toFile().mkdir();
    }

    @Rule
    public NginxContainer nginx = new NginxContainer<>()
            .withFileSystemBind(nginxWebServePath.toAbsolutePath().toString(),
                    "/usr/share/nginx/html/page",
                    BindMode.READ_ONLY)
            .withFileSystemBind(staticResourcesPath.toAbsolutePath().toString(),
                    "/usr/share/nginx/html/staticResources",
                    BindMode.READ_ONLY);

    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING, webdriverRecordingPath.toFile())
                    .withCapabilities(new ChromeOptions());

    @Test
    public void testSimple() throws Exception {
        IntroPageTemplate template = new IntroPageTemplate(
                "intro.html.ftl",
                "../staticResources",
                "https://www.example.com/production/verify");
        IntroPageParameters introPageParameters = new IntroPageParameters().toBuilder()
                .headerImageName("makers.jpg")
                .mainChallengeTitle("Developer Insights")
                .sponsorName("Makers Academy")
                .codingSessionDurationLabel("3 hours")
                .allowNoVideoOption(true)
                .username("xwya01")
                .challenge("CHK")
                .token("asdf")
                .expirationDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/02/2019"))
                .journeyId("myJourneyId")
                .build();
        String content = template.generateContent(introPageParameters);
        Files.write(nginxWebServePath.resolve("index.html"), content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get("http://" + getHostIp() + ":" + nginx.getMappedPort(80) + "/page/index.html");

        ViewportPastingDecorator strategy =
                new ViewportPastingDecorator(new SimpleShootingStrategy()).withScrollTimeout(100);
        final Screenshot screenshot = new AShot().shootingStrategy(
                strategy).takeScreenshot(webDriver);

        Path screenshot1 = webdriverScreenshotPath.resolve("test1.png");
        ImageIO.write(screenshot.getImage(), "PNG", screenshot1.toFile());

        Path reportPath = imageComparisonReportsPath.resolve("test1");
        ImageAssert.assertThat(Image.load(screenshot1.toFile()))
                .reportingTo(new HTMLReportResultListener(reportPath.toFile()))
                .reportingTo(new TestExceptionResultListener("Images are different. See comparison report at: " + reportPath.toAbsolutePath()))
                .isEqualTo(resource("expected_test.png"));
    }

    @SuppressWarnings("SameParameterValue")
    private Image resource(String relativeTestResource) {
        return Image.load(this.getClass().getResourceAsStream(relativeTestResource));
    }

    private String getHostIp() {
        try {
            return chrome.getTestHostIpAddress();
        } catch (RuntimeException e) {
            return "docker.for.mac.host.internal";
        }
    }
}
