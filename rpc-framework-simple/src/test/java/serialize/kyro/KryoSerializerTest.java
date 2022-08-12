package serialize.kyro;

import remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;
import serialize.kryo.KryoSerializer;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

class KryoSerializerTest {

    @Test
    void kryoSerializerTest() {
        RpcRequest target = RpcRequest.builder().methodName("hello")
                .parameters(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("github.javaguide.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("version1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] bytes = kryoSerializer.serialize(target);
        RpcRequest actual = kryoSerializer.deserialize(bytes, RpcRequest.class);
        assertEquals(target.getGroup(), actual.getGroup());
        assertEquals(target.getVersion(), actual.getVersion());
        assertEquals(target.getRequestId(), actual.getRequestId());
    }
}