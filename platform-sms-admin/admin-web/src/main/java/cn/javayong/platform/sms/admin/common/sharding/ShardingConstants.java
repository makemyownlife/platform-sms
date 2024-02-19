package cn.javayong.platform.sms.admin.common.sharding;

public class ShardingConstants {

    //系统默认最大分区数
    public static final int SHARDING_LENGTH = 1024;

    //表分区列表
    private static final int PARTITION_LENGTH = ShardingConstants.SHARDING_LENGTH;

    // %转换为&操作的换算数值
    public static final long AND_VALUE = PARTITION_LENGTH - 1;

    public static final long STEP_LENGTH = 15;

    public static final int SEQ_EXPIRE_TIME = 120;

    public final static int MAX_SEQ = 4095;

    public final static String ID_REDIS_PFEFIX = "sharding:idGenerator:";

    // 主键
    public final static String DEFAULT_PRIMARY_KEY = "id";

    //组合键数目 默认为 1
    public final static int DEFAULT_SINGLE_COMBINE_KEY_LENGTH = 1;

}
