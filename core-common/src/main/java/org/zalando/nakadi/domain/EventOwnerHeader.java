package org.zalando.nakadi.domain;


import com.google.common.base.Charsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;

/**
 * Class represents serializer / deserializer of event authentication header.
 */
public class EventOwnerHeader {

    public static final String AUTH_PARAM_NAME = "X-AuthParam-Name";
    public static final String AUTH_PARAM_VALUE = "X-AuthParam-Value";

    private final String name;
    private final String value;

    public EventOwnerHeader(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public void serialize(final ProducerRecord<String, String> record) {
        record.headers().add(AUTH_PARAM_NAME, name.getBytes(Charsets.UTF_8));
        record.headers().add(AUTH_PARAM_VALUE, value.getBytes(Charsets.UTF_8));
    }

    public static EventOwnerHeader deserialize(final ConsumerRecord<byte[], byte[]> record) {
        final Header nameHeader = record.headers().lastHeader(EventOwnerHeader.AUTH_PARAM_NAME);
        if (null == nameHeader) {
            return null;
        }
        final Header valueHeader = record.headers().lastHeader(EventOwnerHeader.AUTH_PARAM_VALUE);
        if (valueHeader == null) {
            return null;
        }

        return new EventOwnerHeader(
                new String(nameHeader.value(), Charsets.UTF_8),
                new String(valueHeader.value(), Charsets.UTF_8));
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
