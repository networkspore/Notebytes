package io.netnotes.noteBytes;

import java.math.BigDecimal;

import io.netnotes.noteBytes.processing.ByteDecoding;
import io.netnotes.noteBytes.processing.NoteBytesMetaData;

public class NoteBigDecimal extends NoteBytes {
    public NoteBigDecimal(BigDecimal bigDecimal){
        super(ByteDecoding.bigDecimalToScaleAndBigInteger(bigDecimal),  NoteBytesMetaData.BIG_DECIMAL_TYPE);
    }

    public NoteBigDecimal(byte[] bytes){
        super(bytes, NoteBytesMetaData.BIG_DECIMAL_TYPE);
    }

}
