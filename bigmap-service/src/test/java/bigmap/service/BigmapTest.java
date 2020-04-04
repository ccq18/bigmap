package bigmap.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BigmapTest {

    private Bigmap bigmap;

    private Bigmap getObj() {
        if (bigmap == null) {
            bigmap = new Bigmap();
        }
        return bigmap;
    }

    @Test
    public void init() throws IOException {
        log.info("init");
        Map map = new HashMap<String, String>();
        this.getObj().write(map);
        log.info("init end");
    }


    @Test
    public void testDemo10() throws IOException {
        this.init();
        log.info("write");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            map.put("key" + i, buf.toString());
        }

        getObj().write(map);
        log.info("write end");
    }

    @Test
    public void testDemo11() throws IOException {
        this.init();
        log.info("writekey");
        getObj().init();

        for (int i = 0; i < 10000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            getObj().writeKey("key" + i, buf.toString(), getObj().getDi(), getObj().getRaf());
        }
        getObj().close();
        log.info("writekey end");


    }

    @Test
    public void testDemo12() throws IOException {
        this.init();
        log.info("writekey");

        for (int i = 0; i < 10000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            getObj().writeKey("key" + i, buf.toString());
        }

        log.info("writekey end");

    }

    @Test
    public void testDemo13() throws IOException {
        this.init();
        log.info("writekey2");
        getObj().init();

        for (int i = 0; i < 10000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            getObj().writeKey2("key" + i, buf.toString(), getObj().getDi(), getObj().getRaf());
        }
        getObj().close();
        log.info("writekey2 end");


    }

    @Test
    public void testDemo14() throws IOException {
        this.init();
        log.info("writekey2");

        for (int i = 0; i < 10000; i++) {
            StringBuffer buf = new StringBuffer();
            buf.append("hello");
            for (int i2 = 0; i2 < 3000; i2++) {
                buf.append(Math.random());
            }
            getObj().writeKey2("key" + i, buf.toString());
        }

        log.info("writekey2 end");

    }

    @Test
    public void testDemoR0() throws IOException {
        log.info("writekey");

        for (int i = 0; i < 1000; i++) {

            getObj().readKey("key" + i);
        }

        log.info("writekey end");


    }


    @Test
    public void testDemo2() throws IOException {
        log.info("r:{}", getObj().readKey("key520"));
    }

    @Test
    public void testDemo3() throws IOException {
        getObj().writeKey("key524", "1234");
        getObj().writeKey("key522", "123");
        log.info("r:{}", getObj().readKey("key524"));
    }

}
