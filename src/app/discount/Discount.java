package app.discount;

import app.discount.discountCondition.DiscountCondition;

public class Discount {
    private DiscountCondition[] discountConditions;

    public Discount(DiscountCondition[] discountConditions) {
        this.discountConditions = discountConditions;
    }

    public int discount(int price) {
        int discountPrice = price;

        for(DiscountCondition discountCondition : discountConditions) {
            discountCondition.checkDiscountCondition();
            if(discountCondition.isSatisfied()) discountPrice = discountCondition.applyDiscount(discountPrice);
        }
        return discountPrice;
    }
}
