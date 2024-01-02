package hello.notice.noticemain.memocontroller;

import hello.notice.noticemain.memo.Memo;
import hello.notice.noticemain.memo.MemoService;
import hello.notice.noticemain.memo.MemoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/notice")
public class MemoController {

    MemoService memoService = new MemoServiceImpl();

    private Long loginnoticeid;
    private String loginid;

    // 로그인, 로그아웃 화면
    @GetMapping("/main")
    public String NoticeMain(Model model)
    {
        //model.addAttribute("login", loginnoticeid);
        return "/memo/noticemain";
    }


    // 회원가입
    @GetMapping("/join")
    public String NoticeJoin()
    {
        return "/memo/noticejoin";
    }

    /*@PostMapping("/join")
    public String NoticeJoinComplete(@ModelAttribute Memo memo, Model model)
    {
        // save가 null 이면 다시 회원가입으로 return null이 아니면 main으로 return
        //memoService.save(memo);
        //model.addAttribute("memo", memo);
        Memo memo1 = memoService.save(memo);

        if (memo1 == null)
        {
            System.out.println("회원가입 실패");
            System.out.println("회원가입 실패" + memo1);
            model.addAttribute("memo", memo1);
            return "redirect:/notice/join";
        }
        else
        {
            System.out.println("회원가입 성공" + memo1);
            model.addAttribute("memo", memo1);
            System.out.println("회원가입 성공");
            return "redirect:/notice/main";
        }
    }*/
    /*@PostMapping("/join")
    @ResponseBody
    public Map<String, String> NoticeJoinComplete(@ModelAttribute Memo memo) {
        Map<String, String> response = new HashMap<>();

        Memo savedMemo = memoService.save(memo);

        if (savedMemo == null) {
            response.put("status", "fail");
            response.put("message", "이미 등록된 사용자입니다.");
        } else {
            response.put("status", "success");
            response.put("message", "회원가입이 성공적으로 완료되었습니다.");
        }

        return response;
    }*/
    @PostMapping("/join")
    @ResponseBody
    public String NoticeJoinComplete(@ModelAttribute Memo memo) {
        Memo memo1 = memoService.save(memo);

        if (memo1 == null)
        {
            return "회원가입 실패";
        }
        else
        {
            return "회원가입 성공";
        }
    }


    // 로그인
    @GetMapping("/login")
    public String NoticeLogin()
    {

        return "/memo/noticelogin";
    }

    /*@PostMapping("/login")
    public String NoticeLoginComplete(@RequestParam("userid") String userid,
                                      @RequestParam("password") String password, Model model, RedirectAttributes redirectAttributes)
    {
        loginnoticeid = memoService.login(userid, password);
        loginid = userid;

        System.out.println();
        System.out.println("loginnoticeid에 담긴 값 = " + loginnoticeid);
        System.out.println();


        if (loginnoticeid == null)
        {
            System.out.println("로그인 실패 " + loginnoticeid);
            redirectAttributes.addFlashAttribute("login", loginnoticeid);
            //model.addAttribute("login", loginnoticeid);
            //return "redirect:/notice/login";
        }
        else
        {
            System.out.println("로그인 성공 " + loginnoticeid);
            redirectAttributes.addFlashAttribute("login", loginnoticeid);
            //model.addAttribute("login", loginnoticeid);

        }
        model.addAttribute("login", loginnoticeid);
        return "redirect:/notice/list";
    }*/

