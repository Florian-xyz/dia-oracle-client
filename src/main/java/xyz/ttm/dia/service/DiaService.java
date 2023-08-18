package xyz.ttm.dia.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.Map;

public interface DiaService {

    static DiaService create(Vertx vertx) {
        return new DiaServiceImpl(vertx);
    }

    /**
     * Get assets quotations
     *
     * @param assets List of token symbols
     * @return A map of asset with quotation
     */
    Future<Map<String, Double>> getPrices(List<String> assets);

}
