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
 * Created by chenyg on 15/11/11.
 */
public class ApiTest {
    Api api;

    @Before
    public void initApi() {
        api = Yhsd.getInstance().api("fbc7f83524f14e358a325a5066acd741");
    }

    @Test
    public void getApiInstance() {
        assertNotEquals(api, null);
    }


    @Test
    public void privateHttpGet() throws IOException {

        YhsdResponse yhsdResponse = api.get("customers");
        assertEquals(yhsdResponse.getStatusCode(), 200);

    }

    @Test
    public void privateHttpPostAndDelete() {
        String name = System.currentTimeMillis() + "";
        String body = "{\"meta\":{\"name\":\"" + name + "\",\"owner_id\":\"0\",\"owner_resource\":\"shop\",\"fields\":{\"test\":\"test\"},\"descriptions\":\"test\"}}";
        YhsdResponse yhsdResponse = api.post("metas", body);
        int idIndex = yhsdResponse.getBody().indexOf("id");
        String id =yhsdResponse.getBody().substring(idIndex,idIndex + 10).replaceAll("[^0-9]","");
        YhsdResponse delete = api.delete("metas/" + id);
        assertEquals(delete.getStatusCode(), 200);
        assertEquals(yhsdResponse.getStatusCode(), 200);
        //{"customer":{"addresses":[],"avatar":null,"created_at":"2015-11-12T18:09:47.777+08:00","id":239,"last_in":"2015-11-12T18:09:47.777+08:00","name":"for","notify_email":"for@example.com","notify_phone":"13632269380","reg_identity":"for@example.com","reg_type":"email","updated_at":"2015-11-12T18:09:47.777+08:00"}}
    }

    @Test
    public void httpPut() {
        String body = "{\"theme\":{\"name\":\"测试主题\"}}";
        YhsdResponse yhsdResponse = api.put("themes/13576", body);
        assertEquals(yhsdResponse.getStatusCode(), 200);
    }
}
