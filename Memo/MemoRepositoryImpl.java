package hello.notice.noticemain.memo;

import hello.itemservice.domain.company.Company;
import hello.notice.noticemain.noticeconnection.DBConnectionUtility;
import hello.restaurant.user.Rating;
import hello.restaurant.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MemoRepositoryImpl implements MemoRepository {
    private final Map<Long, Memo> data = new HashMap<>();
    private Long id = 0L;
    private Long findid;
    private Long FindById;

    private int numbercount = 0;
    private int editcount = 0;

    private Long getNextId() {
        String sql = "SELECT MAX(id) FROM notice";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                Long maxId = resultSet.getLong(1);
                // 최대 ID 값에 1을 더한 값을 반환
                return maxId + 1;
            } else {
                // 테이블이 비어있을 경우, 1부터 시작
                return 1L;
            }
        } catch (SQLException e) {
            log.error("데이터베이스 오류입니다", e);
            return null;
        }
    }


    @Override
    public Memo save(Memo memo) {

        String CheckUserId = "select count(*) from notice where userid = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CheckUserId)) {

            preparedStatement.setString(1, memo.getUserid());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt(1);
                if (count > 0)
                {
                    System.out.println("이미 등록된 사용자 입니다.");
                    return null;
                }
            }

            String SaveSql = "insert into notice (id, userid, password, name, phonenumber, email) values (?, ?, ?, ?, ?, ?)";

            try (Connection connection1 = getConnection();
                 PreparedStatement preparedStatement1 = connection1.prepareStatement(SaveSql)) {

                Long nextId = getNextId();
                memo.setId(nextId);

                preparedStatement1.setLong(1, memo.getId());
                preparedStatement1.setString(2, memo.getUserid());
                preparedStatement1.setString(3, memo.getPassword());
                preparedStatement1.setString(4, memo.getName());
                preparedStatement1.setString(5, memo.getPhonenumber());
                preparedStatement1.setString(6, memo.getEmail());

                preparedStatement1.executeUpdate();

                System.out.println();
                System.out.println("회원가입이 성공적으로 완료되었습니다.");
                System.out.println();

            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류입니다" + e);
            }

        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류입니다" + e);
        }

        return memo;
    }


    @Override
    public Memo findById(Long id) {
        String findByIdSql = "select * from notice where id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByIdSql))
        {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    Memo memo = new Memo();
                    memo.setId(resultSet.getLong("id"));
                    memo.setUserid(resultSet.getString("userid"));
                    memo.setPassword(resultSet.getString("password"));
                    memo.setName(resultSet.getString("name"));
                    memo.setPhonenumber(resultSet.getString("phonenumber"));
                    memo.setEmail(resultSet.getString("email"));
                    memo.setNumbercount(resultSet.getInt("numbercount"));
                    memo.setTitle(resultSet.getString("title"));
                    memo.setWritername(resultSet.getString("writername"));
                    Date date = resultSet.getDate("writedate");

                    if (date != null)
                    {
                        memo.setWritedate(date.toLocalDate());
                    }
                    else
                    {
                        memo.setWritedate(null);
                    }

                    memo.setViewcount(resultSet.getInt("viewcount"));
                    memo.setContents(resultSet.getString("contents"));
                    memo.setWriterpassword(resultSet.getString("writerpassword"));

                    System.out.println();
                    System.out.println("찾은 id = " + memo.getId());
                    System.out.println();
                    return memo;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류입니다" + e);
        }

        return null; // 해당 id에 대한 Memo를 찾지 못한 경우 null을 반환
    }

    @Override
    public List<Memo> findAll() {
        List<Memo> memoList = new ArrayList<>();

        String findAllSql = "select * from notice";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllSql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {

            while (resultSet.next())
            {
                Memo memo = new Memo();
                memo.setId(resultSet.getLong("id"));
                memo.setUserid(resultSet.getString("userid"));
                memo.setPassword(resultSet.getString("password"));
                memo.setName(resultSet.getString("name"));
                memo.setPhonenumber(resultSet.getString("phonenumber"));
                memo.setEmail(resultSet.getString("email"));
                memo.setNumbercount(resultSet.getInt("numbercount"));
                memo.setTitle(resultSet.getString("title"));
                memo.setWritername(resultSet.getString("writername"));
                Date date = resultSet.getDate("writedate");
                if (date != null)
                {
                    memo.setWritedate(date.toLocalDate());
                }
                else
                {
                    memo.setWritedate(null);
                }

                memo.setViewcount(resultSet.getInt("viewcount"));
                memo.setContents(resultSet.getString("contents"));
                memo.setWriterpassword(resultSet.getString("writerpassword"));

                memoList.add(memo);
                log.info("가져온 사용자 수: {}", memoList.size());
                log.info("사용자: {}", memoList);
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
        }
        return memoList;

    }

    @Override
    public List<Memo> findBykeyword(String keyword) {
        // keyword 값이 매개변수로 들어오면 쿼리문을 통해
        // 저장되어 있는 테이블안에 컬럼명을 기준으로 해당 값을 찾는다
        // like 사용으로 인해 해당 keyword가 포함된 데이터라면 전부 찾을 수 있다
        // 그 후 Memo 객체 값을 List로 전환한 뒤 new 연산자로 빈 공간을 만들고
        // setString으로 찾고자 하는 값의 keyword 값을 입력한 뒤 resultSet으로 모든 데이터를 하나씩 검사한다
        // 검사 한 뒤 비어있는 memoList에 해당 값을 add 하고
        // 추가한 값을 다시 반환한다
        String findByWriterSql = "select * from notice where writername like ? or title like ?";

        List<Memo> memoList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByWriterSql))
        {
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    Memo memo = new Memo();

                    memo.setId(resultSet.getLong("id"));
                    memo.setUserid(resultSet.getString("userid"));
                    memo.setPassword(resultSet.getString("password"));
                    memo.setName(resultSet.getString("name"));
                    memo.setPhonenumber(resultSet.getString("phonenumber"));
                    memo.setEmail(resultSet.getString("email"));
                    memo.setNumbercount(resultSet.getInt("numbercount"));
                    memo.setTitle(resultSet.getString("title"));
                    memo.setWritername(resultSet.getString("writername"));
                    Date date = resultSet.getDate("writedate");
                    if (date != null)
                    {
                        memo.setWritedate(date.toLocalDate());
                    }
                    else
                    {
                        memo.setWritedate(null);
                    }
                    memo.setViewcount(resultSet.getInt("viewcount"));
                    memo.setContents(resultSet.getString("contents"));
                    memo.setWriterpassword(resultSet.getString("writerpassword"));

                    memoList.add(memo);
                    log.info("가져온 사용자 수: {}", memoList.size());
                    log.info("사용자: {}", memoList);
                }
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
        }
        System.out.println("찾은 keyword = " + keyword);
        System.out.println("memoList = " + memoList);
        return memoList;
    }

    @Override
    public Long login(String userid, String password) {

        List<Memo> memoList = findAll();

        for (Memo memo : memoList) {
            if (userid.equals(memo.getUserid()) && password.equals(memo.getPassword())) {
                findid = memo.getId();
                System.out.println();
                System.out.println("게시판 로그인한 id = " + findid);
                System.out.println();
                return findid;
            }
        }

        // db 연결 전 Map으로 key값 찾는 방식
        /*for(Map.Entry<Long, Memo> entry : data.entrySet())
        {
            Memo memo = entry.getValue();
            if (memo.getUserid().equals(userid) && memo.getPassword().equals(password))
            {
                findid = entry.getKey();
                System.out.println();
                System.out.println("게시판 로그인한 id = " + findid);
                System.out.println();
                return findid;
            }
        }*/
        return null;
    }

    @Override
    public void write(Long id, String title, String writer, String password, String contents) {

        String WriteSql = "update notice set title = ?, writername = ?, writerpassword = ?, contents = ?, writedate = ? where id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(WriteSql)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, writer);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, contents);
            preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
            preparedStatement.setLong(6, id);

            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("notice write result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        } catch (SQLException e) {
            System.out.println("데이터 오류입니다" + e);
        }

        System.out.println();
        System.out.println(data.toString());
        System.out.println();

    }

    @Override
    public void updateviewcount(Long id) {

        List<Memo> memoList = findAll();

        for (Memo memo : memoList) {
            if (memo.getId().equals(id)) {
                String ViewUpdateSql = "update notice set viewcount = ? where id = ?";

                try {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    connection = getConnection();
                    preparedStatement = connection.prepareStatement(ViewUpdateSql);

                    memo.setViewcount(memo.getViewcount() + 1);
                    preparedStatement.setInt(1, memo.getViewcount());
                    preparedStatement.setLong(2, memo.getId());

                    int resultSize = preparedStatement.executeUpdate();

                    System.out.println();
                    System.out.println("증가된 조회수 = " + memo.getViewcount());
                    log.info("notice viewcount result = {}", resultSize);
                    System.out.println();
                    // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
                } catch (SQLException e) {
                    System.out.println("데이터 오류 입니다" + e);
                }
            }
        }

        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void edit(Long id, String title, String writername, String writerpassword, String contents) {

        String EditSql = "update notice set title = ?, writername = ?, writerpassword = ?, contents = ?, writedate = ? where id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EditSql)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, writername);
            preparedStatement.setString(3, writerpassword);
            preparedStatement.setString(4, contents);
            preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
            preparedStatement.setLong(6, id);

            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("notice edit result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        } catch (SQLException e) {
            System.out.println("데이터 오류입니다" + e);
        }

        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void updatenumbercount(Long id) {

        List<Memo> memoList = findAll();

        for (Memo memo : memoList) {
            if (memo.getId().equals(id)) {
                String NumberCountSql = "update notice set numbercount = ? where id = ?";

                try {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    connection = getConnection();
                    preparedStatement = connection.prepareStatement(NumberCountSql);

                    numbercount++;
                    memo.setNumbercount(numbercount);

                    preparedStatement.setInt(1, memo.getNumbercount());
                    preparedStatement.setLong(2, memo.getId());

                    int resultSize = preparedStatement.executeUpdate();

                    System.out.println();
                    log.info("notice numbercount result = {}", resultSize);
                    System.out.println();
                    // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
                } catch (SQLException e) {
                    System.out.println("데이터 오류 입니다" + e);
                }
            }
        }

        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void delete(Long id) {

        List<Memo> memoList = findAll();

        for (Memo memo : memoList)
        {
            if (memo.getId().equals(id))
            {


                String DeleteSql = "update notice set numbercount = ?, title = ?, writername = ?, writedate = ?, viewcount = ?, contents = ?, writerpassword = ? where id = ?";

                try {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    connection = getConnection();
                    preparedStatement = connection.prepareStatement(DeleteSql);

                    memo.setNumbercount(0);
                    memo.setTitle(null);
                    memo.setWritername(null);
                    memo.setWritedate(null); // LocalDate의 경우 null로 설정
                    memo.setViewcount(0);
                    memo.setContents(null);
                    memo.setWriterpassword(null);

                    preparedStatement.setInt(1, 0);
                    preparedStatement.setString(2, null);
                    preparedStatement.setString(3, null);
                    preparedStatement.setString(4, null);
                    preparedStatement.setInt(5, 0);
                    preparedStatement.setString(6, null);
                    preparedStatement.setString(7, null);
                    preparedStatement.setLong(8, memo.getId());

                    int resultSize = preparedStatement.executeUpdate();

                    System.out.println();
                    log.info("notice delete result = {}", resultSize);
                    System.out.println();
                    // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
                }
                catch (SQLException e)
                {
                    System.out.println("데이터 오류 입니다" + e);
                }
            }
        }
    }



    @Override
    public String checkwriterpassword(Long id) {
        //Memo memo = data.get(id);
        Memo memo = findById(id);
        System.out.println();
        System.out.println("저장된 비밀번호 = " + memo.getWriterpassword());
        System.out.println();
        return memo.getWriterpassword();
    }

    @Override
    public void editpassword(Long id, String writerpassword) {
        //Memo memo = data.get(id);
        Memo memo = findById(id);
        memo.setCheckwriterpassword(writerpassword);
        System.out.println("새로 입력한 비밀번호 = " + memo.getCheckwriterpassword());
        // count로 체크하면 안됨
        // 다시 돌아왔을때 계속 null로 변경됨
    }

    @Override
    public int TotalNotice() {
        List<Memo> allMemos = findAll();
        System.out.println();
        System.out.println("게시판 총 사이즈 = " + allMemos.size());
        System.out.println();
        return allMemos.size();
    }


    private Connection getConnection()
    {
        return DBConnectionUtility.getConnection();
    }

}
