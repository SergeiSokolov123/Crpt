import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrptApi {

    private final int requestLimit;
    private final TimeUnit timeUnit;
    private long lastRequestTime = 0;
    private int requestCount = 0;
    private final Object lock = new Object();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDocument(Object document, String signature) throws IOException, InterruptedException {
        synchronized (lock) {
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lastRequestTime;
            if (timePassed < timeUnit.toMillis(1)) {
                // Уже превышен лимит запросов в единицу времени
                if (requestCount >= requestLimit) {
                    long sleepTime = timeUnit.toMillis(1) - timePassed;
                    Thread.sleep(sleepTime);
                }
            } else {
                // Сбрасываем счетчик запросов
                requestCount = 0;
            }
            // Увеличиваем счетчик запросов
            requestCount++;
            lastRequestTime = currentTime;
        }

        // Отправляем запрос к API Честного знака
        String url = "https://example.com/api/create_document";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        StringEntity params = new StringEntity(objectMapper.writeValueAsString(document));
        httpPost.setEntity(params);
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("signature", signature);
        httpClient.execute(httpPost);
    }

}
