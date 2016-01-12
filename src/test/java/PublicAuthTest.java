import com.youhaosuda.Auth;
import com.youhaosuda.Yhsd;
import com.youhaosuda.YhsdException;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chenyg on 16/1/12.
 */
public class PublicAuthTest {
    Auth auth;

    @Before
    public void init() throws YhsdException {
        Yhsd yhsd = Yhsd.getInstance();
        String[] appScope = {"read_basic", "write_basic", "read_content", "write_content", "read_themes", "write_themes",
                "read_products", "write_products", "read_customers", "write_customers",
                "read_orders", "write_orders", "read_script_tags", "write_script_tags"};
        auth = yhsd.auth("86cf6af5e51744b2a4327f0fe2936ada", "46154d9b0a684939b5f21fb22a8275b7", "http://yeeshopapp.ddns.net:63009/token", appScope);
    }

    @Test
    public void initWithNoException() throws YhsdException {
        Yhsd yhsd = Yhsd.getInstance();
        String[] appScope = {"read_basic", "write_basic", "read_content", "write_content", "read_themes", "write_themes",
                "read_products", "write_products", "read_customers", "write_customers",
                "read_orders", "write_orders", "read_script_tags", "write_script_tags"};
        Auth auth = yhsd.auth("86cf6af5e51744b2a4327f0fe2936ada", "46154d9b0a684939b5f21fb22a8275b7", "http://yeeshopapp.ddns.net:63009/token", appScope);
        assertNotEquals(auth,null);
    }

    @Test
    public void initWithException() {
        Yhsd yhsd = Yhsd.getInstance();
        String[] appScope = {};
        try {
            auth = yhsd.auth("86cf6af5e51744b2a4327f0fe2936ada", "46154d9b0a684939b5f21fb22a8275b7", "http://yeeshopapp.ddns.net:63009/token", appScope);
        } catch (YhsdException e) {
            assertEquals(e.getMessage(),"公有应用必须注明所需要的权限");
        }
    }

    @Test
    public void getAuthorizeUrl() {
        String url = auth.getAuthorizeUrl("548e29e46091449e949a8e1ffe4e4167", "123456");
        String result = "https%3A%2F%2Fapps.youhaosuda.com%2Foauth2%2Fauthorize%3Fresponse_type%3Dcode%26client_id%3D86cf6af5e51744b2a4327f0fe2936ada%26shop_key%3D548e29e46091449e949a8e1ffe4e4167%26scope%3Dread_basicread_basic%2Cwrite_basic%2Cread_content%2Cwrite_content%2Cread_themes%2Cwrite_themes%2Cread_products%2Cwrite_products%2Cread_customers%2Cwrite_customers%2Cread_orders%2Cwrite_orders%2Cread_script_tags%2Cwrite_script_tags%26state%3D123456%26redirect_uri%3Dhttp%3A%2F%2Fyeeshopapp.ddns.net%3A63009%2Ftoken";
        assertEquals(url, result);
    }

    @Test
    public void verifyHmac() throws UnsupportedEncodingException {
        String hmac = "c4784af7d21d7047c0b1f23a455ff663429ab6038652edd58f5a3932f7fe7a6a";
        String urlParam = "account_id=10831&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T07%3A53%3A31Z";
        urlParam = URLDecoder.decode(urlParam, "UTF-8");
        assertNotEquals(auth.verifyHmac(hmac, urlParam), true);
    }

    @Test
    public void getToken() {
        String token = auth.getToken("testtest").getBody();
        String responseBody = "{\"error\":\"invalid_client\"}";
        assertEquals(token, responseBody);
    }

    @Test
    public void thirdAppAesEncrypt() throws Exception {
        assertNotEquals(auth.thirdAppAesEncrypt("c4784af7d21d7047c0b1f23a455ff663429ab6038652edd58f5a3932f7fe7a6a","account_id=10831&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T07%3A53%3A31Z"), null);
    }
}
