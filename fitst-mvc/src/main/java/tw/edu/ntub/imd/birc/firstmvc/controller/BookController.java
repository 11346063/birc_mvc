package tw.edu.ntub.imd.birc.firstmvc.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.firstmvc.bean.AuthorBean;
import tw.edu.ntub.imd.birc.firstmvc.bean.BookBean;
import tw.edu.ntub.imd.birc.firstmvc.databaseconfig.dao.AuthorDAO;
import tw.edu.ntub.imd.birc.firstmvc.databaseconfig.entity.Author;
import tw.edu.ntub.imd.birc.firstmvc.databaseconfig.entity.Book;
import tw.edu.ntub.imd.birc.firstmvc.service.BookService;
import tw.edu.ntub.imd.birc.firstmvc.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.firstmvc.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.firstmvc.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.firstmvc.util.json.object.ObjectData;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/book")
public class BookController {
    private final BookService bookService;
    private final AuthorDAO authorDAO;

    public String DateToString (Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    //新增
    @PostMapping
    public ResponseEntity<String> createBook(@Valid @RequestBody BookBean bookBean,
                                                BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
//        bookBean.setPublication_date(LocalDate.parse(bookBean.getPublication_date_str()));
//        System.out.println(bookBean.getId() + "\n" + bookBean.getName() + "\n"
//                + bookBean.getPublication_date() + "\n" + bookBean.getAuthonr_id() + "\n"
//               + bookBean.getCreate_time() + "\n");

        bookService.save(bookBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    // 更新
    @PatchMapping(path = "/{id}")
    public ResponseEntity<String> updateScore(@RequestBody BookBean bookBean, @PathVariable Integer id) {

//        bookBean.setPublication_date(LocalDate.parse(bookBean.getPublication_date_str()));
        bookService.update(id, bookBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    //刪除
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Integer id) {
        bookService.delete(id);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }

    //取得全部
//    @GetMapping(path = "")
//    public ResponseEntity<String> getAll() {
//        ArrayData arrayData = new ArrayData();
//        for (BookBean bookBean : bookService.findAll()) {
//            ObjectData objectData = arrayData.addObject();
//            objectData.add("id", bookBean.getId());
//            objectData.add("name", bookBean.getName());
//            objectData.add("publication_date", (bookBean.getPublication_date()));
//            objectData.add("author_id", bookBean.getAuthonr_id());
//            objectData.add("create_time", bookBean.getCreate_time());
////            objectData.add("author_name", bookBean.getAuthor().getName());
//        }
//        return ResponseEntityBuilder.success()
//                .message("查詢成功")
//                .data(arrayData)
//                .build();
//    }
    @GetMapping
    public ResponseEntity<String> getAllBooks() {
//        List<Book> book = bookService.findAll();
        ArrayData arrayData = new ArrayData();
//        return ResponseEntity.ok(book);  // 回傳所有書籍
        for (BookBean bookBean : bookService.findAll()) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", bookBean.getId());
            objectData.add("name", bookBean.getName()); // 返回書名
            objectData.add("publicationDate", DateToString(bookBean.getPublication_date()));
            objectData.add("authorId", bookBean.getAuthor_id());
            objectData.add("authorName", bookBean.getAuthor().getName());// 返回作者名
            objectData.add("authorDescription", bookBean.getAuthor().getInfo());
            objectData.add("birthDate", DateToString(bookBean.getAuthor().getBirthdate()));
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping(params = {"id"})
    public ResponseEntity<String> findById(@RequestParam(name = "id") Integer id) {
        ArrayData arrayData = new ArrayData();
        for (BookBean bookBean : bookService.findId(id)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", bookBean.getId());
            objectData.add("name", bookBean.getName());
            objectData.add("info", bookBean.getInfo());
            objectData.add("publicationDate", DateToString(bookBean.getPublication_date()));
            objectData.add("authorName", bookBean.getAuthor().getName());
            objectData.add("authorId", bookBean.getAuthor().getId());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }
}