    @PostMapping("/login")
    @ResponseBody
    public String NoticeLoginComplete(@RequestParam("userid") String userid, @RequestParam("password") String password) {
        loginnoticeid = memoService.login(userid, password);
        loginid = userid;
        if (loginnoticeid != null)
        {
            return "로그인 성공";
        }
        else
        {
            return "로그인 실패";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String NoticeLogout()
    {
        return "redirect:/notice/main";
    }


    // 게시판 목록
    @GetMapping("/list")
    public String NoticeList(@RequestParam(value = "searchkeyword", required = false) String searchkeyword,
                             @RequestParam(value = "page", defaultValue = "1") int page, Model model, RedirectAttributes redirectAttributes)
    {
        // list 페이지에서 required를 false로 적용해서 꼭 검색어를 입력하지 않아도 list 페이지 내용이 보이게함
        Memo memo = memoService.findById(loginnoticeid);
        model.addAttribute("memo", memo);
        model.addAttribute("login", loginnoticeid);
        model.addAttribute("loginid", loginid);


        List<Memo> alllist;

        if (searchkeyword != null )
        {
            // 입력한 키워드에 해당되는 데이터만 가져와서 alllist에 추가함
            System.out.println("검색 성공");
            List<Memo> searchResult = memoService.findBykeyword(searchkeyword);

            // 페이징 처리를 위한 코드 추가
            int pageSize = 5; // 한 페이지에 보여질 게시물 수
            int totalSize = searchResult.size(); // 검색 결과의 전체 게시물 수
            int totalPages = (int) Math.ceil((double) totalSize / pageSize); // 전체 페이지 수

            // 현재 페이지에 해당하는 검색 결과 추출
            List<Memo> currentPageSearchResult = searchResult.subList((page - 1) * pageSize, Math.min(page * pageSize, totalSize));

            // 페이지 정보 모델에 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("alllist", currentPageSearchResult);
        }
        else
        {
            alllist = memoService.findAll();
            Collections.reverse(alllist);

            // 페이징 처리를 위한 코드 추가
            int pageSize = 5; // 한 페이지에 보여질 게시물 수
            int totalSize = alllist.size(); // 전체 게시물 수
            int totalPages = (int) Math.ceil((double) totalSize / pageSize); // 전체 페이지 수

            // 현재 페이지에 해당하는 게시물 추출
            List<Memo> currentPageList = alllist.subList((page - 1) * pageSize, Math.min(page * pageSize, totalSize));

            System.out.println("pageSize = " + pageSize);
            System.out.println("totalSize = " + totalSize);
            System.out.println("totalPages = " + totalPages);
            System.out.println("currentPageList = " + currentPageList);

            // 페이지 정보 모델에 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("alllist", currentPageList);

            System.out.println("불러온 pagelist = " + alllist);
        }
        //model.addAttribute("alllist", alllist);

        return "/memo/noticelist";
    }


    // 게시글 내용확인
    @GetMapping("/view/{noticeId}")
    public String NoticeView(@PathVariable("noticeId") Long noticeid, Model model)
    {
        Memo memo = memoService.findById(noticeid);
        memoService.updateviewcount(noticeid);
        boolean checkid = loginnoticeid.equals(noticeid);
        model.addAttribute("memo", memo);
        model.addAttribute("loginid", loginid);
        model.addAttribute("checkid", checkid);
        return "/memo/noticeview";
    }


    // 글쓰기
    @GetMapping("/write/{noticeid}")
    public String NoticeWrite(@PathVariable("noticeid") Long noticeid, Model model)
    {
        Memo memo = memoService.findById(noticeid);
        model.addAttribute("memo", memo);
        model.addAttribute("loginid", loginid);
        return "/memo/noticewrite";
    }

    @PostMapping("/write/{noticeid}")
    public String NoticeWriteComplete(@PathVariable("noticeid") Long noticeid,
                                      @RequestParam("title") String title,
                                      @RequestParam("writer") String writer,
                                      @RequestParam("password") String writerpassword,
                                      @RequestParam("contents") String contents)
    {
        memoService.write(noticeid, title, writer, writerpassword, contents);
        memoService.updatenumbercount(noticeid);
        return "redirect:/notice/list";
    }


    // 글 수정
    @GetMapping("/edit/{noticeid}")
    public String NoticeEdit(@PathVariable("noticeid") Long noticeid , Model model)
    {
        Memo memo = memoService.findById(noticeid);
        model.addAttribute("memo", memo);
        model.addAttribute("loginid", loginid);
        return "/memo/noticeedit";
    }

    @PostMapping("/edit/{noticeid}")
    public String NoticeEditComplete(@PathVariable("noticeid") Long noticeid,
                                     @RequestParam("title") String title,
                                     @RequestParam("writername") String writername,
                                     @RequestParam("writerpassword") String writerpassword,
                                     @RequestParam("contents") String contents,
                                     Model model, RedirectAttributes redirectAttributes)
    {
        //memoService.editpassword(noticeid, writerpassword);
        // 글 수정할때 입력한 비번
        System.out.println("writerpassword = " + writerpassword);
        String checksavepassword = memoService.checkwriterpassword(noticeid);
        // 처음 등록할때 입력한 비번
        System.out.println("checksavepassword = " + checksavepassword);
        Memo memo = memoService.findById(noticeid);

        if (!checksavepassword.equals(writerpassword))
        {
            System.out.println();
            System.out.println("비밀번호가 일치하지 않습니다");
            System.out.println();
            model.addAttribute("memo", memo);
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/notice/edit/{noticeid}";
        }
        else
        {
            System.out.println();
            System.out.println("게시글 수정 완료");
            memoService.edit(noticeid, title, writername, writerpassword, contents);
            return "redirect:/notice/view/{noticeid}";
        }
    }

    // 삭제
    @GetMapping("/delete/{noticeid}")
    public String NoticeDelete(@PathVariable("noticeid") Long noticeid)
    {
        memoService.delete(noticeid);
        return "redirect:/notice/list";
    }
}
