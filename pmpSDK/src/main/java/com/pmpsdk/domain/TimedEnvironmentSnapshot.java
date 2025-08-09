package com.pmpsdk.domain;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class TimedEnvironmentSnapshot {
    // TODO: 设为final，防止高并发旧对象覆盖新对象
    private final EnvironmentSnapshot environmentSnapshot;
    private final AtomicLong expireTime;

    public TimedEnvironmentSnapshot(EnvironmentSnapshot snapshot, long ttlMinutes) {
        this.environmentSnapshot = Objects.requireNonNull(snapshot);
        this.expireTime = new AtomicLong(
                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(ttlMinutes)
        );
    }

    /**
     * 重置过期时间
     * @param ttlMinutes
     */
    public void resetNewExpireTime(long ttlMinutes) {
        expireTime.set(
                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(ttlMinutes)
        );
    }

    /**
     * 判断是否过期
     * @return
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime.get();
    }

    /**
     * 获取快照
     * @return
     */
    public EnvironmentSnapshot getSnapshot() {
        return isExpired() ? null : environmentSnapshot;
    }
}


