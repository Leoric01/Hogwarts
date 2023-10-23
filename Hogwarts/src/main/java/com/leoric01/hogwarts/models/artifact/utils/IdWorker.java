package com.leoric01.hogwarts.models.artifact.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/*
 This piece of code is from the Internet. The quality is unknown.
 Only for demoing purpose. Use with caution!
 */
public class IdWorker {

    private final static Long twepoch = 1288834974657L;

    private final static Long workerIdBits = 5L;

    private final static Long datacenterIdBits = 5L;

    private final static Long maxWorkerId = -1L ^ (-1L << workerIdBits);

    private final static Long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    private final static Long sequenceBits = 12L;

    private final static Long workerIdShift = sequenceBits;

    private final static Long datacenterIdShift = sequenceBits + workerIdBits;

    private final static Long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static Long sequenceMask = -1L ^ (-1L << sequenceBits);

    private static Long lastTimestamp = -1L;

    private Long sequence = 0L;

    private final Long workerId;

    private final Long datacenterId;


    public IdWorker() {
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }

    public IdWorker(Long workerId, Long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized Long nextId() {
        Long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        Long nextId = ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
        return nextId;
    }

    private Long tilNextMillis(final Long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private Long timeGen() {
        return System.currentTimeMillis();
    }

    protected static Long getMaxWorkerId(Long datacenterId, Long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            mpid.append(name.split("@")[0]); // GET jvmPid
        }
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    protected static Long getDatacenterId(Long maxDatacenterId) {
        Long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }

}
