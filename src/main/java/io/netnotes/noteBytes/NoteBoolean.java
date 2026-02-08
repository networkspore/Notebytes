package io.netnotes.noteBytes;

import io.netnotes.noteBytes.processing.ByteDecoding;
import io.netnotes.noteBytes.processing.NoteBytesMetaData;

public class NoteBoolean extends NoteBytes {

    public final static NoteBytesReadOnly TRUE = new NoteBytesReadOnly(true);
    public final static NoteBytesReadOnly FALSE = new NoteBytesReadOnly(false);

    public NoteBoolean(boolean value){
        super(ByteDecoding.booleanToBytes(value), NoteBytesMetaData.BOOLEAN_TYPE);
    }

    public NoteBoolean(byte[] bytes){
        super(bytes, NoteBytesMetaData.BOOLEAN_TYPE);
    }

}
