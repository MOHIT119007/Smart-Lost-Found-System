package service;

import model.FoundItem;
import model.LostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for matching Lost and Found items.
 * Performs rule-based category and keyword matching.
 */
public class MatchingService {

    /**
     * Determines whether a lost item and a found item are a potential match.
     */
    public static boolean isMatch(LostItem lost, FoundItem found) {
        if (lost == null || found == null) {
            return false;
        }

        // Rule 1: Category must match
        if (lost.getCategory() == null || found.getCategory() == null) {
            return false;
        }
        if (!lost.getCategory().trim().equalsIgnoreCase(found.getCategory().trim())) {
            return false;
        }

        // Rule 2: Item Name overlap (same or similar keyword)
        String lostName = lost.getItemName() != null ? lost.getItemName().toLowerCase().trim() : "";
        String foundName = found.getItemName() != null ? found.getItemName().toLowerCase().trim() : "";
        if (lostName.isEmpty() || foundName.isEmpty()) {
            return false;
        }

        if (lostName.contains(foundName) || foundName.contains(lostName)) {
            return true;
        }

        for (String word : lostName.split("\\s+")) {
            if (word.length() >= 3 && foundName.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns human-readable match status label for UI display.
     */
    public static String getMatchLabel(LostItem lost, FoundItem found) {
        return isMatch(lost, found) ? "Possible Match Found" : "No Match Found";
    }

    /**
     * Scans active found items to find matches for a given lost item.
     */
    public List<FoundItem> findMatchesForLostItem(LostItem lost, List<FoundItem> foundList) {
        List<FoundItem> matches = new ArrayList<>();
        if (lost == null || foundList == null) return matches;

        for (FoundItem f : foundList) {
            if (isMatch(lost, f)) {
                matches.add(f);
            }
        }
        return matches;
    }

    /**
     * Scans active lost items to find matches for a given found item.
     */
    public List<LostItem> findMatchesForFoundItem(FoundItem found, List<LostItem> lostList) {
        List<LostItem> matches = new ArrayList<>();
        if (found == null || lostList == null) return matches;

        for (LostItem l : lostList) {
            if (isMatch(l, found)) {
                matches.add(l);
            }
        }
        return matches;
    }
}
