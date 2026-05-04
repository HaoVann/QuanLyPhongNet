package DTO;

public enum LoaiDichVu {
    DO_UONG("Đồ uống"),
    THUC_AN("Thức ăn"),
    THE_NAP("Thẻ nạp"),
    KHAC("Khác");
    
    private final String displayValue;

    LoaiDichVu(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
