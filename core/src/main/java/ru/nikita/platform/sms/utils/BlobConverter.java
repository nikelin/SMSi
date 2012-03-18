package ru.nikita.platform.sms.utils;

import java.sql.SQLException;
import java.util.Vector;
import java.sql.Blob;

public final class BlobConverter {

    public static byte[] convert(Blob blob) throws SQLException {
        if (blob == null) {
            throw new IllegalArgumentException("<null>");
        }

        return blob.getBytes(1, (int) blob.length() );
    }
}
