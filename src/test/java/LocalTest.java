import com.youhaosuda.Api;
import com.youhaosuda.Auth;
import com.youhaosuda.Yhsd;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by chenyg on 16/1/12.
 * 本地环境下获取实例
 */
public class LocalTest {
    @Test
    public void getLocalApi(){
        Yhsd yhsd = Yhsd.getInstance("http://www.localtest.com","http://www.localtest.com","http");
        Api api = yhsd.api("123");
        Auth auth = yhsd.auth("123", "!23", "!23");
        assertNotEquals(yhsd,null);
        assertNotEquals(api,null);
        assertNotEquals(auth,null);
    }
}
