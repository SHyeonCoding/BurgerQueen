package app;

import app.product.Product;
import app.product.ProductRepository;
import app.product.subproduct.BurgerSet;
import app.product.subproduct.Drink;
import app.product.subproduct.Hamburger;
import app.product.subproduct.Side;

import java.util.Scanner;

public class Cart {
    private Product[] items = new Product[0];
    private ProductRepository productRepository;
    private Menu menu;
    Scanner scanner = new Scanner(System.in);

    public Cart(ProductRepository productRepository, Menu menu) {
        this.productRepository = productRepository;
        this.menu = menu;
    }

    public void printCart() {
        System.out.println("π μ₯λ°κ΅¬λ");
        System.out.println("-".repeat(60));

        printCartItemDetails();

        System.out.println("-".repeat(60));
        System.out.printf("ν©κ³ : %dμ\n", calculatorTotalPrice());

        System.out.println("μ΄μ μΌλ‘ λμκ°λ €λ©΄ μν°λ₯Ό ν΄λ¦­νμΈμ.");
        scanner.nextLine();
    }

    protected void printCartItemDetails() {
        for(Product product : items) {
            if(product instanceof BurgerSet) {
                BurgerSet burgerSet = (BurgerSet) product;
                System.out.printf(
                        "%-8s %6dμ (%s(μΌμ²© %dκ°), %s(λΉ¨λ %s))\n",
                        product.getName(),
                        product.getPrice(),
                        burgerSet.getSide().getName(),
                        burgerSet.getSide().getKetchup(),
                        burgerSet.getDrink().getName(),
                        burgerSet.getDrink().hasStraw() ? "μμ" : "μμ"
                );
            }
            else if(product instanceof Hamburger) {
                System.out.printf(
                        "  %-8s %6dμ (λ¨ν)\n",
                        product.getName(),
                        product.getPrice()
                );
            }
            else if(product instanceof Side) {
                System.out.printf(
                        "  %-8s %6dμ (μΌμ²© %dκ°)\n",
                        product.getName(),
                        product.getPrice(),
                        ((Side)product).getKetchup()
                );
            }
            else if(product instanceof Drink) {
                System.out.printf(
                        "  %-8s %6dμ (λΉ¨λ %s)\n",
                        product.getName(),
                        product.getPrice(),
                        ((Drink)product).hasStraw() ? "μμ" : "μμ"
                );
            }
        }
    }

    protected int calculatorTotalPrice() {
        int totalPrice = 0;
        for(Product product : items) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public void addToCart(int productId) {
        Product product = productRepository.findById(productId);

        chooseOption(product);

        if(product instanceof Hamburger) {
            Hamburger hamburger = (Hamburger) product;
            if(hamburger.isBurgerSet()) product = composeSet(hamburger);
        }

        Product newProduct;
        if(product instanceof Hamburger) newProduct = new Hamburger((Hamburger) product);
        else if(product instanceof Side) newProduct = new Side((Side) product);
        else if(product instanceof Drink) newProduct = new Drink((Drink) product);
        else newProduct = new BurgerSet((BurgerSet) product);

        Product[] newItems = new Product[items.length + 1];
        System.arraycopy(items, 0, newItems, 0, items.length);
        newItems[newItems.length - 1] = newProduct;
        items = newItems;

        System.out.printf("[π’] %sλ₯Ό μ₯λ°κ΅¬λμ λ΄μμ΅λλ€.\n", product.getName());
    }

    private void chooseOption(Product product) {
        String input;

        if(product instanceof Hamburger) {
            System.out.printf(
                    "λ¨νμΌλ‘ μ£Όλ¬Ένμκ² μ΄μ? (1)_λ¨ν(%dμ) (2)_μΈνΈ(%dμ)\n",
                    product.getPrice(),
                    ((Hamburger) product).getBurgerSetPrice()
            );
            input = scanner.nextLine();
            if(input.equals("2")) {
                ((Hamburger) product).setIsBurgerSet(true);
            }
        }
        else if(product instanceof Side) {
            System.out.println("μΌμ²©μ λͺ κ° νμνμ κ°μ?");
            input = scanner.nextLine();
            ((Side) product).setKetchup(Integer.parseInt(input));
        }
        else if(product instanceof Drink) {
            System.out.println("λΉ¨λκ° νμνμ κ°μ? (1)_μ (2)_μλμ€");
            input = scanner.nextLine();
            if(input.equals("2")) {
                ((Drink) product).setHasStraw(false);
            }
        }
    }

    private BurgerSet composeSet(Hamburger hamburger) {
        System.out.println("μ¬μ΄λλ₯Ό κ³¨λΌμ£ΌμΈμ.");
        menu.printSides(false);

        String sideId = scanner.nextLine();
        Side side = (Side) productRepository.findById(Integer.parseInt(sideId));
        chooseOption(side);

        System.out.println("μλ£λ₯Ό κ³¨λΌμ£ΌμΈμ.");
        menu.printDrinks(false);

        String drinkId = scanner.nextLine();
        Drink drink = (Drink) productRepository.findById(Integer.parseInt(drinkId));
        chooseOption(drink);

        String name = hamburger.getName() + "μΈνΈ";
        int price = hamburger.getBurgerSetPrice();
        int kcal = hamburger.getKcal() + side.getKcal() + drink.getKcal();

        return new BurgerSet(name, price, kcal, hamburger, side, drink);
    }
}

