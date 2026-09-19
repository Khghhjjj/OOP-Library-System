package javaAssignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Library {
    private Map<String, LibraryItem> catalog;
    private Map<String, Member> members;
    private Set<String> borrowedIds;

    public Library() {
        catalog = new HashMap<>();
        members = new HashMap<>();
        borrowedIds = new HashSet<>();
    }

    public void addItem(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        catalog.put(item.getId(), item);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        members.put(member.getMemberId(), member);
    }

    public void borrowItem(String memberId, String itemId) throws LibraryException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException("Member " + memberId + " does not exist.");
        }

        LibraryItem item = catalog.get(itemId);
        if (item == null) {
            throw new LibraryException("Item " + itemId + " does not exist.");
        }

        if (item.isBorrowed()) {
            throw new LibraryException("Item " + itemId + " is already out.");
        }

        if (!member.canBorrowMore()) {
            throw new LibraryException("Member " + memberId +
                    " has reached the borrowing limit.");
        }

        item.markBorrowed();
        member.addBorrowedItem(item);
        borrowedIds.add(itemId);
    }

    public void returnItem(String memberId, String itemId) throws LibraryException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException("Member " + memberId + " does not exist.");
        }

        LibraryItem item = catalog.get(itemId);
        if (item == null) {
            throw new LibraryException("Item " + itemId + " does not exist.");
        }

        if (!member.hasBorrowedItem(itemId)) {
            throw new LibraryException("Member " + memberId +
                    " did not borrow item " + itemId + ".");
        }

        item.markReturned();
        member.removeBorrowedItem(item);
        borrowedIds.remove(itemId);
    }

    public void listCatalog() {
        for (LibraryItem item : catalog.values()) {
            item.displayInfo();
        }
    }

    public void printReport() {
        System.out.println("---------- REPORT ----------");
        System.out.println("Total items : " + catalog.size());
        System.out.println("Currently out : " + borrowedIds.size());
        System.out.println("Borrowed ids : " + borrowedIds);

        Map<String, Integer> typeCounts = new HashMap<>();
        for (LibraryItem item : catalog.values()) {
            String type = item.getType();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }

        System.out.println("Items by type : " + typeCounts);
        System.out.println("Total created : " +
                LibraryItem.getTotalItemsCreated());
        System.out.println("----------------------------");
    }
}
