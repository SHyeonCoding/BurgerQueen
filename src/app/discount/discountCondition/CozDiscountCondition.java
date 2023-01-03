package app.discount.discountCondition;

import app.discount.discountPolicy.FixedRateDiscountPolicy;

import java.util.Scanner;

public class CozDiscountCondition {
    private boolean isSatisfied;
    private FixedRateDiscountPolicy fixedRateDiscountPolicy = new FixedRateDiscountPolicy(10);

    public boolean isSatisfied() {
        return isSatisfied;
    }

    private void setSatisfied(boolean satisfied) {
        isSatisfied = satisfied;
    }

    public void checkDisocountCondition() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("코드스테이츠 수강생이십니까? (1)_예 (2)_아니오");
        String input = scanner.nextLine();

        if(input.equals("1")) setSatisfied(true);
        else if(input.equals("2")) setSatisfied(false);
    }

    public int applyDiscount(int price) {
        return fixedRateDiscountPolicy.calculateDiscountedPrice(price);
    }
}
