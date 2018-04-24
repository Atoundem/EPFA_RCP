/*
 * Created on Jan 11, 2005
 *
 */
package org.icipe.epfa.classUtils;

import java.util.List;

/**
 * @author mschmitt
 *
 */
public class ArrayConvertions {

    public ArrayConvertions() {
    }

    public static String[] StringtoArray(String s, String sep) {
        // convert a String s to an Array, the elements
        // are delimited by sep
        StringBuffer buf = new StringBuffer(s);
        int arraysize = 1;
        for (int i = 0; i < buf.length(); i++) {
            if (sep.indexOf(buf.charAt(i)) != -1)
                arraysize++;
        }
        String[] elements = new String[arraysize];
        int y, z = 0;
        if (buf.toString().indexOf(sep) != -1) {
            while (buf.length() > 0) {
                if (buf.toString().indexOf(sep) != -1) {
                    y = buf.toString().indexOf(sep);
                    if (y != buf.toString().lastIndexOf(sep)) {
                        elements[z] = buf.toString().substring(0, y);
                        z++;
                        buf.delete(0, y + 1);
                    } else if (buf.toString().lastIndexOf(sep) == y) {
                        elements[z] = buf.toString().substring(0,
                                buf.toString().indexOf(sep));
                        z++;
                        buf.delete(0, buf.toString().indexOf(sep) + 1);
                        elements[z] = buf.toString();
                        z++;
                        buf.delete(0, buf.length());
                    }
                }
            }
        } else {
            elements[0] = buf.toString();
        }
        buf = null;
        return elements;
    }

    public static String ArrayToString(String s[], String sep) {
        int k;
        String result = "";

        k = s.length;
        if (k > 0) {
            result = s[0];
            for (int i = 1; i < k; i++) {
                result += sep + s[i];
            }
        }
        return result;
    }
    public static String ArrayToString(List<String> s, String sep) {
        int k;
        String result = "";

        k = s.size();
        if (k > 0) {
            result = s.get(0);
            for (int i = 1; i < k; i++) {
                result += sep + s.get(i);
            }
        }
        return result;
    }
    
    public static String ArrayToString(Object s[], String sep) {
        int k;
        String result = "";

        k = s.length;
        if (k > 0) {
            result = (String) s[0];
            for (int i = 1; i < k; i++) {
                result += sep + s[i];
            }
        }
        return result;
    }
}