package net.cnki.odatax.core;

/**
 * @author hudianwei
 * @date 2018/8/2 15:26
 */

import java.util.Date;

/**
 * 邮件
 */
public class Email {

    /**
     * 主题
     */
    private String subject = null;

    /**
     * 发送时间
     */
    private Date sentDate = new Date();

    /**
     * 邮件正文
     */
    private String content = null;

    /**
     * 收件人
     */
    private String[] to = null;

    /**
     * 抄送人
     */
    private String[] cc = null;

    /**
     * 暗抄送人
     */
    private String[] bcc = null;

    /**
     * 附件
     */
    private Attachment[] attachments = null;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getSentDate() {
        return this.sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTo() {
        return this.to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return this.cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public Attachment[] getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Attachment[] attachments) {
        this.attachments = attachments;
    }

}