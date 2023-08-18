package xyz.ttm.dia.service;

import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(VertxUnitRunner.class)
public class DiaServiceTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext(new VertxOptions()
            .setBlockedThreadCheckInterval(120000)
            .setMaxWorkerExecuteTime(60000)
            .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS));

    @Test(timeout = 2000)
    public void getPriceTest(TestContext testContext){
        List<String> assets = List.of("BTC", "ETH", "USDT", "DOT", "ATOM");

        Async async = testContext.async(assets.size());

        DiaService.create(rule.vertx())
                .getPrices(assets)
                .onSuccess(prices ->
                    prices.forEach((asset, price) -> {
                        testContext.assertTrue(price > 0);
                        async.countDown();
                    })
                )
                .onFailure(testContext::fail);
    }

}
