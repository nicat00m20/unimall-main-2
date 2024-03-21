package com.unimall.unimall.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.unimall.unimall.entity.Category;
import com.unimall.unimall.entity.Product;
import com.unimall.unimall.model.CategoryInputModel;
import com.unimall.unimall.model.ProductInputModel;
import com.unimall.unimall.service.CategoryService;
import com.unimall.unimall.service.ProductService;
@Controller
@RequestMapping
public class AdminController {
    


	@Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    public static String UPLOAD_DIRECTORY = "/Users/Nijat/Downloads/unimall-main-2/src/main/resources/static/images";

    @GetMapping("/adminpanel/add-product")
	public String addProduct(Model model) {
		List<Category> categoryList= categoryService.getAll();
        model.addAttribute("categories",categoryList);
        return "adminpanel/add_product";
	}

    @GetMapping("/adminpanel/update-product/{id}")
	public String updateProduct(Model model, @PathVariable Long id) {
		List<Category> categoryList= categoryService.getAll();
        model.addAttribute("categories",categoryList);
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "adminpanel/products.html";
	}

    @GetMapping("/adminpanel/home")
	public String home(Model model) {
		return "adminpanel/home";
	}

    @GetMapping("/adminpanel/add-category")
	public String addCategory(Model model) {
		List<Category> categoryList= categoryService.getAll();
        model.addAttribute("categories",categoryList);
        return "adminpanel/add_category";
	}

    @GetMapping("/adminpanel/categories")
	public String categories(Model model) {
        List<Category> categoryList= categoryService.getAll();
        model.addAttribute("categories",categoryList);

		return "adminpanel/categories";
	}


    @PostMapping("/adminpanel/add-category")
	public RedirectView createCategory(@RequestParam String category_name,Long  category_parentid) {
        

        CategoryInputModel categoryInputModel= new CategoryInputModel(category_name, category_parentid);
        categoryService.add(categoryInputModel);
        return new RedirectView("http://localhost:8080/home");
	}



    @GetMapping("/adminpanel/products")
	public String products(Model model) {
         List<Product> productList= productService.getProducts();
         model.addAttribute("products",productList);
        
         List<Category> categoryList= categoryService.getAll();
         model.addAttribute("categories",categoryList);
        return "adminpanel/products";
	}

    @PostMapping("/adminpanel/add-product")
	public RedirectView createProduct(@RequestParam String product_name,String product_description,Long category,@RequestParam MultipartFile product_image, Model model) {
        UUID uuid = UUID.randomUUID();
        String file_name = uuid.toString().concat(getFileExtension(product_image.getOriginalFilename()));
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file_name);
        try {
            Files.write(fileNameAndPath, product_image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProductInputModel productInputModel = new ProductInputModel(product_name, product_description, 0, category,"/images/"+file_name );
        productService.addProduct(productInputModel);
        return new RedirectView("http://localhost:8080/home");
	}

    @PostMapping("/adminpanel/update-product/{id}")
	public RedirectView updateproduct(@PathVariable Long id, @RequestParam String product_name,String product_description,Long category,@RequestParam MultipartFile product_image, Model model) {
        UUID uuid = UUID.randomUUID();
        String file_name = uuid.toString().concat(getFileExtension(product_image.getOriginalFilename()));
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file_name);
        try {
            Files.write(fileNameAndPath, product_image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProductInputModel productInputModel = new ProductInputModel(product_name, product_description, 0, category,"/images/"+file_name );
        productService.updateProduct(productInputModel,id);
        return new RedirectView("http://localhost:8080/adminpanel/products");
	}

    
    @DeleteMapping("/adminpanel/delete-product/{id}")
    public RedirectView deleteProduct(@PathVariable Long id) {
       productService.deleteProduct(id);
       return new RedirectView("http://localhost:8080/home");
    }
    
    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}

