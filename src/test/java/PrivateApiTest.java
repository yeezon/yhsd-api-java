import com.youhaosuda.Api;
import com.youhaosuda.Yhsd;
import com.youhaosuda.YhsdResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by chenyg on 16/1/12.
 * 私有API测试
 */
public class PrivateApiTest {
    Api api;
    @Before
    public void initApi() throws IOException {
        //cbac23f353354de6b9675ae3cc4a2e13
        String token = Yhsd.getInstance().auth("a41be9a8d7884a4784c6c003cd1fcaa1","bc69aa7b511447c2b4da1de9a109217e","http:www.youhaosuda.com").getToken().getBody();
        api = Yhsd.getInstance().api(token.substring(10,token.length() - 2));
    }

    @Test
    public void httpGet() throws IOException {
        YhsdResponse yhsdResponse = api.get("shop");
        assertEquals(yhsdResponse.getStatusCode(), 200);
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
