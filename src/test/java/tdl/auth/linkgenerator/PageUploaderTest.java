package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class PageUploaderTest {

    @Test
    public void constructor() {
        PageUploader uploader = new PageUploader("testbucket");
    }
    
    @Test
    public void uploadPage() throws MalformedURLException {
        PageUploader uploader = spy(new PageUploader("testbucket"));
        doCallRealMethod().when(uploader).uploadPage(any());
        Page page = mock(Page.class);
        doReturn("Hello World").when(page).getContent();
        
        AmazonS3 client = mock(AmazonS3.class);
        doReturn(mock(PutObjectResult.class)).when(client).putObject(any());
        doReturn(client).when(uploader).createClient();
        
        URL url = new URL("http://www.example.com");
        doReturn(url).when(client).getUrl(anyString(), anyString());
        
        String result = uploader.uploadPage(page);
        
        verify(client, times(1)).putObject(any());
        verify(client, times(1)).getUrl(anyString(), anyString());

        assertEquals(result, "http://www.example.com");
    }

    @Test
    public void createClient() throws IOException, TemplateException {
        PageUploader uploader = mock(PageUploader.class);
        doCallRealMethod().when(uploader).createClient();
        Object client = uploader.createClient();
        assertThat(client, instanceOf(AmazonS3.class));
    }
}
