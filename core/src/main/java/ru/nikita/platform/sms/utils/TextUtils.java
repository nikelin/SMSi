package ru.nikita.platform.sms.utils;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import java.util.*;

public class TextUtils
{

    /** ���� ��������� ���� @P@R@I@V@E@T@ @1@8 - ���� ��� ���� ������ */
    public static String removeRepeating(String source, char target) {
        CharBuffer result = CharBuffer.allocate( source.length() );

        boolean found = true;
        for (int i = 0; i < source.length(); i++)
        {
            if (found ) {
                if ( source.charAt(i) != '@' ) {
                    return source;
                }
            } else {
                result.append( source.charAt(i) );
            }

            found = !found;
        }

        return result.toString();
    }

    public static String deUnicode(String inStr, boolean skipHeader)
    {
        String rv = inStr;
        int MAX_DATA_HEADER_LEN = 6;
        try {
            byte[] bytes = inStr.getBytes();
            StringBuffer sb = new StringBuffer();
            int ich = 0;
            int startIndex = 0;
            if (skipHeader)
            {
                if (bytes.length > MAX_DATA_HEADER_LEN)
                {
                    int hLen = bytes[0];
                    if (hLen==5 || hLen==6)
                    {
                        startIndex = hLen + 1;
                        sb.append(new String(bytes, 0, startIndex));
                    }
                }
            }
            if (bytes.length > startIndex && bytes[startIndex] > 5)
            {
                return inStr;
            }
            boolean shift = false;
            for (int i=startIndex; i< bytes.length; i++)
            {
                ich = ich + bytes[i];
                if (!shift)
                {
                    ich = ich << 8;
                } else
                {
                    if (ich == 0x2029)
                        ich = 0x0a;
                    sb.append((char)ich);
                    ich = 0;
                }
                shift = !shift;
            }
            rv = sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return rv;
    }
}
