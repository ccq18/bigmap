package bigmap.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class Bigmap {
    private DataIndex di;
    private RandomAccessFile raf;
    private File file;

    public Bigmap() {
        log.info("init Bigmap");

        file = new File(AppConstant.get("data.path") + "data.txt");
    }

    //
    public void write(Map<String, String> map) throws IOException {
        FileUtils.deleteQuietly(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        DataIndex di = new DataIndex();
        map.forEach((k, v) -> {
            try {
                raf.write(v.getBytes());
                di.put(k, v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        raf.close();
        di.wirte();
    }

    // 1.查找缓存是否有符合条件的空位,有则直接写
// 2.向队尾写一个新的数据 .旧空间存入缓存
    public void writeKey(String k, String v) throws IOException {
        DataIndex di = DataIndex.read();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        writeKey(k, v, di, raf);
        raf.close();
        di.wirte();
    }

    public void init() throws IOException {
        log.info("init Bigmap:init");

        raf = new RandomAccessFile(file, "rw");
        di = DataIndex.read();
        raf.seek(di.getEndPointer());
    }

    public void close() throws IOException {
        raf.close();
        di.wirte();
    }


    public void writeKey(String k, String v, DataIndex di, RandomAccessFile raf) throws IOException {

        DataItem item = di.find(k, v);
        if (item == null) {
            raf.seek(di.getEndPointer());
            raf.write(v.getBytes());
            di.put(k, v);
        } else {
            raf.seek(item.getAt());
            raf.write(v.getBytes());
            di.use(k, v, item);
        }
    }

    public void writeKey2(String k, String v) throws IOException {
        DataIndex di = DataIndex.read();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        writeKey2(k, v, di, raf);
        raf.close();
        di.wirte();
    }


    public void writeKey2(String k, String v, DataIndex di, RandomAccessFile raf) throws IOException {
//        raf.seek(di.getEndPointer());
        raf.write(v.getBytes());
        di.put(k, v);

    }


    public String readKey(String key) throws IOException {
        DataIndex map2 = DataIndex.read();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        try {

            DataItem v = map2.get(key);
            raf.seek(v.getAt());
            byte[] bytes = new byte[v.getLen()];
            raf.read(bytes);

            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


//    public void init() throws IOException {
//        Map map = new HashMap<String, String>();
//        this.write(map);
//    }

}
