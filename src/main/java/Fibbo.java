import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.http.client.HttpClientSpec;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.RatpackServer;

import java.net.URI;
import java.time.Duration;

public class Fibbo {
    public static void main(String[] args) throws Exception {
        HttpClient httpClient = HttpClient.of(httpClientSpec -> httpClientSpec.readTimeout(Duration.ofMinutes(1)));
        RatpackServer.start(ratpackServerSpec ->
                ratpackServerSpec.handlers(chain -> chain.
                        prefix("fibb", fibb -> fibb.get(":n", ctx -> {
                            long n = Long.parseLong((ctx.getAllPathTokens().get("n")));
                            if(n < 2) {
                                ctx.render("1");
                            } else {
                                final Promise<ReceivedResponse> f1 = httpClient.get(new URI("http://localhost:5050/fibb/" + (n - 1)));
                                final Promise<Long> promise1 = f1.map(result -> Long.parseLong(result.getBody().getText()));

                                final Promise<ReceivedResponse> f2 = httpClient.get(new URI("http://localhost:5050/fibb/" + (n - 2)));
                                final Promise<Long> promise2 = f2.map(result -> Long.parseLong(result.getBody().getText()));

                                promise1.then(v1 -> promise2.then(v2 -> ctx.render(String.valueOf(v1 + v2))));
                            }

                        }))));
    }

    public long fibbo(long n) {
        if(n < 2) {
            return 1;
        } else {

        }
        return 1L;
    }
}