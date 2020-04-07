package bigmap.service;


import bigmap.service.file.ProtostuffUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;

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
     *
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
            log.info("write pos");

            raf.write(buf.toString().getBytes());
            raf.seek((int) (Math.random() * 1000));
            log.info("write end");
        }
    }

    /**
     * 分100次写入
     *
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
            log.info("write pos");
            raf.write(buf.toString().getBytes());
            log.info("write end");
        }
    }


    /**
     * 1次写入
     *
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
        log.info("write pos");
        raf.write(buf.toString().getBytes());
        log.info("write end");
    }

    @Test
    public void cpfile() throws IOException {
        File file = new File("/Users/mac/code/bigmap/files/data2.txt");
        File file2 = new File("/Users/mac/code/bigmap/files/data3.txt");
        if (!file.exists()) {
            return;
        }
        FileUtils.deleteQuietly(file2);
        FileUtils.copyFile(
                file,
                file2
        );
    }

    static class NowWrite {
        Integer pos = 0;

        public static NowWrite read() throws IOException {
            File file = new File("/Users/mac/code/bigmap/files/testWrite.txt");
            if (!file.exists()) {
                return new NowWrite();
            }
            byte[] index = FileUtils.readFileToByteArray(file);
            return ProtostuffUtils.deserialize(index, NowWrite.class);
        }

        public void write() throws IOException {
            File file = new File("/Users/mac/code/bigmap/files/testWrite.txt");
            FileUtils.writeByteArrayToFile(file, ProtostuffUtils.serialize(this));

        }

    }

    public void wirte(InputStream inputStream) {

    }
//    public  read(){
//        return null;
//    }


    class ByteArrayInputStreamObj extends ByteArrayInputStream {

        public ByteArrayInputStreamObj(byte[] buf) {
            super(buf);
        }

        public ByteArrayInputStreamObj(byte[] buf, int offset, int length) {
            super(buf, offset, length);
        }

        @Override
        public synchronized int read(byte b[]) throws IOException {
            if (Math.random() > 0.7) {
                throw new IOException();
            }
            return super.read(b);
        }
    }

    @Test
    public void testWrite() throws IOException {
        NowWrite nowWrite = NowWrite.read();

        InputStream is;
        try {

            String testString = "1234567890abcdefghijklnmopqrstuvwxyz";

            is = new ByteArrayInputStreamObj(testString.getBytes());


            File file = new File("/Users/mac/code/bigmap/files/testWriteData.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            is.skip(nowWrite.pos);
            raf.seek(nowWrite.pos);
            byte[] b = new byte[5];
            int len;
            while ((len = is.read(b)) != -1) {
                raf.write(b,0,len);
                nowWrite.pos += len;
                log.info("{},len:{}", new String(b, 0, len, "UTF-8"), len);

            }
        } catch (IOException e) {
            log.error("len:{}",  nowWrite.pos);
            nowWrite.write();
            return;
        }
//            while ((temp = in.read()) != -1) { //当没有读取完时，继续读取
//                b[len] = (byte) temp;
//                len++;
//            } in .close();
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write();

        //            byte bWrite[] = { 11, 21, 3, 40, 5 };
//            OutputStream os = new FileOutputStream("test.txt");
//            for (int x = 0; x < bWrite.length; x++) {
//                os.write(bWrite[x]); // writes the bytes
//            }
//            os.close();
//
//            InputStream is = new FileInputStream("test.txt");
//            int size = is.available();
//
//            for (int i = 0; i < size; i++) {
//                System.out.print((char) is.read() + "  ");
//            }
//            is.close();


    }


    @Test
    public void testdemofile() {

        String key = AppConstant.get("log4j.logger.com.neusoft");
        log.info("success:{}", key);
    }
}
