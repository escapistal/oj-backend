package com.xc.oj.response;

public enum responseCode {
    SUCCESS(0,"成功"),
    FAIL(1,"失败"),

    FORBIDDEN(403,"权限不足"),

    USERNAME_INVALID(10001,"用户名不合法"),
    USERNAME_EXISTS(10002,"用户名已存在"),
    PASSWORD_INVALID(10003,"密码不合法"),
    EMAIL_INVALID(10004,"邮箱格式不合法"),
    EMAIL_EXISTS(10004,"该邮箱已注册"),
    USER_BLOCKED(10005,"该账户已被封禁,有疑问请联系管理员"),
    LOGIN_FAIL(10006,"登录失败"),
    USER_NOT_EXIST(10007,"用户不存在"),

    PROBLEM_NOT_EXIST(20001,"题目不存在"),

    NOT_ZIP_EXTENSION(30001,"非zip文件"),
    UNMATCHED_FILE(30003,"存在失配的输入与输出文件"),
    READ_FILE_ERROR(30004,"文件读取失败"),
    FTP_UPLOAD_ERROR(30005,"FTP上传失败，请稍后再试，或联系管理员"),

    CONTEST_NOT_EXIST(40001,"比赛不存在"),

    CONTEST_PROBLEM_NOT_EXIST(50001,"赛题不存在"),

    CONTEST_ANNOUNCEMENT_NOT_EXIST(60001,"比赛公告不存在"),

    ANNOUNCEMENT_NOT_EXIST(70001,"公告不存在")
    ;
    private int code;
    private String msg;

    responseCode() {
    }

    responseCode(int code, String msg) {

        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
