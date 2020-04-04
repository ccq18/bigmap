package bigmap.service;


import lombok.Data;

@Data
public class DataItem {
    private Integer at;
    private Integer len;
    private Integer realLen;

    static DataItem of(Integer at, Integer len) {
        DataItem print = new DataItem();
        print.at = at;
        print.len = len;
        print.realLen = len;
        return print;
    }
}