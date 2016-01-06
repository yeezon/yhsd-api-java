import com.youhaosuda.Auth;
import com.youhaosuda.Yhsd;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chenyg on 15/11/11.
 */

public class AuthTest {
    Yhsd yhsd;
    Auth auth;
    @Before
    public void init(){
        yhsd = Yhsd.getInstance();
        auth = yhsd.auth("548e29e46091449e949a8e1ffe4e4167","b9fec3d128064ea89f1e9b8324eeabc5","http://192.168.1.71:3004/admin/token");
    }


    @Test
    public void verifyHmac() throws UnsupportedEncodingException {
        String hmac = "c4784af7d21d7047c0b1f23a455ff663429ab6038652edd58f5a3932f7fe7a6a";
        String urlParam = "account_id=10831&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T07%3A53%3A31Z";
        urlParam = URLDecoder.decode(urlParam, "UTF-8");
        assertNotEquals(auth.verifyHmac(hmac,urlParam),true);
    }

    @Test
    public void getAuthorizeUrl() throws UnsupportedEncodingException {
        //account_id=10831&code=e5251c4a14984fdca035ff833fa7a8f5&hmac=eb979e1aae41898e8a3e706347866431a28e06f87509a75129ed8637832fc7b5&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T08%3A42%3A55Z
        String urlParam = "account_id=10831&code=e5251c4a14984fdca035ff833fa7a8f5&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T08%3A42%3A55Z";
        String hmac = "eb979e1aae41898e8a3e706347866431a28e06f87509a75129ed8637832fc7b5";
        String shopKey = "28eb744fab8a0cd7d118dd0e20b7357e";
        urlParam = URLDecoder.decode(urlParam, "UTF-8");
        assertNotEquals(auth.verifyHmac(hmac,urlParam),true);
        String authorizeUrl = auth.getAuthorizeUrl(shopKey, "");
        assertNotEquals(authorizeUrl,null);
    }

    @Test
    public void privateGetToken() throws IOException {
        ///admin/token?account_id=10831&code=5e3a098aa4044a558c95377cc8c3534d&hmac=abbe925dd246373c67d8e54fe5925007c1631cd80795f980a599d3e40245df58&shop_key=28eb744fab8a0cd7d118dd0e20b7357e&time_stamp=2015-11-11T09%3A26%3A53Z
        String token = auth.getToken().getBody();
        assertNotEquals(token,null);
    }
}
