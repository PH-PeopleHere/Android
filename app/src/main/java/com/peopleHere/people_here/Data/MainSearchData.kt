package com.peopleHere.people_here.Data

data class MainSearchData(
    var searchImage: Int,
    var searchRegion: String,
    var searchPlace: String
)


//MainSearchData는 사용자가 정의한 클래스일 가능성이 높으며,
// 검색 화면에서 사용되는 데이터 항목을 나타낼 것입니다.
// 각 MainSearchData 인스턴스는 검색 결과의 하나의 항목을 대표할 수 있습니다.