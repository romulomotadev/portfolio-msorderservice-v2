package github.romulomotadev.msproducts.factory;

import github.romulomotadev.msproducts.entities.Category;

public class CategoryFactory {

    public static Category createdCategory(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");

        return category;
    }
}
