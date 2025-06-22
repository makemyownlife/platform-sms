package cn.javayong.platform.sms.admin.common.sharding;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地缓冲 (每张表预留50个)
 */
public class LocalSequence {

    private static ConcurrentLinkedQueue<SequenceEntity> sequenceQueue = new ConcurrentLinkedQueue<SequenceEntity>();

    public static SequenceEntity getSeqEntity() {
        SequenceEntity seqEntity = sequenceQueue.poll();
        if (seqEntity == null) {
            return null;
        }
        return seqEntity;
    }

    public static void addSeqEntity(SequenceEntity seqEntity) {
        sequenceQueue.offer(seqEntity);
    }

    public static class SequenceEntity {

        private Long currentTime;

        private Integer seq;

        public SequenceEntity(Long currentTime, Integer seq) {
            this.currentTime = currentTime;
            this.seq = seq;
        }

        public Long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(Long currentTime) {
            this.currentTime = currentTime;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

    }

}
