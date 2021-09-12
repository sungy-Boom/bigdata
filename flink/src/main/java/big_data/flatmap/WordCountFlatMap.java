package big_data.flatmap;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class WordCountFlatMap extends RichFlatMapFunction<String, Tuple2<String, Integer>> {

    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
        String[] arr = s.split(",");
        for (String s1 : arr) {
            s1 = s1.trim();
            String[] tmp = s1.split(" ");
            for (String s2 : tmp) {
                collector.collect(new Tuple2<>(s2, 1));
            }
        }
    }

    //RichFlatMapFunction相比FlatMapFunction多出来的方法
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
