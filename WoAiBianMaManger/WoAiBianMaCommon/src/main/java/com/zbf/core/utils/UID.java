package com.zbf.core.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UID {
	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 9999;

	public static synchronized long next() {
		if (seq > ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$ty%1$tm%1$td%1$tH%1$tM%1$tS%2$04d",date, seq++);

		return Long.parseLong(str);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			System.out.println(next());
		}
		System.out.println(System.currentTimeMillis());

		System.out.println(String.format("%1$04d",23));

        System.out.println (UID.getUUID16 ());

	}


	//获取32位的UUID
	public static String getUUID32(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

    //获取32位的UUID
    public static String getUUID16(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr.substring ( 0,16 );
    }

    //获取16位的UUID 的订单号
	public static String getUUIDOrder(){
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

	public static SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
}
