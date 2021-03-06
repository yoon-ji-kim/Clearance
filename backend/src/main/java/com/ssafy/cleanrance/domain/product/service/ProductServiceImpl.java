package com.ssafy.cleanrance.domain.product.service;
import com.ssafy.cleanrance.domain.product.db.entity.Product;
import com.ssafy.cleanrance.domain.product.db.entity.ProductCategory;
import com.ssafy.cleanrance.domain.product.db.repository.ProductCategoryRepository;
import com.ssafy.cleanrance.domain.product.db.repository.ProductRepository;
import com.ssafy.cleanrance.domain.product.db.repository.ProductRepositorySupport;
import com.ssafy.cleanrance.domain.product.request.ProductRegisterRequest;
import com.ssafy.cleanrance.domain.product.request.ProductStockUpdatePutRequest;
import com.ssafy.cleanrance.domain.product.request.ProductUpdatePutRequest;
import com.ssafy.cleanrance.domain.user.db.entity.Location;
import com.ssafy.cleanrance.domain.user.db.repository.LocationRepository;
import com.ssafy.cleanrance.global.util.ImageUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRepositorySupport productRepositorySupport;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    LocationRepository locationRepository;
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;

    @Override
    public String createStore(ProductRegisterRequest productRegisterRequest, MultipartFile image1, MultipartFile image2) throws IOException {
        Product product = new Product();
        product.setCategoryId(productRegisterRequest.getCategory_id());
        product.setStoreUserId(productRegisterRequest.getStore_user_id());
        product.setProductPrice(productRegisterRequest.getProduct_price());
        product.setProductName(productRegisterRequest.getProduct_name());
        product.setProductDiscount(productRegisterRequest.getProduct_discount());
        product.setProductDiscountprice((int) (productRegisterRequest.getProduct_price() * (1-productRegisterRequest.getProduct_discount())));
        product.setProductStock(productRegisterRequest.getProduct_stock());
        product.setProductExpdate(productRegisterRequest.getProduct_expDate());
        //????????? Base64 ????????? ????????? ??????
        MultipartFile imagefront = image1;
        File file1 = ImageUtil.multipartFileToFile(imagefront);
        //?????????1 ?????? ?????? ??????
        File resizefile1 = resizeFile(file1);
        byte[] byteArr1 = FileUtils.readFileToByteArray(resizefile1);
        String base64_1 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr1);
        System.out.println(base64_1);
        product.setProductImagefront(base64_1);

        MultipartFile imageback = image2;
        File file2 = ImageUtil.multipartFileToFile(imageback);
        //?????????2 ?????? ?????? ??????
        File resizefile2 = resizeFile(file2);
        byte[] byteArr2 = FileUtils.readFileToByteArray(resizefile2);
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

//    @Override
//    public Optional<Product> updateStore(Product product) {
//        Optional<Product> updateStore = productRepository.findById(product.getProductId()); // ?????? ??????
//        updateStore.ifPresent(selectStore->{
//            selectStore.setProductPrice(product.getProductPrice());
//            selectStore.setProductDiscount(product.getProductDiscount());
//            selectStore.setProductDiscountprice((int) (product.getProductPrice() * product.getProductDiscount()));
//            selectStore.setProductStock(product.getProductStock());
//            selectStore.setProductExpdate(product.getProductExpdate());
//            selectStore.setCategoryId(product.getCategoryId());
//
//            productRepository.save(selectStore);
//        });
//        return updateStore;
//    }

    @Override
    public List<Product> findProductByStoreId(String storeId) {
        List<Product> productlist = productRepositorySupport.findProductByStoreId(storeId);
        System.out.println(productlist.size());
        return productlist;
    }


    @Override
    public List<ProductCategory> findProductCategory() {
        List<ProductCategory> list= productCategoryRepository.findAll();
        return list;
    }

