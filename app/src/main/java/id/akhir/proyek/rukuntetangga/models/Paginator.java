package id.akhir.proyek.rukuntetangga.models;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {
    public int TOTAL_NUM_ITEMS = 0;
    public static final int ITEMS_PER_PAGE = 10;
    public int ITEMS_REMAINING = 0;
    public int LAST_PAGE = 0;

    public void setTotalNumItems(int totalNumItems) {
        TOTAL_NUM_ITEMS = totalNumItems;
    }

    public int getTotalNumItems() {
        return TOTAL_NUM_ITEMS;
    }

    public List<T> generatePage(int currentPage, List<T> data) {
        int startItem = currentPage*ITEMS_PER_PAGE;
        int numOfData = ITEMS_PER_PAGE;
        List<T> pageData = new ArrayList<>();
        ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;

        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0)  {
            for (int i = startItem; i < startItem+ITEMS_REMAINING; i++) {
                pageData.add(data.get(i));
            }
        } else {
            for (int i = startItem; i < startItem+numOfData; i++) {
                pageData.add(data.get(i));
            }
        }
        return pageData;
    }
}
