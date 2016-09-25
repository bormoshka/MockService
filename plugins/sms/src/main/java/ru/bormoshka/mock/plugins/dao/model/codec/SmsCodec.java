package ru.bormoshka.mock.plugins.dao.model.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import ru.bormoshka.mock.plugins.dao.model.SmsModel;

public class SmsCodec implements Codec<SmsModel> {
    public static final String COLLECTION_NAME = "mock_sms";
    public static final String MSID = "msid";
    public static final String PHONE = "phn";
    public static final String TEXT = "txt";
    public static final String STATUS = "s";
    public static final String DATE = "d";
    public static final String INFO = "i";
    public static final String TOUCH_COUNT = "tc";

    private CodecRegistry codecRegistry;

    public SmsCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public SmsModel decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId();
        SmsModel model = new SmsModel(reader.readString(MSID),
                reader.readString(PHONE), reader.readString(TEXT), reader.readString(STATUS),
                reader.readString(DATE), reader.readString(INFO), reader.readInt32(TOUCH_COUNT));
        reader.readEndDocument();
        return model;
    }

    @Override
    public void encode(BsonWriter writer, SmsModel sms, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName(MSID);
        writer.writeString(sms.getMsid());
        writer.writeName(PHONE);
        writer.writeString(sms.getPhone());
        writer.writeName(TEXT);
        writer.writeString(sms.getText());
        writer.writeName(STATUS);
        writer.writeString(sms.getStatus());
        writer.writeName(DATE);
        writer.writeString(sms.getDate());
        writer.writeName(INFO);
        writer.writeString(sms.getInfo());
        writer.writeName(TOUCH_COUNT);
        writer.writeInt32(sms.getTouchCount());
        writer.writeEndDocument();
    }

    @Override
    public Class<SmsModel> getEncoderClass() {
        return SmsModel.class;
    }
}
