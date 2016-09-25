package ru.bormoshka.mock.plugins.dao.model.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Component;
import ru.bormoshka.mock.plugins.dao.model.SmsModel;

@SuppressWarnings("unchecked")
@Component
public class SmsCodecProvider implements CodecProvider {
    /*
    public static SmsCodecProvider get() {
        return new SmsCodecProvider();
    }*/
    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == SmsModel.class) {
            return (Codec<T>) new SmsCodec(codecRegistry);
        }
        return null;
    }
}
