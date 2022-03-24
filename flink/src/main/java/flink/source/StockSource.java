package flink.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import com.common.LocalTimeUtil;

import flink.util.Stock;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class StockSource extends RichSourceFunction<Stock> {

    private Boolean isRunning = true;
    private InputStream stream;
    private String path;

    public StockSource(String path) {
        this.path = path;
    }

    @Override
    public void run(SourceContext<Stock> sourceContext) throws Exception {
        File file = new File(path);
        stream = new FileInputStream(file);
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(stream));
        String line;

        while (isRunning && (line = bufReader.readLine()) != null) {
            String[] data = line.split(",");
            String symbol = data[0];
            long timestamp = LocalTimeUtil.string2Long(data[1] + " " + data[2], "yyyyMMdd HHmmss");
            String price = data[3];
            String amount = data[4];
            Stock stock = Stock.of(symbol, String.valueOf(timestamp), price, amount);
            Thread.sleep(1000); //一秒钟发一次
            System.out.println(System.currentTimeMillis());
            System.out.println(stock.toString());
            sourceContext.collect(stock);
        }
    }

    @Override
    public void cancel() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }
}
