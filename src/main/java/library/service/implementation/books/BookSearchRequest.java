package library.service.implementation.books;

import library.config.ProjectConstants;

public class BookSearchRequest {
    private short minYoC;
    private short maxYoC;
    private short minYoU;
    private short maxYoU;
    private String bookName;
    private String creatorName;
    private String bookStatusName;
    private long minCost;
    private long maxCost;

    public short getMinYoC() {
        return minYoC;
    }

    public void setMinYoC(short minYoC) {
        this.minYoC = minYoC;
    }

    public short getMaxYoC() {
        return maxYoC;
    }

    public void setMaxYoC(short maxYoC) {
        this.maxYoC = maxYoC;
    }

    public short getMinYoU() {
        return minYoU;
    }

    public void setMinYoU(short minYoU) {
        this.minYoU = minYoU;
    }

    public short getMaxYoU() {
        return maxYoU;
    }

    public void setMaxYoU(short maxYoU) {
        this.maxYoU = maxYoU;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String tagName) {
        this.bookName = tagName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getBookStatusName() {
        return bookStatusName;
    }

    public void setBookStatusName(String bookStatusName) {
        this.bookStatusName = bookStatusName;
    }

    public long getMinCost() {
        return minCost;
    }

    public void setMinCost(long minCost) {
        this.minCost = minCost;
    }

    public long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(long maxCost) {
        this.maxCost = maxCost;
    }

    BookSearchRequest(){
        minCost = ProjectConstants.MIN_COST;
        maxCost = ProjectConstants.MAX_COST;
        minYoC = ProjectConstants.MIN_YEAR_OF_CREATION;
        maxYoC = ProjectConstants.MAX_YEAR_OF_CREATION;
        minYoU = ProjectConstants.MIN_YEAR_OF_UPLOAD;
        maxYoU = ProjectConstants.MAX_YEAR_OF_UPLOAD;
        bookName ="%";
        creatorName ="%";
        bookStatusName = "%";
    }
}
