package jp.wataju.context

class RegionContext {
    var region: Region

    fun setRegion(prefecture: String) {
        region.checkRegion(this, prefecture)
    }
    fun changeRegion(region: Region) {
        this.region = region
    }

    init {
        region = AreaA
    }
}

interface Region {
    fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    )
}

object AreaA: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "青森県",
            "岩手県",
            "秋田県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "大分県" -> regionContext.changeRegion(AreaB)

            "宮城県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県" -> regionContext.changeRegion(AreaC)

            "熊本県",
            "宮崎県",
            "鹿児島県" -> regionContext.changeRegion(AreaD)

            "沖縄県" -> regionContext.changeRegion(AreaE)

            else -> regionContext.changeRegion(AreaNon)
        }
    }
}

object AreaB: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "北海道" -> regionContext.changeRegion(AreaA)

            "宮城県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県" -> regionContext.changeRegion(AreaC)

            "熊本県",
            "宮崎県",
            "鹿児島県" -> regionContext.changeRegion(AreaD)

            "沖縄県" -> regionContext.changeRegion(AreaE)

            else -> regionContext.changeRegion(AreaNon)
        }
    }
}

object AreaC: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "北海道" -> regionContext.changeRegion(AreaA)

            "青森県",
            "岩手県",
            "秋田県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "大分県" -> regionContext.changeRegion(AreaB)

            "熊本県",
            "宮崎県",
            "鹿児島県" -> regionContext.changeRegion(AreaD)

            "沖縄県" -> regionContext.changeRegion(AreaE)

            else -> regionContext.changeRegion(AreaNon)
        }
    }
}

object AreaD: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "北海道" -> regionContext.changeRegion(AreaA)

            "青森県",
            "岩手県",
            "秋田県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "大分県" -> regionContext.changeRegion(AreaB)

            "宮城県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県" -> regionContext.changeRegion(AreaC)

            "沖縄県" -> regionContext.changeRegion(AreaE)

            else -> regionContext.changeRegion(AreaNon)
        }
    }
}

object AreaE: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "北海道" -> regionContext.changeRegion(AreaA)

            "青森県",
            "岩手県",
            "秋田県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "大分県" -> regionContext.changeRegion(AreaB)

            "宮城県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県" -> regionContext.changeRegion(AreaC)

            "熊本県",
            "宮崎県",
            "鹿児島県" -> regionContext.changeRegion(AreaD)

            else -> regionContext.changeRegion(AreaNon)
        }
    }
}

object AreaNon: Region {
    override fun checkRegion(
        regionContext: RegionContext,
        prefecture: String
    ) {
        when (prefecture) {
            "北海道" -> regionContext.changeRegion(AreaA)

            "青森県",
            "岩手県",
            "秋田県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "大分県" -> regionContext.changeRegion(AreaB)

            "宮城県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県" -> regionContext.changeRegion(AreaC)

            "熊本県",
            "宮崎県",
            "鹿児島県" -> regionContext.changeRegion(AreaD)

            "沖縄県" -> regionContext.changeRegion(AreaE)
        }
    }
}
