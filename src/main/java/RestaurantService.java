import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private static List<Restaurant> restaurants = new ArrayList<>();

    public Restaurant findRestaurantByName(String restaurantName) throws restaurantNotFoundException{
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName)) {
                return restaurant;
            }
        }
        throw new restaurantNotFoundException(restaurantName);
    }

    public Restaurant findRestaurantByNameAndLocation(String restaurantName, String restaurantLocation) throws restaurantNotFoundException{
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName) && restaurant.getLocation().equals(restaurantLocation)) {
                return restaurant;
            }
        }
        throw new restaurantNotFoundException(restaurantName);
    }

    public Restaurant addRestaurant(String name, String location, LocalTime openingTime, LocalTime closingTime) throws restaurantInvalidException {
        if (name == null || name.trim().length() == 0 || location == null || location.trim().length() == 0) {
            throw new restaurantInvalidException(name);
        }
        for (Restaurant restaurant : restaurants) {
            // Assumption: The system allow one restaurant can have many branches in different locations.
            // The system can automatically get the customer's location in the background,
            // we don't need to change the user interface of the Restaurant Finder application.
            if (restaurant.getName().equals(name) && restaurant.getLocation().equals(location)) {
                throw new restaurantInvalidException(name);
            }
        }
        Restaurant newRestaurant = new Restaurant(name, location, openingTime, closingTime);
        restaurants.add(newRestaurant);
        return newRestaurant;
    }

    public Restaurant removeRestaurant(String restaurantName) throws restaurantNotFoundException {
        Restaurant restaurantToBeRemoved = findRestaurantByName(restaurantName);
        restaurants.remove(restaurantToBeRemoved);
        return restaurantToBeRemoved;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
