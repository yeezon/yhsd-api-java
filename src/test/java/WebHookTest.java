import com.youhaosuda.WebHook;
import com.youhaosuda.Yhsd;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Created by chenyg on 16/1/12.
 */
public class WebHookTest {
    WebHook webHook;
    @Before
    public void init(){
        webHook = Yhsd.getInstance().webHook("1234567");
    }

    @Test
    public void verifyHmac() throws InvalidKeyException, NoSuchAlgorithmException {
        assertEquals(webHook.verifyHmac("1234","1234"),false);
    }
}
