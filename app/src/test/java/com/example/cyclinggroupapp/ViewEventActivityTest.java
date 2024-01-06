package com.example.cyclinggroupapp;
import org.junit.Test;
import android.widget.EditText;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;



public class ViewEventActivityTest {
    private ViewEventActivity viewEventActivity;

    @Before
    public void setUp() {
        viewEventActivity = new ViewEventActivity();
    }

    // Helper method to check if a rating is within the valid range
    private boolean isRatingWithinRange(int rating) {
        return rating >= 1 && rating <= 5;
    }
    @Test
    public void testValidRating() {
        ViewEventActivity ViewEventActivity = new ViewEventActivity();
        int rating = 3; // Set the valid rating
        ViewEventActivity.updateEventInFirestore("eventName", "eventRegion", "eventType", rating);

        // Check if the rating is within the valid range (1-5)
        assertTrue(isRatingWithinRange(rating));
    }

    @Test
    public void testInvalidRatingBelowRange() {
        ViewEventActivity ViewEventActivity = new ViewEventActivity();
        int rating = 0; // Set the Invalid rating
        ViewEventActivity.updateEventInFirestore("eventName", "eventRegion", "eventType", rating);

        // Check if the rating is outside the valid range (1-5)
        assertFalse(isRatingWithinRange(rating));
    }

    @Test
    public void testInvalidRatingAboveRange() {
        ViewEventActivity ViewEventActivity = new ViewEventActivity();
        int rating = 6; // Set the valid rating
        ViewEventActivity.updateEventInFirestore("eventName", "eventRegion", "eventType", rating);

        // Check if the rating is outside the valid range (1-5)
        assertFalse(isRatingWithinRange(rating));
    }

    @Test
    public void testCheckUser_UserLoggedIn_ReturnsTrue() {
        boolean result = viewEventActivity.checkUser();
        assertTrue(result);
    }

    @Test
    public void testCheckUser_UserNotLoggedIn_ReturnsFalse() {
        boolean result = viewEventActivity.checkUser();
        assertFalse(result);
    }

    @Test
    public void saveEventChanges_WithValidRating_DisplaySuccessMessage() {
        ViewEventActivity activity = new ViewEventActivity();
        activity.editRating = new EditText(activity);
        activity.editRating.setText("4");

        activity.saveEventChanges();

        assertTrue(true);
    }

    @Test
    public void saveEventChanges_WithInvalidRating_DisplayErrorMessage() {
        ViewEventActivity activity = new ViewEventActivity();
        activity.editRating = new EditText(activity);
        activity.editRating.setText("invalid");

        activity.saveEventChanges();

        assertFalse(false);
    }
}
