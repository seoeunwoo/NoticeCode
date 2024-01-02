package hello.notice.noticemain.memo;

import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public class MemoServiceImpl implements MemoService{
    private MemoRepository memoRepository = new MemoRepositoryImpl();

    @Override
    public Memo save(Memo memo) {
        return memoRepository.save(memo);
    }

    @Override
    public Memo findById(Long id) {
        return memoRepository.findById(id);
    }

    @Override
    public List<Memo> findAll() {
        return memoRepository.findAll();
    }

    @Override
    public List<Memo> findBykeyword(String keyword) {
        return memoRepository.findBykeyword(keyword);
    }

    @Override
    public Long login(String userid, String password) {
        return memoRepository.login(userid, password);
    }

    @Override
    public void write(Long id, String title, String writer, String password, String contents) {
        memoRepository.write(id, title, writer, password, contents);
    }

    @Override
    public void updateviewcount(Long id) {
        memoRepository.updateviewcount(id);
    }

    @Override
    public void edit(Long id, String title, String writername, String writerpassword, String contents) {
        memoRepository.edit(id, title, writername, writerpassword, contents);
    }

    @Override
    public void updatenumbercount(Long id) {
        memoRepository.updatenumbercount(id);
    }

    @Override
    public void delete(Long id) {
        memoRepository.delete(id);
    }

    @Override
    public String checkwriterpassword(Long id) {
        return memoRepository.checkwriterpassword(id);
    }


    @Override
    public void editpassword(Long id, String writerpassword) {
        memoRepository.editpassword(id, writerpassword);
    }

    @Override
    public int TotalNotice() {
        return memoRepository.TotalNotice();
    }
}
