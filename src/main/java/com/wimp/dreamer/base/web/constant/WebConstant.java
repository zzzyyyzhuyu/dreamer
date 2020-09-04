package com.wimp.dreamer.base.web.constant;

/**
 * @author zy
 * @date 2020/9/4
 * <p>
 *  web常量类
 */
public class WebConstant {
    public static final class OP {
        private OP() {
        }
        public static final String  LIKE = "like" ;
        public static final String LEFT_LIKE = "leftLike";
        public static final String RIGHT_LIKE = "rightLike";
        public static final String EQ = "eq";// 等于
        public static final String NE = "ne";// 不等于
        public static final String GT = "gt";// 大于
        public static final String NL = "nl";// 大于等于
        public static final String LT = "lt";// 小于
        public static final String NG = "ng";// 小于等于
        public static final String NULL = "null";// 值为 null
        public static final String NOTNULL = "notNull";// 值不为 null
        public static final String IN = "in";// in 操作
        public static final String NOT_IN = "notIn";// 不在区间 操作
        public static final String  BETWEEN = "between";// between 操作
        public static final String NOT_BETWEEN ="notBetween";// 不在区间 操作
        public static final String ORDER ="ody";// 排序

    }
}
