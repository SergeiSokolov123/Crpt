import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class CrptApiTest {

    @Test
    public void testCreateDocument() throws IOException, InterruptedException {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 2);
        Object document1 = new Object();
        Object document2 = new Object();
        String signature = "signature";

        crptApi.createDocument(document1, signature);
        crptApi.createDocument(document2, signature);
        Thread.sleep(1000);
        crptApi.createDocument(document1, signature);
        crptApi.createDocument(document2, signature);
    }

}