//    @Override
//    public int updateProduct(ProductUpdatePutRequest productUpdatePutRequest) {
//        if(productRepository.findById(productUpdatePutRequest.getProduct_id()).isPresent()){
//            int productId = productRepository.findById(productUpdatePutRequest.getProduct_id()).get().getProductId();
//            int categorgId = productUpdatePutRequest.getCategory_id();
//            String productName = productUpdatePutRequest.getProduct_name();
//            int productPrice = productUpdatePutRequest.getProduct_price();
//            float productDiscount = productUpdatePutRequest.getProduct_discount();
//            int productDiscountPrice = productUpdatePutRequest.getProduct_discountPrice();
//            int productStock = productUpdatePutRequest.getProduct_stock();
//            String productExpdate = productUpdatePutRequest.getProduct_expDate();
//
//            productRepository.productModifyByProductId(productId, categorgId, productName, productPrice, productDiscount, productDiscountPrice, productStock, productExpdate);
//            return SUCCESS;
//        }
//        return FAIL;
//    }

    @Override
    public Product updateProduct(ProductUpdatePutRequest productUpdatePutRequest) {
        Product product = productRepositorySupport.findById(productUpdatePutRequest.getProduct_id());
        product.setProductId(productUpdatePutRequest.getProduct_id());
        product.setCategoryId(productUpdatePutRequest.getCategory_id());
        product.setProductName(productUpdatePutRequest.getProduct_name());
        product.setStoreUserId(productUpdatePutRequest.getStore_user_id());
        product.setProductPrice(productUpdatePutRequest.getProduct_price());
        product.setProductDiscount(productUpdatePutRequest.getProduct_discount());
        product.setProductDiscountprice((int) (productUpdatePutRequest.getProduct_price() * (1-productUpdatePutRequest.getProduct_discount())));
        product.setProductStock(productUpdatePutRequest.getProduct_stock());
        product.setProductExpdate(productUpdatePutRequest.getProduct_expDate());
        productRepository.save(product);
        //????????? Base64 ????????? ????????? ??????
//        MultipartFile imagefront = image1;
//        File file1 = ImageUtil.multipartFileToFile(imagefront);
//        byte[] byteArr1 = FileUtils.readFileToByteArray(file1);
//        String base64_1 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr1);
//        System.out.println(base64_1);
//        product.setProductImagefront(base64_1);
//
//        MultipartFile imageback = image2;
//        File file2 = ImageUtil.multipartFileToFile(imageback);
//        byte[] byteArr2 = FileUtils.readFileToByteArray(file2);
//        String base64_2 ="data:image/jpeg;base64," + new Base64().encodeToString(byteArr2);
//        System.out.println(base64_2);
//        product.setProductImageback(base64_2);
//        productRepository.save(product);
        return product;
    }

