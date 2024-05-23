package io.easycrud.core.util.snowflake;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
@Component
public class Snowflake implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    // 起始的时间戳
    private final static long START_TIMESTAMP = 1716437990631L;

    // 每一部分占用的位数，符号位不算在内
    private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final static long MACHINE_BIT = 5; // 机器标识占用的位数
    private final static long DATACENTER_BIT = 5; // 数据中心占用的位数

    // 每一部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long dataCenterId; // 数据中心
    private long machineId; // 机器标识
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L;// 上一次时间戳

    @PostConstruct
    public void init() {
        machineId = generateMachineId();
        dataCenterId = generateDatacenterId();
        log.info("Snow flake parameters: Machine id: {}, Data center id: {}", machineId, dataCenterId);
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than MAX_MACHINE_NUM or less than 0");
        }
    }

    private long generateMachineId() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.info("Encounter UnknownHostException, set machine id to 0.");
            return 0L;
        }
        byte[] bytes = inetAddress.getAddress();
        long ip = 0;
        for (byte b : bytes) {
            ip = ip << 8 | (b & 0xFF);
        }
        return ip % MAX_MACHINE_NUM;
    }

    private long generateDatacenterId() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.info("Encounter UnknownHostException, set data center id to 0.");
            return 0L;
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                    byte[] bytes = inetAddress.getAddress();
                    long ip = 0;
                    for (byte b : bytes) {
                        ip = ip << 8 | (b & 0xFF);
                    }
                    return ip % MAX_DATACENTER_NUM;
                }
            }
        }
        throw new RuntimeException("Cannot get network interface, please set datacenterId manually");

    }

    public synchronized String nextId() {
        long currTimestamp = getTimestamp();
        if (currTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (currTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号已经达到最大值，下一个毫秒
            if (sequence == 0L) {
                currTimestamp = getNextTimestamp();
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = currTimestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        long id = (currTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT // 时间戳部分
                | dataCenterId << DATACENTER_LEFT // 数据中心部分
                | machineId << MACHINE_LEFT // 机器标识部分
                | sequence;
        return String.valueOf(id); // 序列号部分
    }

    private long getNextTimestamp() {
        long timestamp = getTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp();
        }
        return timestamp;
    }

    private long getTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Snowflake.applicationContext = applicationContext;
    }

    public static Snowflake getBean() {
        return applicationContext.getBean(Snowflake.class);
    }
}