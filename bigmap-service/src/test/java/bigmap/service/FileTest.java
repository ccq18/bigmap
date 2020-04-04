package bigmap.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class FileTest {
    public final static String FILE_PATH = "/Users/mac/code/bigmap/files/";



    @Test
    public void testDemo() throws IOException {
        File file2 = new File(FILE_PATH + "data.txt");

        FileUtils.writeStringToFile(file2, "hello", "utf-8", true);
        String rs = FileUtils.readFileToString(file2, "utf-8");
        log.info("{}", rs);
        String rs2 = FileUtils.readFileToString(file2, "utf-8");
        log.info("{}", FileUtils.sizeOf(file2));

        FileInputStream in = FileUtils.openInputStream(file2);
        byte[] bytes = new byte[10];

        in.read(bytes, 0, 5);
        in.read(bytes, 5, 5);
        log.info("{}", new String(bytes));
        in.skip(5);
        in.read(bytes);
        log.info("{}", new String(bytes));

        RandomAccessFile raf = new RandomAccessFile(file2, "r");
        raf.seek(10);
        raf.read(bytes);
        log.info("{}", new String(bytes));


    }



    /**
     * 分100次写入 随机移动指针
     * @throws IOException
     */
    @Test
    public void writefile1() throws IOException {
        File file = new File("/Users/mac/code/bigmap/files/data2.txt");
        FileUtils.deleteQuietly(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        for (int i = 0; i < 1000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            log.info("write start");

            raf.write( buf.toString().getBytes());
            raf.seek((int)(Math.random()*1000));
            log.info("write end");
        }
    }

    /**
     * 分100次写入
     * @throws IOException
     */
    @Test
    public void writefile2() throws IOException {
        File file = new File("/Users/mac/code/bigmap/files/data2.txt");
        FileUtils.deleteQuietly(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        for (int i = 0; i < 1000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            log.info("write start");
            raf.write( buf.toString().getBytes());
            log.info("write end");
        }
    }


    /**
     * 1次写入
     * @throws IOException
     */
    @Test
    public void writefile3() throws IOException {
        File file = new File("/Users/mac/code/bigmap/files/data2.txt");
        FileUtils.deleteQuietly(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }

        }
        log.info("write start");
        raf.write( buf.toString().getBytes());
        log.info("write end");
    }
    @Test
    public void cpfile() throws IOException {
        File file =  new File("/Users/mac/code/bigmap/files/data2.txt");
        File file2 =  new File("/Users/mac/code/bigmap/files/data3.txt");

        FileUtils.deleteQuietly(file2);
        FileUtils.copyFile(
                file,
                file2
        );
    }

    @Test
    public void testdemofile() {

        String key = AppConstant.get("log4j.logger.com.neusoft");
        log.info("success:{}", key);
    }
}
