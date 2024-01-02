package hello.notice.noticemain.memo;

import org.springframework.data.domain.Page;

import java.util.List;

public interface MemoRepository {
    Memo save(Memo memo);
    Memo findById(Long id);
    List<Memo> findAll();
    List<Memo> findBykeyword(String keyword);
    Long login(String userid, String password);
    void write(Long id, String title, String writer, String password, String contents);
    void updateviewcount(Long id);
    void edit(Long id, String title, String writername, String writerpassword, String contents);
    void updatenumbercount(Long id);
    void delete(Long id);
    String checkwriterpassword(Long id);
    void editpassword(Long id, String writerpassword);
    int TotalNotice();
}
