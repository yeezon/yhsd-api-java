import com.youhaosuda.Auth;
import com.youhaosuda.Yhsd;
import com.youhaosuda.YhsdResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chenyg on 16/1/12.
 */
public class PrivateAuthTest {
    Auth auth;

    @Before
    public void init() {
        Yhsd yhsd = Yhsd.getInstance();
        auth = yhsd.auth("a41be9a8d7884a4784c6c003cd1fcaa1", "bc69aa7b511447c2b4da1de9a109217e", "http://yeeshopapp.ddns.net:63009/token");
    }


    @Test
    public void getAuthorizeUrl(){
        String url = auth.getAuthorizeUrl("a41be9a8d7884a4784c6c003cd1fcaa1");
        String result = "https%3A%2F%2Fapps.youhaosuda.com%2Foauth2%2Fauthorize%3Fresponse_type%3Dcode%26client_id%3Da41be9a8d7884a4784c6c003cd1fcaa1%26shop_key%3Da41be9a8d7884a4784c6c003cd1fcaa1%26redirect_uri%3Dhttp%3A%2F%2Fyeeshopapp.ddns.net%3A63009%2Ftoken";
        assertEquals(url,result);
    }

    @Test
    public void verifyHmac() throws UnsupportedEncodingException {
        String hmac = "c4784af7d21d7047c0b1f23a455ff663429ab6038652edd58f5a3932f7fe7a6a";
        String urlParam = "account_id=10831&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T07%3A53%3A31Z";
        urlParam = URLDecoder.decode(urlParam, "UTF-8");
        assertNotEquals(auth.verifyHmac(hmac,urlParam),true);
    }

    @Test
    public void getToken(){
        YhsdResponse token = auth.getToken();
        assertNotEquals(token,null);
    }
}
