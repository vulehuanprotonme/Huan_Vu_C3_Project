import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    //REFACTOR ALL THE REPEATED LINES OF CODE
    @BeforeEach
    void beforeEach() throws itemInvalidException {
        restaurant = Mockito.spy(new Restaurant("Amelie's cafe", "Chennai", LocalTime.parse("10:30:00"), LocalTime.parse("22:00:00")));
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        Mockito.when(restaurant.getCurrentTime()).thenReturn(restaurant.openingTime.plusHours(1));
        assertTrue(restaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        Mockito.when(restaurant.getCurrentTime()).thenReturn(restaurant.openingTime.minusHours(1));
        assertFalse(restaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time_when_opening_time_and_closing_time_between_2_days() {
        Restaurant anotherRestaurant = Mockito.spy(new Restaurant("Amelie's cafe", "New Delhi", LocalTime.parse("22:00:00"), LocalTime.parse("03:00:00")));
        Mockito.when(anotherRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("01:00:00"));
        assertTrue(anotherRestaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time_when_opening_time_and_closing_time_between_2_days() {
        Restaurant anotherRestaurant = Mockito.spy(new Restaurant("Amelie's cafe", "New Delhi", LocalTime.parse("22:00:00"), LocalTime.parse("03:00:00")));
        Mockito.when(anotherRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("21:00:00"));
        assertFalse(anotherRestaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() throws itemInvalidException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }

    @Test
    public void adding_another_item_with_same_name_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu("Sweet corn soup", 319));
    }

    @Test
    public void adding_item_with_null_name_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu(null, 319));
    }

    @Test
    public void adding_item_with_blank_name_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu("", 319));
    }

    @Test
    public void adding_item_with_white_space_only_name_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu(" ", 319));
    }

    @Test
    public void adding_item_with_price_less_than_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu("Sizzling brownie", -1));
    }

    @Test
    public void adding_item_with_price_equal_zero_to_menu_should_throw_exception() {
        assertThrows(itemInvalidException.class,
                () -> restaurant.addToMenu("Sizzling brownie", 0));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void cost_should_be_zero_if_do_not_choose_any_items() throws itemNotFoundException {
        assertEquals(0, restaurant.calculateOrderValue(null));
        assertEquals(0, restaurant.calculateOrderValue(Collections.emptyList()));
    }

    @Test
    public void giving_not_existed_selected_item_name_should_throw_exception() throws itemNotFoundException {
        assertThrows(itemNotFoundException.class,
                () -> restaurant.calculateOrderValue(List.of("Not existed")));
    }

    @Test
    public void cost_should_be_greater_than_zero_if_choose_at_least_one_item() throws itemNotFoundException {
        List<Item> items = restaurant.getMenu();
        List<String> selectedItemNames = new ArrayList<>();
        selectedItemNames.add(items.get(0).getName());
        assertThat(restaurant.calculateOrderValue(selectedItemNames), greaterThan(0l));
        selectedItemNames.add(items.get(1).getName());
        assertThat(restaurant.calculateOrderValue(selectedItemNames), greaterThan(0l));
    }

    @Test
    public void cost_should_be_the_item_price_if_choose_only_one_item() throws itemNotFoundException {
        Item firstItem = restaurant.getMenu().get(0);
        assertEquals(firstItem.getPrice(), restaurant.calculateOrderValue(List.of(firstItem.getName())));
    }

    @Test
    public void cost_should_be_equal_the_total_selected_items_price() throws itemNotFoundException {
        List<Item> items = restaurant.getMenu();
        long total = items.get(0).getPrice() + items.get(1).getPrice();
        assertEquals(total, restaurant.calculateOrderValue(Arrays.asList(items.get(0).getName(), items.get(1).getName())));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<ORDER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}