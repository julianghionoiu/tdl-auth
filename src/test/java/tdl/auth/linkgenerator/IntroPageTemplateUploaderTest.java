package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static tdl.auth.test.TestConfiguration.TEST_PUBLIC_PAGE_BUCKET;

public class IntroPageTemplateUploaderTest {

    private PageUploader uploader;
    private AmazonS3 s3client;

    @Before
    public void setUp() throws Exception {
        s3client = mock(AmazonS3.class);
        uploader = spy(new PageUploader(s3client, TEST_PUBLIC_PAGE_BUCKET));

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void uploadPage() throws IOException, TemplateException {
        doCallRealMethod().when(uploader).uploadPage(any(), any());
        doReturn(mock(PutObjectResult.class)).when(s3client).putObject(any());

        URL url = new URL("http://www.example.com");
        doReturn(url).when(s3client).getUrl(anyString(), anyString());
        
        String result = uploader.uploadPage("xyz01","content");
        
        verify(s3client, times(1)).putObject(any());
        verify(s3client, times(1)).getUrl(any(), contains("xyz01"));

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
