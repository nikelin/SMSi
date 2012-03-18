package ru.nikita.platform.sms.utils;

import ru.nikita.platform.sms.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ShortMessageUtils {

    private static String toBinary(byte b) {
        String bin = Integer.toBinaryString(b);
        int bl = bin.length();
        if (bl > 7) {
            bin = bin.substring(bin.length() - 7);
        } else if (bl < 7) {
            for (int j = 0; j < 7 - bl; j++) {
                bin = "0" + bin;
            }
        }
        return bin;
    }

    public static byte[][] split(Message message) {
        return split(message, false);
    }

    public static byte[][] split(Message message, int setSize) {
        return split(message, false, setSize);
    }

    public static byte[][] split(Message message, boolean to8bit) {
        return split(message, to8bit, -1);
    }

    public static byte[][] split(Message message, boolean to8bit, int setSize) {
        try {
            String content = message.getContent();
            byte[] cbytes = message.getLang().equals("RUS") ? content.getBytes("UTF-16") : content.getBytes();
            byte[] temp = new byte[cbytes.length - 2];
            if (message.getLang().equals("RUS")) {
                System.arraycopy(cbytes, 2, temp, 0, temp.length);
                cbytes = temp;
            }
            int maxLength = message.getLang().equals("RUS") ? 134 : 153;
            if (setSize > -1) {
                maxLength = setSize;
            }
            int fNum = cbytes.length / maxLength + 1;
            if (cbytes.length % maxLength == 0) {
                fNum--;
            }
            byte[][] ret = new byte[fNum][0];
            if (fNum > 1) {
                byte mid = (byte) (Math.random() * 255);
                for (int i = 0; i < fNum; i++) {
                    int v = cbytes.length - i * maxLength;
                    int arrSize = v > maxLength ? maxLength : v;
                    byte[] newb = new byte[arrSize + 6];

                    if (message.getLang().equals("RUS")) {
                        newb[0] = 5;
                        newb[1] = 0;
                        newb[2] = 3;
                        newb[3] = mid;
                        newb[4] = (byte) fNum;
                        newb[5] = (byte) (i + 1);
                        System.arraycopy(cbytes, i * maxLength, newb, 6, arrSize);
                    } else if (!to8bit) {
                        byte[] t = new byte[arrSize];
                        System.arraycopy(cbytes, i * maxLength, t, 0, arrSize);
                        byte[] newt = ShortMessageUtils.to7bit(t, i == fNum - 1);
                        newb = new byte[newt.length + 6];
                        newb[0] = 5;
                        newb[1] = 0;
                        newb[2] = 3;
                        newb[3] = mid;
                        newb[4] = (byte) fNum;
                        newb[5] = (byte) (i + 1);
                        System.arraycopy(newt, 0, newb, 6, newt.length);
                    } else {
                        newb[0] = 5;
                        newb[1] = 0;
                        newb[2] = 3;
                        newb[3] = mid;
                        newb[4] = (byte) fNum;
                        newb[5] = (byte) (i + 1);
                        System.arraycopy(cbytes, i * maxLength, newb, 6, arrSize);
                    }
                    ret[i] = newb;
                }
            } else {
                ret[0] = cbytes;
            }
            return ret;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] to7bit(byte[] b, boolean last) {
        int news = b.length - b.length / 8;
        if (last && news < 134) {
            news++;
        }
        byte[] ret = new byte[news];

        int ind = 0;
        int rbits = 7;
        String prev = toBinary(b[0]);
        for (int i = 0; i < b.length; i++) {
            String bin = toBinary(b[i]);
            String rbs = bin.substring(bin.length() - rbits);
            if (prev.length() != 0) {
                byte newb = (byte) Integer.parseInt(rbs + prev, 2);
                ret[ind++] = newb;
                prev = bin.substring(0, bin.length() - rbits);
                rbits++;
                if (rbits == 8) {
                    rbits = 1;
                }
            } else {
                prev = bin;
                rbits = 1;
            }
        }
        if (prev.length() > 0 && last && ind < 134) {
            byte newb = (byte) Integer.parseInt(prev, 2);
            ret[ind++] = newb;
        }
        ret[0] = (byte) (b[0] << 1);
        return ret;
    }

    public static List<String> splitByLength(String content, int maxLength) {
        List<String> result = new ArrayList<String>();
        if (content.length() <= maxLength) {
            result.add(content);
            return result;
        }

        StringTokenizer st = new StringTokenizer(content, "");
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String tmpStr = st.nextToken();
            if (!tmpStr.equals("\n")) {
                if ((sb.length() + tmpStr.length() + 3) > maxLength) {
                    result.add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(tmpStr + "");
                } else {
                    sb.append(tmpStr + "");
                }
            }

            if (!st.hasMoreTokens()) {
                result.add(sb.toString());
            }
        }

        return result;
    }
    
}
