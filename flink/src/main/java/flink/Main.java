package flink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2021-09-09
 */
public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("flink/src/file/stock");
        InputStream streamt = new FileInputStream(file);
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(streamt));
        String line;

        while ((line = bufReader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
