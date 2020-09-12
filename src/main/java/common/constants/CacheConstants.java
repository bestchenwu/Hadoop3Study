package common.constants;

/**
 * 缓存常量类
 *
 * @author chenwu on 2017.6.28
 */
public class CacheConstants {

    public static final String DEFAULT_EMPTY_STRING = "";// 空白字符串
    public static final String CACHE_LINE_THROUGH_SEPERATE_CHAR = "|";// 中划线分隔符
    public static final String CACHE_UNDER_LINE_SEPERATE_CHAR = "_";// 缓存key的下划线分隔符
    public static final String CACHE_UNDER_LINE_SEPERATE_COMMA = ",";// 缓存key的逗号分隔符
    public static final String CACHE_KEY_SUFFIX = "key";// 缓存key的统一后缀
    public static final String NULL_OBJECT = "N/A"; // 存放一个简短的字符串对象，表示当DB里没数据时候的缩写
    // --------缓存时间常量 <开始>-------------//
    public static final int ExpirMins_5 = 5; // 5分钟
    public static final long ExpirMins_5_millSeconds = ExpirMins_5 * 60 * 1000;//5分钟的毫秒数
    public static final int ExpirMins_10 = 10; // 10分钟
    public static final int ExpirMins_15 = 15; // 15分钟
    public static final int ExpirMins_30 = 30; // 30分钟
    public static final int ExpirMins_40 = 40; // 40分钟
    public static final int ExpirMins_60 = 60; // 60分钟
    public static final int ExpirMins_90 = 90; // 90分钟
    public static final int ExpirMins_120 = 120; // 120分钟
    public static final int ExpirMins_150 = 150; // 150分钟
    public static final int ExpirMins_180 = 180; // 180分钟
    public static final int ExpirMins_210 = 210; // 210分钟
    public static final int ExpirMins_240 = 240; // 240分钟
    public static final int ExpirMins_270 = 270; // 270分钟
    public static final int ExpirMins_300 = 300; // 300分钟
    public static final int ExpirSeconds_1_day = 24 * 60 * 60; //一天换算成秒数
    public static final int ExpirSeconds_30_hour = 30 * 60 * 60; //30小时换算成秒数
    public static final int ExpirSeconds_48_hour = 48 * 60 * 60; //30小时换算成秒数
}
