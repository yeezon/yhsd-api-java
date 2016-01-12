import com.youhaosuda.Api;
import com.youhaosuda.Yhsd;
import com.youhaosuda.YhsdResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chenyg on 16/1/12.
 * 公有应用测试
 */
public class PublicApiTest {
    Api api;
    @Before
    public void initApi() {
        api = Yhsd.getInstance().api("ae72e08a31b84701b274b82de3e4acd8");
    }

    @Test
    public void httpGet() throws IOException {
        YhsdResponse yhsdResponse = api.get("shop");
        assertEquals(yhsdResponse.getStatusCode(), 200);
    }

    @Test
    public void httpGetWithParam() throws IOException {
        Map param = new HashMap();
        param.put("fields","id,handle,images");
        YhsdResponse yhsdResponse = api.get("products",param);
        assertEquals(yhsdResponse.getStatusCode(), 200);
        Map<String, String> header = yhsdResponse.getHeader();
        assertNotEquals(header,null);
    }

    @Test
    public void httpPostAndDelete() throws IOException {
        String name = System.currentTimeMillis() + "";
        String body = "{\"meta\":{\"name\":\"" + name + "\",\"owner_id\":\"0\",\"owner_resource\":\"shop\",\"fields\":{\"test\":\"test\"},\"descriptions\":\"test\"}}";
        YhsdResponse yhsdResponse = api.post("metas", body);
        int idIndex = yhsdResponse.getBody().indexOf("id");
        String id =yhsdResponse.getBody().substring(idIndex,idIndex + 10).replaceAll("[^0-9]","");
        YhsdResponse delete = api.delete("metas/" + id);
        assertEquals(delete.getStatusCode(), 200);
        assertEquals(yhsdResponse.getStatusCode(), 200);
    }
    @Test
    public void httpPut(){
        //14078
        String body = "{\"theme\":{\"name\":\"测试主题\"}}";
        YhsdResponse yhsdResponse = api.put("themes/14078", body);
        assertEquals(yhsdResponse.getStatusCode(), 200);
    }
}
