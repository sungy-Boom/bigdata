package big_data.flatmap;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import big_data.util.Stock;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class StockFlatMap extends RichFlatMapFunction<Stock, Stock> {

    private String stepName;

    public StockFlatMap(String stepName) {
        this.stepName = stepName;
    }

    @Override
    public void flatMap(Stock stock, Collector<Stock> collector) throws Exception {
        System.out.println(stepName);
        System.out.println(stock.toString());
    }
}
