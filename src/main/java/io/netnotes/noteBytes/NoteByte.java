package io.netnotes.noteBytes;

import io.netnotes.noteBytes.processing.NoteBytesMetaData;

public class NoteByte extends NoteBytes {

    public static final NoteByte ZERO = new NoteByte((byte) 0);
    public static final NoteByte ONE = new NoteByte((byte) 1);

    public NoteByte(byte value){
        super(new byte[]{ value },  NoteBytesMetaData.BYTE_TYPE);
    }

}
