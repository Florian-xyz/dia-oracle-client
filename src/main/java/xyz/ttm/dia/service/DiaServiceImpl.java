package xyz.ttm.dia.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import xyz.ttm.dia.constant.DiaConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiaServiceImpl implements DiaService {

    private final WebClient webClient;

    public DiaServiceImpl(Vertx vertx) {
        super();
        webClient = WebClient.create(vertx);
    }

    @Override
    public Future<Map<String, Double>> getPrices(List<String> assets) {
        List<Future> futurs = new ArrayList<>();

        for (String asset : assets) {
            String assetPriceEndpoint = DiaConstant.API_REST_URL + DiaConstant.ENDPOINT_PRICE + asset;
            futurs.add(webClient.getAbs(assetPriceEndpoint).send());
        }

        return CompositeFuture
                .join(futurs)
                .compose(compositeFuture -> {
                    Map<String, Double> prices = new HashMap<>();
                    for (HttpResponse<JsonObject> result : compositeFuture.<HttpResponse<JsonObject>>list()) {
                        if(result.statusCode() == HttpResponseStatus.OK.code()){
                            JsonObject response = result.bodyAsJsonObject();
                            String asset = response.getString(DiaConstant.JSON_KEY_SYMBOL);
                            prices.put(asset, response.getDouble(DiaConstant.JSON_KEY_PRICE));
                        }
                    }
                    return Future.succeededFuture(prices);
                });
    }

}
