package com.zufar.icedlatte.review.converter;

import com.zufar.icedlatte.openapi.dto.ProductReviewDto;
import com.zufar.icedlatte.openapi.dto.ProductReviewsAndRatingsWithPagination;
import com.zufar.icedlatte.openapi.dto.RatingMap;
import com.zufar.icedlatte.review.dto.ProductRatingCount;
import com.zufar.icedlatte.review.entity.ProductReview;
import com.zufar.icedlatte.user.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ProductReviewDtoConverter.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.FIELD)
public interface ProductReviewDtoConverter {

    ProductReviewDto EMPTY_PRODUCT_REVIEW_RESPONSE =
            new ProductReviewDto(null, null, null, null, null, null, null, null, null);

    @Mapping(target = "productReviewId", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productRating", source = "productRating")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "userName", source = "user", qualifiedByName = "toUserName")
    @Mapping(target = "userLastname", source = "user", qualifiedByName = "toUserLastName")
    @Mapping(target = "likesCount", source = "likesCount")
    @Mapping(target = "dislikesCount", source = "dislikesCount")
    ProductReviewDto toProductReviewDto(ProductReview productReview);

    @Mapping(target = "page", expression = "java(page.getTotalPages())")
    @Mapping(target = "size", expression = "java(page.getSize())")
    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "reviewsWithRatings", expression = "java(page.getContent())")
    ProductReviewsAndRatingsWithPagination toProductReviewsAndRatingsWithPagination(final Page<ProductReviewDto> page);

    @Named("toUserName")
    default String convertToUserName(UserEntity user) {
        Optional<UserEntity> userOptional = Optional.ofNullable(user);
        Optional<String> firstNameOptional = userOptional.map(UserEntity::getFirstName);
        return firstNameOptional.orElse(null);
    }

    @Named("toUserLastName")
    default String convertToUserLastName(UserEntity user) {
        Optional<UserEntity> userOptional = Optional.ofNullable(user);
        Optional<String> lastNameOptional = userOptional.map(UserEntity::getLastName);
        return lastNameOptional.orElse(null);
    }

    default RatingMap convertToProductRatingMap(List<ProductRatingCount> productRatingCountPairs) {
        var productRatingMap = new RatingMap(0, 0, 0, 0, 0);

        for (ProductRatingCount productRatingCount : productRatingCountPairs) {
            var productRating = productRatingCount.productRating();
            var count = (int) productRatingCount.count();

            switch (productRating) {
                case 5:
                    productRatingMap.setStar5(count);
                    break;
                case 4:
                    productRatingMap.setStar4(count);
                    break;
                case 3:
                    productRatingMap.setStar3(count);
                    break;
                case 2:
                    productRatingMap.setStar2(count);
                    break;
                case 1:
                    productRatingMap.setStar1(count);
                    break;
                default:
                    assert false : "Unexpected product's rating value";
            }
        }
        return productRatingMap;
    }
}
