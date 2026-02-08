package io.netnotes.noteBytes;

import io.netnotes.noteBytes.processing.ByteDecoding;
import io.netnotes.noteBytes.processing.NoteBytesMetaData;

public class NoteFloat extends NoteBytes{
    public NoteFloat(float _float){
        super(ByteDecoding.floatToBytesBigEndian(_float), NoteBytesMetaData.FLOAT_TYPE);
    }

    public NoteFloat(byte[] bytes){
        super(bytes, NoteBytesMetaData.FLOAT_TYPE);
    }
}
