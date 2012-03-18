package ru.nikita.platform.sms.utils;

import org.apache.commons.collections.FastHashMap;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Transliterator
{

    private static final Map<Character, String> equityTable = new FastHashMap();
    static {
        equityTable.put('А', "A");
        equityTable.put('Б', "B");
        equityTable.put('В', "V");
        equityTable.put('Д', "D");
        equityTable.put('Е', "E");
        equityTable.put('Ё', "E");
        equityTable.put('Ж', "ZH");
        equityTable.put('З', "Z");
        equityTable.put('И', "I");
        equityTable.put('Й', "Y");
        equityTable.put('К', "K");
        equityTable.put('Л', "L");
        equityTable.put('М', "M");
        equityTable.put('Н', "G");
        equityTable.put('О', "O");
        equityTable.put('П', "P");
        equityTable.put('Р', "R");
        equityTable.put('С', "S");
        equityTable.put('Т', "T");
        equityTable.put('У', "U");
        equityTable.put('Ф', "F");
        equityTable.put('Х', "H");
        equityTable.put('Ц', "C");
        equityTable.put('Ч', "CH");
        equityTable.put('Ш', "SH");
        equityTable.put('Щ', "SCH");
        equityTable.put('Ь', "\'");
        equityTable.put('Ы', "Y");
        equityTable.put('Ъ', "\'");
        equityTable.put('Э', "E");
        equityTable.put('Ю', "YU");
        equityTable.put('Я', "YA");
    }

    /**  */
    public static String translateFromUnicode(String in)
    {
        try {
            byte[] bytes = in.getBytes();
            StringBuffer sb = new StringBuffer();
            int ich = 0;
            boolean shift = false;
            for (int i=0; i< bytes.length; i++)
            {
                ich = ich + bytes[i];
                if (!shift)
                {
                    ich = ich << 8;
                } else
                {
                    if (ich == 0x2029)
                        ich = 0x0a;
                    sb.append(convert((char)ich));
                    ich = 0;
                }
                shift = !shift;
            }
            in = sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return in;
    }

    /**  */
    public static String translate(String in)
    {
        StringBuilder sb = new StringBuilder();

        for ( char c : in.toCharArray() )
        {
            sb.append(convert(c));
        }

        return sb.toString();
    }

    private static String convert(char c)
    {
        String result = equityTable.get(c);
        if ( result == null ) {
            result = String.valueOf(c);
        }

        return result;
    }
}
