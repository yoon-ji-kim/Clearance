package com.ssafy.cleanrance.domain.consumer.mypage.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ssafy.cleanrance.domain.consumer.mypage.bean.ProductName;
import com.ssafy.cleanrance.domain.consumer.mypage.db.entity.Book;
import com.ssafy.cleanrance.domain.consumer.mypage.db.repository.BookRepository;
import com.ssafy.cleanrance.domain.consumer.mypage.db.repository.BookRepositorySupport;
import com.ssafy.cleanrance.domain.consumer.mypage.request.BookSetUpdatePutRequest;
import com.ssafy.cleanrance.domain.product.db.entity.Product;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service("consumerService")
public class ConsumerServiceImpl implements ConsumerService{

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookRepositorySupport bookRepositorySupport;

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;

    @Override
    public List<Book> findBookByuserId(String userId) {
        return bookRepositorySupport.findBookByuserId(userId);
    }

    @Override
    public List<Product> findBookByDate(String userId, String date) {
        return null;
    }

    @Override
    public String findBookByUserIdAndBookSet(int bookSet) throws IOException, WriterException {
        QRCodeWriter writer = new QRCodeWriter();
                //QR코드에 담고 싶은 정보를 문자열로 표시
        String codeInformation = null;
//        codeInformation = "구매자 아이디: "+list.get(0).getUserId() +"\n";
//        codeInformation = "https://k6e203.p.ssafy.io/mypage/"+userId+"/"+bookSet;
        codeInformation = "https://k6e203.p.ssafy.io/mypage/"+bookSet;
        //System.out.println(list.get(0).getUserId());
        codeInformation = new String(codeInformation.getBytes("UTF-8"),"ISO-8859-1");
        BitMatrix matrix = writer.encode(codeInformation, BarcodeFormat.QR_CODE, 200,200);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
        File QRImg = new File("qr.png");
        ImageIO.write(qrImage, "png", QRImg);
        byte[] byteArr = FileUtils.readFileToByteArray(QRImg);
        String base64 = "data:image/jpeg;base64," + new Base64().encodeToString(byteArr);
        return base64;
    }

    @Override
    public List<ProductName> findBookByUserIdAndBookSetList(int bookSet) {
        List<ProductName> list = bookRepositorySupport.findBookByuserIdAndbookSet(bookSet);
        return list;
    }

    @Override
    public List<Book> findByBookSet(int book_set) {
        List<Book> book = bookRepositorySupport.findByBookSet(book_set);
        return book;
    }

    @Override
    public List<Book> updateBook(BookSetUpdatePutRequest bookSetUpdatePutRequest) {
        List<Book> book = bookRepositorySupport.findByBookSet(bookSetUpdatePutRequest.getBook_set());
        if (book.size() >= 0) {
            for (Book b : book) {
                int k = b.getProductId();
                bookRepository.BookSetModify(k);
            }
        }
//        Book book = bookRepositorySupport.findByBookSet(bookSetUpdatePutRequest.getBook_set());
//        book.setBookStatus(bookSetUpdatePutRequest.getBook_status());
//        bookRepository.save(book);
//        return book;
        return book;
    }
}
