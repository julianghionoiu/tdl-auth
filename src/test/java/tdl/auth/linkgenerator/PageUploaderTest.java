package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static tdl.auth.test.TestConfiguration.TEST_BUCKET;

public class PageUploaderTest {

    private PageUploader uploader;
    private AmazonS3 s3client;

    @Before
    public void setUp() throws Exception {
        s3client = mock(AmazonS3.class);
        uploader = spy(new PageUploader(s3client, TEST_BUCKET));

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void uploadPage() throws MalformedURLException {
        doCallRealMethod().when(uploader).uploadPage(any());
        Page page = mock(Page.class);
        doReturn("Hello World").when(page).getContent();
        
        doReturn(mock(PutObjectResult.class)).when(s3client).putObject(any());

        URL url = new URL("http://www.example.com");
        doReturn(url).when(s3client).getUrl(anyString(), anyString());
        
        String result = uploader.uploadPage(page);
        
        verify(s3client, times(1)).putObject(any());
        verify(s3client, times(1)).getUrl(anyString(), anyString());

        assertEquals(result, "http://www.example.com");
    }

    @Test
    public void createClient() throws IOException, TemplateException {
        AmazonS3ClientBuilder
                .standard()
                .build();
        Object client = AmazonS3ClientBuilder
                .standard()
                .build();
        assertThat(client, instanceOf(AmazonS3.class));
    }
}
