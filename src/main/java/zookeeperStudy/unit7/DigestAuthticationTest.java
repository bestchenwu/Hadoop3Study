package zookeeperStudy.unit7;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

/**
 * 测试acl加密方式<br/>
 * zookeeper会对权限表示先执行SHA-1,再进行BASE64编码
 */
public class DigestAuthticationTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //等价于在linux里执行echo -n foo:sweet | openssl dgst -binary -sha1 | openssl base64
        System.out.println(DigestAuthenticationProvider.generateDigest("super:admin"));
        //输出eV1N7OcGTqvmNBKvufDpDNbztq4=
    }
}