//    @Override
//    public List<ProductFindStoreId> findProductByStoreId(String storeId) throws ParseException {
//        List<Product> productlist = productRepositorySupport.findProductByStoreId(storeId);
//        List<ProductFindStoreId> list = new ArrayList<>();
//        for (Product p: productlist) {
//            ProductFindStoreId productFindStoreId = new ProductFindStoreId();
//            productFindStoreId.setProduct_id(p.getProductId());
//            productFindStoreId.setCategory_id(p.getCategoryId());
//            productFindStoreId.setProduct_name(p.getProductName());
//            productFindStoreId.setProduct_price(p.getProductPrice());
//            productFindStoreId.setProduct_discount(p.getProductDiscount());
//            productFindStoreId.setProduct_discountprice(p.getProductDiscountprice());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = sdf.parse(p.getProductExpdate());
//            productFindStoreId.setProduct_expdate(date);
//            productFindStoreId.setProduct_imagefront(p.getProductImagefront());
//            list.add(productFindStoreId);
//        }
//        return list;
//    }

    private File resizeFile(File file) throws IOException {
        BufferedImage originImage = ImageIO.read(file);
        int type = originImage.getType() ==0? BufferedImage.TYPE_INT_ARGB : originImage.getType();
        BufferedImage resizeImage = resizeImg(originImage, type);
        File resizeFile = new File("resize.png");
        ImageIO.write(resizeImage, "png", resizeFile);
        return resizeFile;
    }

    private BufferedImage resizeImg(BufferedImage origin, int type){
        BufferedImage resizeImg = new BufferedImage(300,300, type);
        Graphics2D graphics2D = resizeImg.createGraphics();
        graphics2D.drawImage(origin, 0,0, 300,300, null);
        graphics2D.dispose();
        graphics2D.setComposite(AlphaComposite.Src);
        //?????? ??????
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //?????????
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        //?????????????????? ??????
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return resizeImg;
    }

    @Override
    public List<Product> findProductByDate(String userId, String date) {
        List<Product> list = productRepositorySupport.findProductByDate(userId, date);
        return list;
    }

    @Override
    public List<Product> findProductList(double ypoint, double xpoint, String storeId, int categoryId, String word) {
        List<Location> locations = locationRepository.findAll();
        //????????? ?????? 2km ?????? ?????? ??????
        List<String> loc = new ArrayList<>();
        int num = 2000;
        for (Location l: locations) {
            double x = l.getLocationXpoint();
            double y = l.getLocationYpoint();
            double theta = x- xpoint;
            double dist = Math.sin(def2rad(y)) * Math.sin(def2rad(ypoint)) + Math.cos(def2rad(y)) * Math.cos(def2rad(ypoint)) * Math.cos(def2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            //meter??? ??????
            dist = dist * 1609.344;
            if(dist <num){
                loc.add(l.getUserId());
            }
        }
        int n = loc.size();
        String[] arr = new String[n];
        for(int i=0; i<n; i++){
            arr[i] = loc.get(i);
        }
        List<Product> list;
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDate = now.format(formatter);
        if(storeId.equals("") && categoryId == 20 && word.equals("")){    //??????, ????????????, ????????? ?????? ??? ??? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndStoreUserIdIn(strDate,arr);
        }else if(!storeId.equals("") && categoryId == 20 && word.equals("")){ //????????? ?????? ?????? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndStoreUserId(strDate,storeId);
        }else if(!storeId.equals("") && categoryId != 20 && word.equals("")){ //????????? ???????????? ???????????? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndCategoryIdAndStoreUserId(strDate,categoryId,storeId);
        }else if(!storeId.equals("") && categoryId == 20 && !word.equals("")){ //??????, ?????? ?????? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndStoreUserIdAndProductNameContains(strDate,storeId, word);
        }else if(storeId.equals("") && categoryId == 20 && !word.equals("")){ //???????????? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndProductNameContainsAndStoreUserIdIn(strDate,word, arr);
        }else if(storeId.equals("") && categoryId !=20 && word.equals("")){ //???????????? ???????????? ??????
            list = productRepository.findByProductExpdateGreaterThanEqualAndStoreUserIdInAndCategoryId(strDate,arr, categoryId);
        }else if(storeId.equals("")& categoryId !=20 && !word.equals("")){ //??????????????? ?????? ?????? ??????
            list =productRepository.findByProductExpdateGreaterThanEqualAndStoreUserIdInAndCategoryIdAndProductNameContains(strDate, arr, categoryId, word);
        }else{                                                      //?????? ????????? ??????
            list= productRepository.findByProductExpdateGreaterThanEqualAndStoreUserIdAndCategoryIdAndProductNameContains(strDate,storeId,categoryId,word);
        }
        return list;
    }

    @Override
    public Page<Product> findProductByStoreId(String storeId,String word, Pageable pageable) {
        Page<Product> products;

        if(word.equals("")){
            products = productRepository.findBystoreUserId(storeId, pageable);
        }else{
            products = productRepository.findBystoreUserIdAndProductNameContains(storeId, word, pageable);
        }
        return products;
    }

    @Override
    public Product updateCountProduct(ProductStockUpdatePutRequest productStockUpdatePutRequest) {
        Product product = productRepositorySupport.findById(productStockUpdatePutRequest.getProduct_id());
        product.setProductStock(productStockUpdatePutRequest.getProduct_stock());
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> findStoreProductList(String storeId, String word) {
        List<Product> list = new ArrayList<>();
        list = productRepositorySupport.findProductByStoreIdAndWord(storeId, word);
        return list;
    }

    @Override
    public List<String> findExpdateByUser(String storeuserId) {
        List<String> list = new ArrayList<>();
        list = productRepositorySupport.findProductByExpdate(storeuserId);
        return list;
    }

    //???????????? radian?????? ??????
    private static double def2rad(double deg){
        return (deg * Math.PI /180.0);
    }
    //radian??? ???????????? ??????
    private  static double rad2deg(double rad){
        return (rad * 180/ Math.PI);
    }
}
