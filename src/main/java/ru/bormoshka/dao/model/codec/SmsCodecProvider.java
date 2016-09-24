package ru.bormoshka.dao.model.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import ru.bormoshka.dao.model.SmsModel;

@SuppressWarnings("unchecked")
public class SmsCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == SmsModel.class) {
            return (Codec<T>) new SmsCodec(codecRegistry);
        }
        return null;
    }
}
