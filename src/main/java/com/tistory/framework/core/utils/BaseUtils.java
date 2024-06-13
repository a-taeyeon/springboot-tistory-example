package com.tistory.framework.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Framework Base Utils
 */
@Slf4j
public class BaseUtils {

    /**
     * Exception.printStackTrace() 를 String 형태로 변환해 주는 함수
     * @param exception
     * @return
     */
    public static String getPrintStackTrace(Exception exception) {
        StringWriter errors = new StringWriter();
        exception.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    // Param 에 Null 이 들어와야 하는데, 빈 값이 들어와서 Mapper에서 잘못된
    // Query 를 만들어 내는 상황이 생겨서, 빈 값이면, Null 처리 할 수 있도록 삭제.
    @SuppressWarnings("unused")
    private void removeEmptyElements(Map<String, Object> params, String keys) {
        String[] elements = keys.split(",");

        for(String element : elements) {
            String key = element.trim();
            Object value = params.get(key);
            if (value == null) {
                params.remove(key);
            } else if (value instanceof List && ((List<?>)(value)).isEmpty()) {
                params.remove(key);
            }
        }
    }

    /**
     * IP4 주소를 가져오는 메소드
     * @return
     * @throws SocketException
     */
    public static String getIP4() throws SocketException {

        String ip4 = "";
        Enumeration<NetworkInterface> nienum = NetworkInterface.getNetworkInterfaces();

        while (nienum.hasMoreElements()) {

            NetworkInterface ni = nienum.nextElement();

            Enumeration<InetAddress> kk= ni.getInetAddresses();

            while (kk.hasMoreElements()) {

                InetAddress inetAddress = kk.nextElement();

                if (!inetAddress.isLoopbackAddress() &&

                        !inetAddress.isLinkLocalAddress() &&

                        inetAddress.isSiteLocalAddress()) {

                    ip4 = inetAddress.getHostAddress().toString();
                }
            }
        }
        return ip4;
    }




    /**
     *  문자를 바이트길이로 계산하여 자른다.
     * EUC-KR 인코딩의 이유는 한글을 2바이트로 취급하기 위함이다.
     **/
    public static String cropStringByByteLength(String str, int byteLength)  {
        try {
            if(str.getBytes("EUC-KR").length > byteLength){
                StringBuilder stringBuilder = new StringBuilder(byteLength);
                int nCnt = 0;
                for(char ch: str.toCharArray()){
                    nCnt += String.valueOf(ch).getBytes("EUC-KR").length;

                    if(nCnt > byteLength) break;

                    stringBuilder.append(ch);
                }
                return stringBuilder.toString();
            }else{
                return str;
            }
        } catch (Exception e) {
            return "";
        }
    }


}
