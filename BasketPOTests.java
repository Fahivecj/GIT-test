package ua27.basket;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BasePO;
import pages.BaseTest;
import utils.RunTestAgain;

import static org.testng.Assert.*;

public class BasketPOTests extends BaseTest {

    private String EMPTY_BASKET_TXT_MSG = dependsOnLanguage(
            "Ваш кошик порожній.\n" + "Перейти в каталог товарів",
            "Ваша корзина пуста.\n" + "Перейти в каталог товаров");

    @BeforeMethod
    public void beforeMethodConditions() {
        mainPage.open();
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перейти в пустий кошик і перевірити що немає кнопки  Видалити всі товари")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void goToEmptyBasketVerifyDeleteAllIsNotVisible() {
        header.goToBasket();

        assertTrue(basket.deleteAllBTNIsNotVisible());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перехід в повний кошик і перевірка що кнопка \"Видалити всі товари\" видима")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void goToFullBasketVerifyDeleteAllBtnIsVisible() {
        header.searchBox
                .search("линолеум")
                .getRandomAvailableProduct()
                .addToBasket();

        assertFalse(basket.deleteAllBTNIsNotVisible());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перейти в пустий кошик і перевірити що нема кнопки Оформити замовлення(тест можливо неправильний!)")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void openEmptyBasketMakeOrderBtnNotVisible() {
        header.goToBasket();
        assertFalse(basket.isMakeOrderBtnVisible());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перейти в пустий кошик і перевірити що виводиться текст \"Ваш кошик порожній\\n\" + \"Перейти в каталог товарів\"")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void goToEmptyBasketVerifyEmptyMessageIsPresentTest() {
        header.goToBasket();

        assertEquals(basket.getEmptyMsgTxt(), EMPTY_BASKET_TXT_MSG);
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка пустого кошика. Чи переходить в Каталог по посиланню")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void goToEmptyBasketProceedToCatalogueThroughEmptyMsg() {
        header.goToBasket().clickCatalogueLink();

        assertTrue(catalogue.isOpen());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("знайти товар, добавити в кошик, перевірити що залишилась кнопка Оформити замовлення")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addProductsToBasketVerifyMakeOrderBtnIsVisibleTest() {
        header.searchBox.search("Плитка из натурального камня")
                .getRandomAvailableProduct()
                .addToBasket();

        assertTrue(basket.isMakeOrderBtnVisible());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("тест 2 рази добавляє товар в кошик з карточки товару. Перевірка що товар став в кількості 2 шт")
    @Test
    public void theTestAddsTheSameProductToTheBasketTwice() {
        productCard.open(getGoods.everywhereAvailable).addToBasket();
        BasePO.waitUntilPreLoaderBasketNotDisplayed();
        popupAddToBasket.closeBasketModal();
        productCard.addToBasket();
        BasePO.waitUntilPreLoaderBasketNotDisplayed();
        assertEquals(basket.getProductsList().get(0).getQtyFirst(), 2);

    }


    private String[] items = {"топор", "вата", "лопата"};
    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка що товар видаляється хрестиком в кошику")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addSeveralGoodsToBasketDeleteOneVerifyIsDeleted() {
        for (String s : items) {
            header.searchBox.search(s)
                    .getRandomAvailableProduct()
                    .addToBasket();
        }
        int before = basket.getProductsCount();
        Reporter.log("before=" + before,true);
        basket.deleteProduct(2); //видаляє 2 товар з кошика
        int after = basket.getProductsCount();
        Reporter.log("after=" + after,true);

        assertTrue(before != after);
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка що коли відміняєш видаленння всіх товарів з кошика, то товар залишається в кошику")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addSeveralProductsDeleteAllCancelDeletionInPopUpGoodsStayInBasket() {
        for (String s : items) {
            header.searchBox.search(s)
                    .getRandomAvailableProduct()
                    .addToBasket();
        }
        int countBefore = basket.getProductsCount();
        basket.deleteAllFromBasket()
                .cancelDeleting();
        int countAfter = basket.getProductsCount();

        assertEquals(countBefore, countAfter);
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("видалення хрестиком першого товару з кошика і перевірка що залишилось ще 2")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addThreeItemsToBasketDeleteOne() {
        for (String s : items) {
            header.searchBox.search(s)
                    .getRandomAvailableProduct()
                    .addToBasket();
        }
        basket.deleteProduct(1);
        /*      Thread.sleep(2000);*/
        int qty = basket.getProductsCount();
        assertEquals(qty, 2);
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("Добавляє товар в кошик, потім змінює кількість плюсом і перевіряє що зміни відбулись")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void increaseProductItemsWhenClickIncreaseQtyBTN() {
        header.searchBox.search("лопата")
                .getRandomAvailableProduct()
                .addToBasket();
        basket.getProductsList().get(0).clickIncreaseBtn(2);
        int itemQty = basket.getProductsList().get(0).getQtyFirst();

        assertEquals(itemQty, 3);
    }

    //TODO після зняття карантинних обмежень повернути пошукковий запит "ведро"
    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("Добавляє товар в кошик, потім змінює кількість в полі і перевіряє що зміни відбулись")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void decreaseProductItemsWhenClickDecreaseQtyBTN() {
        header.searchBox.search("готові кухні")
                .getRandomAvailableProduct()
                .addToBasket().setQty(3);
        basket.getProductsList().get(0).clickDecreaseBtn(1);
        int itemQty = basket.getProductsList().get(0).getQtyFirst();
        assertEquals(itemQty, 2);

    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка чи співпадає ціна товара в КТ з ціною що в кошику")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addProductToBasketAssertPriceTest() {
        mainPage.open();
        header.searchBox.search("телефони")
                .getRandomAvailableProduct();
        double price = productCard.getActualPrice();
        productCard.addToBasket();

        double priceInBasket = basket.getProductsList().get(0).getPrice();

        assertEquals(price, priceInBasket);
    }

    //TODO після зняття карантинних обмежень повернути запит "телефони"
    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка що відкрився чекаут")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void clickProceedOrderVerifyOrderPageIsOpen() {
        header.searchBox.search("душова кабіна")
                .getRandomAvailableProduct()
                .addToBasket();
        popupAddToBasket.goToCheckout();
        assertTrue(order.isOpen());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("зміна локації в кошику і перевірка що змінилось")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addRegionalGoodsToBasketChangeRegionAssertPopUpChangeNotification() {
        String city = dependsOnLanguage("Стрий", "Стрый");
        productCard.open(getGoods.everywhereAvailable)
                .addToBasket();
        header.chooseNearestInTown(city);
        assertTrue(header.getTownInBasket().contains(city));

    }

    //TODO після відміни карантинних обмежень розкоментувати мою цеглу
    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("добавляє регіональний товар. Зміняє на регіон в якому товар не доступний. Перевіряє що товар пропадає з кошика")
    @Test(retryAnalyzer = RunTestAgain.class)
//(dependsOnMethods = "addRegionalGoodsToBasketChangeRegionAssertPopUpChangeNotification")
    public void addOneRegionalGoodsToBasketChangeStoreAssertItemInNotAvailableGoodsSector() {
      //  productCard.open("kirpich-ryadovoy-polnotelyy-m-100-priluki-").addToBasket();
        productCard.open("blok-keramicheskiy-sbk-2nf-").addToBasket();
        String prodName = productCard.getName();
        header.chooseNearestInTown(dependsOnLanguage("Стрий", "Стрый"));
        popupTermsChanging.submitChanging();

        assertEquals(basket.getEmptyMsgTxt(), EMPTY_BASKET_TXT_MSG);
        assertTrue(basket.getNotAvailableProductsNames().contains(prodName));
        assertFalse(basket.isMakeOrderBtnVisible());
    }

    //TODO після відміни карантинних обмежень розкоментувати мою цеглу
    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("добавляє регіональний товар. Зміняє на регіон в якому товар не доступний. Перевіряє що товар пропадає з кошика")
    @Test(retryAnalyzer = RunTestAgain.class)
    public void addToBasketRegItemChangeLocationAssertPopupRegionalTerms() {
      //  productCard.open("kirpich-ryadovoy-polnotelyy-m-100-priluki-").addToBasket();
        productCard.open("blok-keramicheskiy-sbk-2nf-").addToBasket();
        String prodName = productCard.getName();

        header.chooseNearestInTown(dependsOnLanguage("Стрий", "Стрый"));

        Assert.assertTrue(isPopUpOpen());
        Assert.assertTrue(popupTermsChanging.hasItem(prodName));
        String status = dependsOnLanguage("цей товар не доступний", "этот товар не доступен");
        Assert.assertTrue(popupTermsChanging.getItemStatus().equalsIgnoreCase(status));
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевіряє що якщо добавити маркетплейсний товар в кошик зі звичайним товаром, то відображається \"жовта подолбота\"")
    @Test
    public void checkYellowPodolbota() {
        productCard.open(getGoods.getMarketplaceGoods()).addToBasket();
        productCard.open(getGoods.everywhereAvailable).addToBasket();
        assertTrue(popupAddToBasket.yellowPodolbotaIsVisible());
        basket.getProductsList().get(1).delete();
        assertFalse(popupAddToBasket.yellowPodolbotaIsVisible());
    }

    @Epic(value = "Кошик")
    @Feature(value = "Функціональність кошика")
    @Description("перевірка на наявність і правильність тексту \"ПРОДАВЕЦЬ: Hubber\"")
    @Test
    public void checkSellerHubberText() {
        productCard.open(getGoods.getMarketplaceGoods()).addToBasket();
        String HUBBER_TEXT = dependsOnLanguage("ПРОДАВЕЦЬ: Hubber", "ПРОДАВЕЦ: Hubber");
        assertEquals(popupAddToBasket.getSellerHubberText(), HUBBER_TEXT);
        basket.getProductsList().get(0).delete();
        productCard.open(getGoods.everywhereAvailable).addToBasket();
        assertFalse(popupAddToBasket.sellerHubberTextIsVisible());
    }

}