package hello.notice.noticemain.memo;

import java.time.LocalDate;

public class Memo {

    // 회원가입 정보
    private Long id;
    private String userid;
    private String password;
    private String name;
    private String phonenumber;
    private String email;

    // 게시판 구성 정보
    private int numbercount;
    private String title;
    private String writername;
    private LocalDate writedate;
    private int viewcount;
    private String contents;
    private String writerpassword;
    private String checkwriterpassword;

    private String keyword;

    public Memo()
    {

    }

    public Memo(String userid, String password, String name, String phonenumber, String email) {
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumbercount() {
        return numbercount;
    }

    public void setNumbercount(int numbercount) {
        this.numbercount = numbercount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    public LocalDate getWritedate() {
        return writedate;
    }

    public void setWritedate(LocalDate writedate) {
        this.writedate = writedate;
    }

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWriterpassword() {
        return writerpassword;
    }

    public void setWriterpassword(String writerpassword) {
        this.writerpassword = writerpassword;
    }

    public String getCheckwriterpassword() {
        return checkwriterpassword;
    }

    public void setCheckwriterpassword(String checkwriterpassword) {
        this.checkwriterpassword = checkwriterpassword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", numbercount=" + numbercount +
                ", title='" + title + '\'' +
                ", writername='" + writername + '\'' +
                ", writedate=" + writedate +
                ", viewcount=" + viewcount +
                ", contents='" + contents + '\'' +
                ", writerpassword='" + writerpassword + '\'' +
                ", checkwriterpassword='" + checkwriterpassword + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
