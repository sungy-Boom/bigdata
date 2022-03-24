package flink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import flink.flatmap.StockFlatMap;
import flink.source.StockSource;
import flink.util.Stock;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class StockStream {
    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream<Stock> ds =
                env.addSource(new StockSource("/Users/umi/Desktop/projects/self/bigdata/flink/src/file/stock"));

        //计算最大价格
        ds = ds.keyBy(Stock::getSymbol).max("price");

        ds.flatMap(new StockFlatMap("max price"));

        //转换汇率
        ds = ds.map(stock -> {
            String price = stock.getPrice();
            stock.setPrice(Double.parseDouble(price) * 7 + "");
            return stock;
        });
        ds.flatMap(new StockFlatMap("转换汇率"));

        ds = ds.filter(stock -> Double.parseDouble(stock.getPrice()) > 297.27d);
        ds.flatMap(new StockFlatMap("filter"));

        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
}
