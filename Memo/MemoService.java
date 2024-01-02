package hello.notice.noticemain.memo;


import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;



public interface MemoService {
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
