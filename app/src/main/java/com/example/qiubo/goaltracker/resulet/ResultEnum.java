package com.example.qiubo.goaltracker.resulet;


/**
 * Created by weijian on 2018/7/20.
 */
enum ResultEnum {
    SUCCESS("0000","success"),
    FAIL("1111","fail"),
    APPSTOP("1112","找不到相关应用/应用关闭"),
    ACCOUNT_EXIST("0001","账号存在"),
    ACCOUNT_LOCK("0002","账号已锁定"),
    ACCOUNT_BINDED("0003","账号已绑定"),
    TIMEOUT("0004","您未登录或长时间未操作，为了您的账户安全，请重新登录"),
    PASSWORD_NOT_FIT("0005","app密码和官网密码不一致"),
    KEYEXPIRE("0006","SYSKEY过期"),
    ACCOUNT_NOT_EXIST("0007","账户不存在"),
    ACCOUNT_REGERROR("0008","账号格式有误，请检查"),
    ACCOUNT_DC_EXIST("0011","该用户是代销用户"),
    CHECK_PASSWD_NOT_ACCEPT("0009","您的密码过于简单"),
    QUESTIONANSWER_FORMAT_ERROR("0010","问卷入参格式错误"),
    TRADEPASSWORDERROR("TPE","交易密码错误"),
    KKFAIL("KF","申购请求成功但扣款失败"),
    KKORDERSUCCESS("KOS","扣款指令已发出，需要刷新交易记录查看结果"),
    MISSRISK("2784","开户必须先做风险评测"),
    DCNOMSG("2222","数据中心数据返回为空"),
    TRADE_LEGAL("TPAE","交易请求参数有误，请检查"),
    AUTHCODEERR("0012","验证码错误，请重新输入"),
    BANKTIMEOUT("0013","请求长时间未处理，请重新鉴权"),
    BANK_VARI_FALL("0014","银行卡鉴权失败，请重新鉴权"),
    PASSWORD_ERR("0015","交易密码错误"),
    BIND_FAIL("0016","绑定失败"),
    HADOPENACCO("0017","已开户"),
    HANDLE_FAIL("0018","操作失败，请重试或联系管理员"),
    INVITECODE_NOT_EXIST("0019","预约码不存在"),
    INVITECODE_USED("0020","已使用，请换一个预约码重试"),
    OCR_FAIL("0021","OCR调用失败"),
    ANXINSIGN_FAIL_3001("3001","安心签开户调用失败"),
    ANXINSIGN_FAIL_3011("3011","安心签增加印章调用失败"),
    ANXINSIGN_ACCO_NULL("0022","未签署电子签名约定书"),
    NOTSUPERUSER("0023","权限不够"),
    USERNAMEEXIST("0024","用户名已存在"),
    MIDUSERNULL("0025","账户不存在或密码错误"),
    FILENULL("0026","资料不能为空，请上传"),
    CAPITALTIMEOUT("0027","鉴权超时，请重新获取验证码"),
    OPENTRADEACCQUERYERRO("0028","绑卡失败，请重新登录"),
    ;

    private String code;
    private String msg;


    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
