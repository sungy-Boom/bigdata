package flink.util;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class Stock {
    public String symbol; //股票代码
    public String timestamp; //交易时间
    public String price; //价格
    public String amount; //交易数量

    //最大价格
    public String maxPrice;

    public Stock() {
    }

    public Stock(String symbol, String timestamp, String price, String amount) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.price = price;
        this.amount = amount;
    }

    public static Stock of(String symbol, String timestamp, String price, String amount) {
        return new Stock(symbol, timestamp, price, amount);
    }

    public String toString() {
        return "symbol:" + this.symbol + ", timestamp:" + this.timestamp
                + ", price:" + this.price + ", amount:" + this.amount;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
