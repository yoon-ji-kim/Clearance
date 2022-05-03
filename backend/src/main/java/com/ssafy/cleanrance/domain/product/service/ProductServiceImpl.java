package com.ssafy.cleanrance.domain.product.service;
import com.ssafy.cleanrance.domain.product.db.entity.Product;
import com.ssafy.cleanrance.domain.product.db.repository.ProductRepository;
import com.ssafy.cleanrance.domain.product.db.repository.ProductRepositorySupport;
import com.ssafy.cleanrance.domain.product.request.ProductRegisterRequest;
import com.ssafy.cleanrance.domain.product.request.ProductUpdatePutRequest;
import com.ssafy.cleanrance.global.util.ImageUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.Optional;

@Service("productService")
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRepositorySupport productRepositorySupport;

    @Override
    public String createStore(ProductRegisterRequest productRegisterRequest, MultipartFile image1, MultipartFile image2) throws IOException {
        Product product = new Product();
        product.setCategoryId(productRegisterRequest.getCategory_id());
        product.setStoreUserId(productRegisterRequest.getStore_user_id());
        product.setProductPrice(productRegisterRequest.getProduct_price());
        product.setProductDiscount(productRegisterRequest.getProduct_discount());
        product.setProductDiscountprice((int) (productRegisterRequest.getProduct_price() * productRegisterRequest.getProduct_discount()));
        product.setProductStock(productRegisterRequest.getProduct_stock());
        product.setProductExpdate(productRegisterRequest.getProduct_expDate());
        //이미지 Base64 인코딩 소스로 변환
        MultipartFile imagefront = image1;
        File file1 = ImageUtil.multipartFileToFile(imagefront);
        byte[] byteArr1 = FileUtils.readFileToByteArray(file1);
        String base64_1 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr1);
        System.out.println(base64_1);
        product.setProductImagefront(base64_1);

        MultipartFile imageback = image2;
        File file2 = ImageUtil.multipartFileToFile(imageback);
        byte[] byteArr2 = FileUtils.readFileToByteArray(file2);
        String base64_2 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr2);
        System.out.println(base64_2);
        product.setProductImageback(base64_2);
        productRepository.save(product);
        return "OK";
    }

    @Override
    public Product findById(int productId) {
        Product product = productRepositorySupport.findById(productId);
        return product;
    }

    @Override
    public void removeProduct(Integer productId) {
        productRepositorySupport.deleteProductkByProductId(productId);
    }

    @Override
    public Optional<Product> updateStore(Product product) {
        Optional<Product> updateStore = productRepository.findById(product.getProductId()); // 수정 필요
        updateStore.ifPresent(selectStore->{
            selectStore.setProductPrice(product.getProductPrice());
            selectStore.setProductDiscount(product.getProductDiscount());
            selectStore.setProductDiscountprice((int) (product.getProductPrice() * product.getProductDiscount()));
            selectStore.setProductStock(product.getProductStock());
            selectStore.setProductExpdate(product.getProductExpdate());
            productRepository.save(selectStore);
        });
        return updateStore;
    }

//    @Override
//    public Product updateStore(ProductUpdatePutRequest productUpdatePutRequest) throws IOException {
//        Product product = productRepositorySupport.findById(productUpdatePutRequest.getProduct_id());
//        product.setCategoryId(productUpdatePutRequest.getCategory_id());
//        product.setStoreUserId(productUpdatePutRequest.getStore_user_id());
//        product.setProductPrice(productUpdatePutRequest.getProduct_price());
//        product.setProductDiscount(productUpdatePutRequest.getProduct_discount());
//        product.setProductDiscountprice((int) (productUpdatePutRequest.getProduct_price() * productUpdatePutRequest.getProduct_discount()));
//        product.setProductStock(productUpdatePutRequest.getProduct_stock());
//        product.setProductExpdate(productUpdatePutRequest.getProduct_expDate());
//        productRepository.save(product);
//        //이미지 Base64 인코딩 소스로 변환
////        MultipartFile imagefront = image1;
////        File file1 = ImageUtil.multipartFileToFile(imagefront);
////        byte[] byteArr1 = FileUtils.readFileToByteArray(file1);
////        String base64_1 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr1);
////        System.out.println(base64_1);
////        product.setProductImagefront(base64_1);
////
////        MultipartFile imageback = image2;
////        File file2 = ImageUtil.multipartFileToFile(imageback);
////        byte[] byteArr2 = FileUtils.readFileToByteArray(file2);
////        String base64_2 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr2);
////        System.out.println(base64_2);
////        product.setProductImageback(base64_2);
////        productRepository.save(product);
//        return product;
//    }


}
