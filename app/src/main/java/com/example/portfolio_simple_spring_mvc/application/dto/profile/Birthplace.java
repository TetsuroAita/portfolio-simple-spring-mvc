package com.example.portfolio_simple_spring_mvc.application.dto.profile;

public enum Birthplace {
    
    // 北海道
    HOKKAIDO("北海道"),

    // 東北
    AOMORI("青森県"),
    AKITA("秋田県"),
    IWATE("岩手県"),
    YAMAGATA("山形県"),
    MIYAGI("宮城県"),
    FUKUSHIMA("福島県"),

    // 関東
    TOCHIGI("栃木県"),
    GUNMA("群馬県"),
    IBARAKI("茨城県"),
    SAITAMA("埼玉県"),
    CHIBA("千葉県"),
    TOKYO("東京都"),
    KANAGAWA("神奈川県"),
    
    // 中部
    NAGANO("長野県"),
    SHIZUOKA("静岡県"),
    YAMANASHI("山梨県"),
    NIGATA("新潟県"),
    ISHIKAWA("石川県"),
    TOYAMA("富山県"),
    GIFU("岐阜県"),
    AICHI("愛知県"),
    FUKUI("福井県"),
    
    // 近畿
    MIE("三重県"),
    KYOTO("京都府"),
    SHIGA("滋賀県"),
    HYOGO("兵庫県"),
    OSAKA("大阪府"),
    NARA("奈良県"),
    WAKAYAMA("和歌山県"),

    // 中国
    TOTORI("鳥取県"),
    HIROSHIMA("広島県"),
    YAMAGUCHI("山口県"),
    SHIMANE("島根県"),
    OKAYAMA("岡山県"),

    // 四国
    KAGAWA("香川県"),
    TOKUSHIMA("徳島県"),
    EHIME("愛媛県"),
    KOCHI("高知県"),

    // 九州
    FUKUOKA("福岡県"),
    SAGA("佐賀県"),
    OITA("大分県"),
    NAGASAKI("長崎県"),
    KUMAMOTO("熊本県"),
    MIYAZAKI("宮崎県"),
    KAGOSHIMA("鹿児島県"),
    OKINAWA("沖縄県");

    private final String displayName;

    Birthplace(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }

    public static Birthplace convert(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }

        for (Birthplace birthplace : values()) {
            if (birthplace.name().equalsIgnoreCase(value)) {
                return birthplace;
            }
        }

        throw new IllegalArgumentException();
    }
}
