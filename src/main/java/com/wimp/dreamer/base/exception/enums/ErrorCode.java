package com.wimp.dreamer.base.exception.enums;

/**
 * @author zy
 * @date 2020/9/3
 * <p>
 *  异常状态码枚举
 */
public enum ErrorCode {

    /**
     * Gl 99990100 error code enum.
     */
    GL99990100(9999100, "参数异常"),
    /**
     * Gl 99990401 error code enum.
     */
    GL99990401(99990401, "无访问权限"),
    /**
     * Gl 000500 error code enum.
     */
    GL99990500(500, "未知异常"),
    /**
     * Gl 000403 error code enum.
     */
    GL99990403(9999403, "无权访问"),
    /**
     * Gl 000404 error code enum.
     */
    GL9999404(9999404, "找不到指定资源"),
    /**
     * Gl 99990001 error code enum.
     */
    GL99990001(99990001, "注解使用错误"),
    /**
     * Gl 99990002 error code enum.
     */
    GL99990002(99990002, "微服务不在线,或者网络超时"),
    /**
     * Uac 10010001 error code enum.
     */
    //	 1001 用户中心
    UAC10010001(10010001, "会话超时,请刷新页面重试"),
    /**
     * Uac 10010002 error code enum.
     */
    UAC10010002(10010002, "TOKEN解析失败"),
    /**
     * Uac 10010003 error code enum.
     */
    UAC10010003(10010003, "操作频率过快, 您的帐号已被冻结"),
    /**
     * Uac 10011001 error code enum.
     */
    UAC10011001(10011001, "用户Id不能为空"),
    /**
     * Uac 10011002 error code enum.
     */
    UAC10011002(10011002, "找不到用户,loginName=%s"),
    /**
     * Uac 10011003 error code enum.
     */
    UAC10011003(10011003, "找不到用户,userId=%s"),
    /**
     * Uac 10011004 error code enum.
     */
    UAC10011004(10011004, "找不到用户,email=%s"),
    /**
     * Uac 10011006 error code enum.
     */
    UAC10011006(10012006, "手机号不能为空"),
    /**
     * Uac 10011007 error code enum.
     */
    UAC10011007(10011007, "登录名不能为空"),
    /**
     * Uac 10011008 error code enum.
     */
    UAC10011008(10011008, "新密码不能为空"),
    /**
     * Uac 10011009 error code enum.
     */
    UAC10011009(10011009, "确认密码不能为空"),
    /**
     * Uac 10011010 error code enum.
     */
    UAC10011010(10011010, "两次密码不一致"),
    /**
     * Uac 10011011 error code enum.
     */
    UAC10011011(10011011, "用户不存在, userId=%s"),
    /**
     * Uac 10011012 error code enum.
     */
    UAC10011012(10011012, "登录名已存在"),
    /**
     * Uac 10011013 error code enum.
     */
    UAC10011013(10011013, "手机号已存在"),
    /**
     * Uac 10011014 error code enum.
     */
    UAC10011014(10011014, "密码不能为空"),
    /**
     * Uac 10011016 error code enum.
     */
    UAC10011016(10011016, "用户名或密码错误"),
    /**
     * Uac 10011017 error code enum.
     */
    UAC10011017(10011017, "验证类型错误"),
    /**
     * Uac 10011018 error code enum.
     */
    UAC10011018(10011018, "邮箱不能为空"),
    /**
     * Uac 10011019 error code enum.
     */
    UAC10011019(10011019, "邮箱已存在"),

    /**
     * Opc 10040001 error code enum.
     */
    // 1004 对接中心
    OPC10040001(10040001, "根据IP定位失败"),
    /**
     * Opc 10040002 error code enum.
     */
    OPC10040002(10040002, "上传文件失败"),
    /**
     * Opc 10040003 error code enum.
     */
    OPC10040003(10040003, "文件类型不符"),
    /**
     * Opc 10040004 error code enum.
     */
    OPC10040004(10040004, "发送短信失败"),
    /**
     * Opc 10040005 error code enum.
     */
    OPC10040005(10040005, "生成邮件消息体失败"),
    /**
     * Opc 10040006 error code enum.
     */
    OPC10040006(10040006, "获取模板信息失败"),
    /**
     * Opc 10040007 error code enum.
     */
    OPC10040007(10040007, "更新附件失败, id=%s"),
    /**
     * Opc 10040008 error code enum.
     */
    OPC10040008(10040008, "找不到该附件信息, id=%s"),
    /**
     * Opc 10040009 error code enum.
     */
    OPC10040009(10040009, "上传图片失败"),
    /**
     * Tpc 10050001 error code enum.
     */
    OPC10040010(10040010, "文件名不能为空"),
    /**上传文件为空*/
    UPLOAD_NOT_EXIST(10040011,"上传文件为空"),
    /**文件格式不正确*/
    UPLOAD_FILE_FORMAT(10040012,"文件格式不正确"),

    /**
     * 文件超出限定大小
     */
    UPLOAD_FILE_OVERLARGE(10040013,"文件超出限定大小"),
    /**
     * 生成Excel工作簿出错
     */
    EXPORT_EXCEL(10040014,"生成Excel工作簿出错"),
    /**
     * 向Excel工作簿中追加数据出错
     */
    EXPORT_EXCEL_APPEND(10040015,"向Excel工作簿中追加数据出错"),
    /**
     * 读取Excel出错
     */
    READ_EXCEL(10040016,"读取Excel出错");

    private final int code;
    private final String msg;

    /**
     * Msg string.
     *
     * @return the string
     */
    public String msg() {
        return msg;
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public int code() {
        return code;
    }

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets enum.
     *
     * @param code the code
     *
     * @return the enum
     */
    public static ErrorCode getEnum(int code) {
        for (ErrorCode ele : ErrorCode.values()) {
            if (ele.code() == code) {
                return ele;
            }
        }
        return null;
    }
}
