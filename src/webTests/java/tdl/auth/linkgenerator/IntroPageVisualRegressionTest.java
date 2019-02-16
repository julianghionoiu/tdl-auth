package tdl.auth.linkgenerator;

import com.hotwire.imageassert.Image;
import com.hotwire.imageassert.ImageAssert;
import com.hotwire.imageassert.report.HTMLReportResultListener;
import freemarker.template.TemplateException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.NginxContainer;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;
import tdl.auth.webTests.support.TestExceptionResultListener;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntroPageVisualRegressionTest {

    private static Path staticResourcesPath;
    private static Path nginxWebServePath;
    private static Path webdriverRecordingPath;
    private static Path webdriverScreenshotPath;
    private static Path imageComparisonReportsPath;
    private static AShot aShot;
    private static IntroPageTemplate pageTemplate;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void setup() throws IOException {
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

        ViewportPastingDecorator strategy =
                new ViewportPastingDecorator(new SimpleShootingStrategy()).withScrollTimeout(100);
        aShot = new AShot().shootingStrategy(strategy);

        pageTemplate = new IntroPageTemplate(
                "intro.html.ftl",
                "../staticResources",
                "https://www.example.com/production/verify");
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
    //NOTE: Change BrowserWebDriverContainer.VncRecordingMode if you want to visually debug failing test
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, webdriverRecordingPath.toFile())
                    .withCapabilities(new ChromeOptions());

    //~~~~~~~ Tests

    @Test
    public void testValidLinkDefaultView() throws Exception {

        IntroPage p = generateAndServe(validLinkPageParameters()
                .build());

        p.assertScreenVisuallyMatches("valid_link_default_view.png");
        p.assertDownloadLinkIs("https://get.accelerate.io/v0/runner-for-java-linux.zip");
    }

    @Test
    public void testValidLinkSelectRunner() throws Exception {

        IntroPage p = generateAndServe(validLinkPageParameters()
                .build());

        p.selectPlatform("Windows");
        p.selectLanguage("Python");

        p.assertScreenVisuallyMatches("valid_link_select_runner.png");
        p.assertDownloadLinkIs("https://get.accelerate.io/v0/runner-for-python-windows.zip");
    }


    @Test
    public void testAllTogglesChanged() throws Exception { ;
        IntroPageParameters introPageParameters = validLinkPageParameters()
                .enableNoVideoOption(false)
                .enableApplyPressure(false)
                .enableReportSharing(false)
                .build();

        IntroPage p = generateAndServe(introPageParameters);

        p.assertScreenVisuallyMatches("all_toggles_set_to_false.png");
    }

    @Test
    public void testExpiredLink() throws Exception { ;
        IntroPageParameters introPageParameters = expiredLinkPageParameters()
                .build();

        IntroPage p = generateAndServe(introPageParameters);

        p.assertScreenVisuallyMatches("expired_link.png");
    }

    //~~~~~~~ The Page object

    @SuppressWarnings("SameParameterValue")
    static class IntroPage {

        WebDriver webDriver;
        IntroPage(WebDriver webDriver) {
            this.webDriver = webDriver;
        }

        void selectLanguage(final String language) {
            webDriver.findElement(By.id(language.toLowerCase() + "-tab")).click();
        }

        void selectPlatform(final String platform) {
            webDriver.findElement(By.id(platform.toLowerCase() + "-tab")).click();
        }

        void assertDownloadLinkIs(String expected) {
            String defaultDownloadLink = webDriver.findElement(By.className("download-link")).getAttribute("href");
            assertThat(defaultDownloadLink, equalTo(expected));
        }

        void assertScreenVisuallyMatches(String screenshotName) throws IOException {
            Path screenshot1 = webdriverScreenshotPath.resolve(screenshotName);
            ImageIO.write(aShot.takeScreenshot(webDriver).getImage(), "PNG", screenshot1.toFile());

            Path reportPath = imageComparisonReportsPath.resolve(screenshotName);
            ImageAssert.assertThat(Image.load(screenshot1.toFile()))
                    .reportingTo(new HTMLReportResultListener(reportPath.toFile()))
                    .reportingTo(new TestExceptionResultListener("Images are different. See comparison report at: " + reportPath.toAbsolutePath()))
                    .isEqualTo(resource(screenshotName));
        }


    }

    //~~~~~~~ Helpers

    private static IntroPageParameters.IntroPageParametersBuilder validLinkPageParameters() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return IntroPageParameters.builder()
                .expirationDate(simpleDateFormat.parse("31/02/2019"))
                .fakeCurrentDate(Optional.of(simpleDateFormat.parse("30/02/2019")));
    }

    private static IntroPageParameters.IntroPageParametersBuilder expiredLinkPageParameters() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return IntroPageParameters.builder()
                .expirationDate(simpleDateFormat.parse("1/02/2019"))
                .fakeCurrentDate(Optional.of(simpleDateFormat.parse("2/02/2019")));
    }


    private IntroPage generateAndServe(IntroPageParameters introPageParameters)
            throws IOException, TemplateException {
        String content = pageTemplate.generateContent(introPageParameters);
        Files.write(nginxWebServePath.resolve("index.html"), content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get("http://" + getHostIp() + ":" + nginx.getMappedPort(80) + "/page/index.html");
        return new IntroPage(webDriver);
    }

    @SuppressWarnings("SameParameterValue")
    private static Image resource(String relativeTestResource) {
        return Image.load(IntroPageVisualRegressionTest.class.getResourceAsStream(relativeTestResource));
    }

    private String getHostIp() {
        try {
            return chrome.getTestHostIpAddress();
        } catch (RuntimeException e) {
            return "docker.for.mac.host.internal";
        }
    }
}
