package bigmap.service;

import bigmap.service.file.ProtostuffUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

@Data
@Slf4j
public class DataIndex {
    private Integer endPointer;
    private Integer pointer;
    private HashMap<String, DataItem> dataItems;
    private File file;
    /**
     * 改用树
     */
    private TreeMap<Integer, LinkedList<DataItem>> caches;

    /**
     * 写入优化
     * //        顺序写入，不立即对空间进行回收
     * //        碎片整理 对碎片空间进行整理
     * //        索引优化，如何尽可能多的载入索引和数据到内存
     * //        利用机器学习统计优化管理数据和索引优化
     */
    public DataIndex() {
        log.info("init DataIndex");
         file = new File(AppConstant.get("data.path") + "data-index.json");
        pointer = 0;
        endPointer = 0;
        dataItems = new HashMap<>();
        caches = new TreeMap<>();

    }

    public void addCache(DataItem item) {
        LinkedList<DataItem> list = caches.get(item.getRealLen());
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(item);
        caches.put(item.getRealLen(), list);

    }

    public void removeCache(DataItem item) {
        LinkedList<DataItem> list = caches.get(item.getRealLen());
        if (list == null) {
            return;
        }
        list.remove(item);

        if (list.isEmpty()) {
            caches.remove(item.getRealLen());
        }
    }


    public DataItem find(String key, String data) {
        DataItem nowItem = dataItems.get(key);
        if (nowItem != null) {
            if (nowItem.getRealLen() >= data.length()) {
                return nowItem;
            }
        }

        Integer k = caches.higherKey(data.length());
        if (k == null) {
            return null;
        }
        LinkedList<DataItem> list = caches.get(k);

        return list.getFirst();
    }

    /**
     * 插入新数据，不会更新索引
     *
     * @param key
     * @param data
     */
    public void put(String key, String data) {
        DataItem item = dataItems.get(key);
        if (item != null) {
            addCache(item);
        }
        dataItems.put(key, DataItem.of(pointer, data.length()));
        pointer += data.length();
        endPointer = pointer;
    }

    public void use(String key, String data, DataItem item) {
        item.setLen(data.length());
        dataItems.put(key, item);
        removeCache(item);
    }

    public DataItem get(String key) {
        return dataItems.get(key);
    }

    public static DataIndex di;

    public static DataIndex read() throws IOException {
        if (DataIndex.di != null) {
            return DataIndex.di;
        }
        File file = new File(AppConstant.get("data.path") + "data-index.json");
        if(!file.exists()){
            DataIndex.di = new DataIndex();
            return DataIndex.di ;
        }
        byte[] index = FileUtils.readFileToByteArray(file);
        DataIndex.di = ProtostuffUtils.deserialize(index, DataIndex.class);

        return DataIndex.di ;
    }

    public void wirte() throws IOException {

        FileUtils.writeByteArrayToFile(file, ProtostuffUtils.serialize(this));
    }
}