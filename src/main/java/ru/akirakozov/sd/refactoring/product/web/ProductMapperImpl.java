package ru.akirakozov.sd.refactoring.product.web;

import ru.akirakozov.sd.refactoring.product.model.Product;

import java.util.List;

import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_BREAK;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.LINE_BREAK;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.TAB;

public class ProductMapperImpl implements ProductMapper {
    @Override
    public String map(Product product) {
        return writeProducts(List.of(product));
    }

    @Override
    public String map(List<Product> products) {
        return writeProducts(products);
    }

    private String writeProducts(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            sb.append(product.getName());
            sb.append(TAB);
            sb.append(product.getPrice());
            sb.append(HTML_BREAK);
            if (i < products.size() - 1) {
                sb.append(LINE_BREAK);
            }
        }
        return sb.toString();
    }
}
