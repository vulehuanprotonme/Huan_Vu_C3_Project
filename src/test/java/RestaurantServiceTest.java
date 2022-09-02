import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RestaurantServiceTest {

    RestaurantService service = new RestaurantService();
    Restaurant restaurant;
    //REFACTOR ALL THE REPEATED LINES OF CODE

    @BeforeEach
    void beforeEach() throws itemInvalidException, restaurantInvalidException {
        restaurant = service.addRestaurant("Amelie's cafe", "Chennai", LocalTime.parse("10:30:00"), LocalTime.parse("22:00:00"));
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    @AfterEach
    void afterEach() throws restaurantNotFoundException {
        if (service.getRestaurants().size() == 0) {
            return;
        }
        service.removeRestaurant("Amelie's cafe");
    }

    //>>>>>>>>>>>>>>>>>>>>>>SEARCHING<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void searching_for_existing_restaurant_should_return_expected_restaurant_object() throws restaurantNotFoundException {
        assertEquals(restaurant.getName(), service.findRestaurantByName("Amelie's cafe").getName());
    }

    //You may watch the video by Muthukumaran on how to write exceptions in Course 3: Testing and Version control: Optional content
    @Test
    public void searching_for_non_existing_restaurant_should_throw_exception() throws restaurantNotFoundException {
        assertThrows(restaurantNotFoundException.class, () -> {
            service.findRestaurantByName("Pantry d'or");
        });
    }
    //<<<<<<<<<<<<<<<<<<<<SEARCHING>>>>>>>>>>>>>>>>>>>>>>>>>>




    //>>>>>>>>>>>>>>>>>>>>>>ADMIN: ADDING & REMOVING RESTAURANTS<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void remove_restaurant_should_reduce_list_of_restaurants_size_by_1() throws restaurantNotFoundException {
        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.removeRestaurant("Amelie's cafe");
        assertEquals(initialNumberOfRestaurants-1, service.getRestaurants().size());
    }

    @Test
    public void removing_restaurant_that_does_not_exist_should_throw_exception() throws restaurantNotFoundException {
        assertThrows(restaurantNotFoundException.class,()->service.removeRestaurant("Pantry d'or"));
    }

    @Test
    public void add_restaurant_should_increase_list_of_restaurants_size_by_1() throws restaurantInvalidException {
        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.addRestaurant("Pumpkin Tales","Chennai",LocalTime.parse("12:00:00"),LocalTime.parse("23:00:00"));
        assertEquals(initialNumberOfRestaurants + 1,service.getRestaurants().size());
    }

    @Test
    public void add_another_restaurant_with_the_same_name_and_same_location_should_throw_exception() {
        List<Restaurant> restaurants = service.getRestaurants();
        Restaurant firstRestaurant = restaurants.get(0);
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(firstRestaurant.getName(), firstRestaurant.getLocation(), LocalTime.parse("12:00:00"), LocalTime.parse("23:00:00")));
    }

    @Test
    public void add_another_restaurant_with_the_same_name_and_different_location_should_increase_list_of_restaurants_size_by_1() throws restaurantInvalidException {
        List<Restaurant> restaurants = service.getRestaurants();
        int initialNumberOfRestaurants = restaurants.size();
        service.addRestaurant(restaurants.get(0).getName(), "New Delhi", LocalTime.parse("12:00:00"), LocalTime.parse("23:00:00"));
        assertEquals(initialNumberOfRestaurants + 1, service.getRestaurants().size());
    }

    @Test
    public void two_customers_with_different_locations_should_get_the_relevant_restaurant_base_on_location() throws restaurantInvalidException, restaurantNotFoundException {
        List<Restaurant> restaurants = service.getRestaurants();
        Restaurant restaurant1 = restaurants.get(0);
        String restaurantName = restaurant1.getName();
        String anotherLocation = "Mumbai";
        service.addRestaurant(restaurantName, anotherLocation, LocalTime.parse("06:00:00"), LocalTime.parse("14:00:00"));
        // Assumption: The system can automatically get the customer's location in the background,
        // we don't need to change the user interface of the Restaurant Finder application.
        Restaurant restaurant2 = service.findRestaurantByNameAndLocation(restaurantName, anotherLocation);
        assertEquals(restaurant1.getName(), restaurant2.getName());
        assertNotEquals(restaurant1.getLocation(), restaurant2.getLocation());
        assertEquals(restaurant2.getLocation(), anotherLocation);
        // Assumption: The opening time and closing time can be different depending on the number of customers during peak hours in each location.
        assertNotEquals(restaurant1.getOpeningTime(), restaurant2.getOpeningTime());
        assertNotEquals(restaurant1.getClosingTime(), restaurant2.getClosingTime());
    }

    @Test
    public void add_restaurant_with_null_name_should_throw_exception() {
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                null,
                anotherRestaurant.getLocation(),
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }

    @Test
    public void add_restaurant_with_blank_name_should_throw_exception(){
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                "",
                anotherRestaurant.getLocation(),
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }

    @Test
    public void add_restaurant_with_white_space_only_name_should_throw_exception(){
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                " ",
                anotherRestaurant.getLocation(),
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }

    @Test
    public void add_restaurant_with_null_location_should_throw_exception(){
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                anotherRestaurant.getName(),
                null,
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }

    @Test
    public void add_restaurant_with_blank_location_should_throw_exception(){
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                anotherRestaurant.getName(),
                "",
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }
    @Test
    public void add_restaurant_with_white_space_only_location_should_throw_exception(){
        Restaurant anotherRestaurant = anotherRestaurantSample();
        assertThrows(restaurantInvalidException.class, () -> service.addRestaurant(
                anotherRestaurant.getName(),
                " ",
                anotherRestaurant.getOpeningTime(),
                anotherRestaurant.getClosingTime()
        ));
    }

    private Restaurant anotherRestaurantSample() {
        return new Restaurant("Pumpkin Tales", "Chennai", LocalTime.parse("12:00:00"), LocalTime.parse("23:00:00"));
    }
    //<<<<<<<<<<<<<<<<<<<<ADMIN: ADDING & REMOVING RESTAURANTS>>>>>>>>>>>>>>>>>>>>>>>>>>
}